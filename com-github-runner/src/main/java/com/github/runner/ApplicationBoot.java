package com.github.runner;

import com.github.app.spi.utils.VertxFactory;
import com.github.server.ApiServerVerticle;
import com.github.app.spi.config.AppServerConfig;
import com.github.app.spi.services.impl.SystemOperationServiceImpl;
import com.github.app.spi.utils.AppServerConfigLoader;
import com.github.app.utils.JacksonUtils;
import com.github.app.utils.ServerEnvConstant;
import com.github.server.ServerDaemonVerticle;
import com.github.server.ZookeeperServerVerticle;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Vertx;
import io.vertx.core.cli.CLI;
import io.vertx.core.cli.CLIException;
import io.vertx.core.cli.CommandLine;
import io.vertx.core.cli.Option;
import io.vertx.core.json.JsonObject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.LoggerContext;

import java.io.File;
import java.net.URI;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.function.BiConsumer;
import java.util.function.Function;

public class ApplicationBoot {

    private static CLI cli;

    static {
        setup();
        cli = CLI.create("ApplicationBooter").setSummary("An application runner instance")
                .addOption(new Option().setLongName("name").setShortName("name")
                        .setDescription("the application name which want bo boot").setRequired(true))
                .addOption(new Option().setLongName("file").setShortName("file")
                        .setDescription("the sql file which you want to restore").setRequired(false));

    }

    static Optional<CommandLine> cli(String[] args) {

        CommandLine commandLine = null;
        try {
            List<String> userCommandLineArguments = Arrays.asList(args);
            commandLine = cli.parse(userCommandLineArguments);
        } catch (CLIException e) {
            printCmdUsage();
            System.exit(-1);
        }
        return Optional.ofNullable(commandLine);
    }

    public static void main(String[] args) {

        Optional<CommandLine> commandLine = cli(args);
        if (commandLine.isPresent()) {
            String name = commandLine.get().getRawValueForOption(commandLine.get().cli().getOption("name"));

            BootType bootType = null;
            try {
                bootType = BootType.valueOf(name.toUpperCase());
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println(String.format("Current application name is: %s, Available name :%s", name, JacksonUtils.serialize(BootType.values())));
                System.exit(-1);
            }

            if (bootType != null) {
                switch (bootType) {
                    case RESTORE: {
                        String file = commandLine.get()
                                .getRawValueForOption(commandLine.get().cli().getOption("file"));
                        new SystemOperationServiceImpl().restore(AppServerConfigLoader.getServerCfg(), file);
                        break;
                    }
                    case BACKUP: {
                        new SystemOperationServiceImpl().backup(AppServerConfigLoader.getServerCfg());
                        break;
                    }
                    case SERVER: {
                        Vertx vertx = VertxFactory.vertx();
                        DeploymentOptions deploymentOptions = new DeploymentOptions();
                        deploymentOptions.setConfig(JsonObject.mapFrom(AppServerConfigLoader.getServerCfg()));

                        CompletableFuture<String> zkFuture = new CompletableFuture<>();
                        CompletableFuture<String> apiFuture = new CompletableFuture<>();
                        CompletableFuture<String> deamonServerFuture = new CompletableFuture<>();

                        vertx.deployVerticle(ZookeeperServerVerticle.class, deploymentOptions, ar -> {
                            if (ar.succeeded()) {
                                zkFuture.complete(ar.result());
                            } else {
                                zkFuture.completeExceptionally(ar.cause());
                            }
                        });

                        zkFuture.whenComplete((aVoid, throwable) -> {
                            vertx.deployVerticle(ApiServerVerticle.class, deploymentOptions, ar -> {
                                if (ar.succeeded()) {
                                    apiFuture.complete(ar.result());
                                } else {
                                    apiFuture.completeExceptionally(ar.cause());
                                }
                            });
                        });

                        zkFuture.whenComplete((aVoid, cause) -> {
                            vertx.deployVerticle(ServerDaemonVerticle.class, deploymentOptions, ar -> {
                                if (ar.succeeded()) {
                                    deamonServerFuture.complete(ar.result());
                                } else {
                                    deamonServerFuture.completeExceptionally(ar.cause());
                                }
                            });
                        });

                        CompletableFuture.allOf(zkFuture, apiFuture, deamonServerFuture).exceptionally(throwable -> {
                            try {
                                vertx.undeploy(zkFuture.get());
                                vertx.undeploy(apiFuture.get());
                                vertx.undeploy(deamonServerFuture.get());
                                vertx.close();
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            } catch (ExecutionException e) {
                                e.printStackTrace();
                            }
                            return null;
                        }).join();
                        break;
                    }
                    default: {
                        printCmdUsage();
                        System.exit(-1);
                    }
                }
            }
        } else {
            System.exit(-1);
        }
    }

    public static void setup() {
        /**
         * set log4j2 cfg
         */
        LoggerContext ctx = (LoggerContext) LogManager.getContext(false);
        String path = ServerEnvConstant.getLog4j2CfgFilePath();

        File file = new File(path);
        URI configURI = file.toURI();
        ctx.setConfigLocation(configURI);
        ctx.reconfigure();
        /**
         * set vert.x log impl
         */
        System.setProperty("vertx.logger-delegate-factory-class-name", "io.vertx.core.logging.Log4j2LogDelegateFactory");

        /**
         * set vertx cache base directory
         */
        AppServerConfig config  = AppServerConfigLoader.getServerCfg();
        String cacheBase = config.getVertxCacheDir();
        System.setProperty("vertx.cacheDirBase", cacheBase);
    }

    private static void printCmdUsage() {
        StringBuilder builder = new StringBuilder();
        cli.usage(builder);
        System.out.println(builder.toString());
    }
}
