package com.github.runner;

import com.github.app.api.config.AppServerConfig;
import com.github.app.api.services.impl.MySqlOperationServiceImpl;
import com.github.app.api.utils.AppServerConfigLoader;
import com.github.app.api.verticles.HttpServerVerticle;
import com.github.app.utils.JacksonUtils;
import com.github.app.utils.ServerEnvConstant;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import io.vertx.core.cli.CLI;
import io.vertx.core.cli.CLIException;
import io.vertx.core.cli.CommandLine;
import io.vertx.core.cli.Option;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.dropwizard.DropwizardMetricsOptions;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.LoggerContext;

import java.io.File;
import java.net.URI;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

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
                        new MySqlOperationServiceImpl().restore(AppServerConfigLoader.getServerCfg(), file);
                        break;
                    }
                    case INSTALL: {
                        new MySqlOperationServiceImpl().install(AppServerConfigLoader.getServerCfg());
                        break;
                    }
                    case BACKUP: {
                        new MySqlOperationServiceImpl().backup(AppServerConfigLoader.getServerCfg());
                        break;
                    }
                    case SERVER: {
                        DeploymentOptions deploymentOptions = new DeploymentOptions();
                        deploymentOptions.setConfig(JsonObject.mapFrom(AppServerConfigLoader.getServerCfg()));
                        Vertx vertx = Vertx.vertx(new VertxOptions().setMetricsOptions(new DropwizardMetricsOptions().setJmxEnabled(true)));
                        vertx.deployVerticle(HttpServerVerticle.class, deploymentOptions, ar -> {
                            if (ar.succeeded()) {
                            } else {
                                vertx.close();
                                System.exit(-2);
                            }
                        });
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
