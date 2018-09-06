package com.github.server;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;

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
        startFuture.complete();
        logger.info("ServerDaemonVerticle start success");
    }

    @Override
    public void stop(Future<Void> stopFuture) throws Exception {
        stopFuture.complete();
    }
}
