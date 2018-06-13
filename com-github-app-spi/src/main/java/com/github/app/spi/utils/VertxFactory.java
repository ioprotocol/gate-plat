package com.github.app.spi.utils;

import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import io.vertx.ext.dropwizard.DropwizardMetricsOptions;

public class VertxFactory {
    private static Vertx vertx;

    public static Vertx vertx() {
        if (vertx == null) {
            VertxOptions vertxOptions = new VertxOptions();
            vertxOptions.setMetricsOptions(new DropwizardMetricsOptions().setJmxEnabled(true));
            vertxOptions.setWorkerPoolSize(Runtime.getRuntime().availableProcessors() * 2);
            vertxOptions.setEventLoopPoolSize(Runtime.getRuntime().availableProcessors());
            vertx = Vertx.vertx(vertxOptions);
        }
        return vertx;
    }
}
