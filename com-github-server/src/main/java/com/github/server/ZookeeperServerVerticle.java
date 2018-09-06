package com.github.server;

import com.github.app.utils.ServerEnvConstant;
import com.github.zkbooter.ZooKeeperStarter;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ZookeeperServerVerticle extends AbstractVerticle {
    private Logger logger = LogManager.getLogger(ZookeeperServerVerticle.class);
    private Thread zkWorker = new Thread(new Runnable() {
        @Override
        public void run() {
            try {
                String[] args = new String[]{ServerEnvConstant.getZkConfigFilePath()};
                ZooKeeperStarter.main(args);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    });

    @Override
    public void start(Future<Void> startFuture) throws Exception {
        zkWorker.start();
        startFuture.complete();
        logger.info("ZookeeperServerVerticle start success");
    }

    @Override
    public void stop(Future<Void> stopFuture) throws Exception {
        zkWorker.interrupt();
        stopFuture.complete();
    }
}
