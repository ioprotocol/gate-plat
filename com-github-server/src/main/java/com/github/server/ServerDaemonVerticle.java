package com.github.server;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Future;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * 托管服务
 * 1. 网关服务
 * 2. 接口服务
 * 3. zk服务
 */
public class ServerDaemonVerticle extends AbstractVerticle {
    private Logger logger = LogManager.getLogger(ServerDaemonVerticle.class);

    @Override
    public void start(Future<Void> startFuture) throws Exception {
        vertx.executeBlocking(future -> {
            vertx.deployVerticle(ZookeeperServerVerticle.class, new DeploymentOptions().setConfig(config()), ar -> {
                if (ar.succeeded()) {
                    future.complete(ar.result());
                    logger.info("deploy zookeeper service success");
                } else {
                    future.fail(ar.cause());
                }
            });
        }, result -> {
            if (result.succeeded()) {
                startFuture.complete();
            } else {
                startFuture.fail(result.cause());
            }
        });
    }

    @Override
    public void stop(Future<Void> stopFuture) throws Exception {
        super.stop(stopFuture);
    }
}
