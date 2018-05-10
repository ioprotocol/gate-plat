package com.github.runner;

import com.github.app.server.HttpServerVerticle;
import com.github.app.spi.utils.AppServerConfigLoader;
import com.github.app.utils.ServerEnvConstant;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.dropwizard.DropwizardMetricsOptions;

import java.util.concurrent.atomic.AtomicBoolean;

public class WinBootstrap {
    private static Vertx vertx;
    private static String verticleId;

    private static AtomicBoolean isStop = new AtomicBoolean(false);

    /**
     * 启动服务
     *
     * @param args
     * args[0]: application home path
     */
    public static void start(String[] args) {
        if (args != null && args.length > 0) {
            ServerEnvConstant.setAppServerHome(args[0]);
        }

        ApplicationBoot.setup();

        DeploymentOptions deploymentOptions = new DeploymentOptions();
        deploymentOptions.setConfig(JsonObject.mapFrom(AppServerConfigLoader.getServerCfg()));
        vertx = Vertx.vertx(new VertxOptions()
                .setMetricsOptions(new DropwizardMetricsOptions().setJmxEnabled(true)));
        vertx.deployVerticle(HttpServerVerticle.class, deploymentOptions, ar -> {
            if (ar.succeeded()) {
                verticleId = ar.result();
            } else {
                vertx.close();
                System.exit(-2);
            }
        });

        suspend();
    }

    private static void suspend() {
        try {
            while (true) {
                if(isStop.get()) {
                    break;
                }
                Thread.sleep(1000);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 停止服务
     *
     * @param args
     */
    public static void stop(String[] args) {
        vertx.undeploy(verticleId, ar -> {
            if (ar.succeeded()) {
                System.out.println("undeploy http verticle successfully");
                vertx.close(res -> {
                    if (res.succeeded()) {
                        System.out.println("stop vertx server successfully");
                    } else {
                        res.cause().printStackTrace();
                        System.out.println("stop vertx server failed");
                    }
                    isStop.set(true);
                });
            } else {
                ar.cause().printStackTrace();
                System.out.println("undeploy http verticle failed");
                vertx.close(res -> {
                    if (res.succeeded()) {
                        System.out.println("stop vertx server successfully");
                    } else {
                        res.cause().printStackTrace();
                        System.out.println("stop vertx server failed");
                    }
                    isStop.set(true);
                });
            }
        });
    }

}
