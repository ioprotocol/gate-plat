package com.github.app.api.plugin;

import io.vertx.ext.web.RoutingContext;

public class ApiMetricTimeMeterHandlerImpl implements ApiMetricTimeMeterHandler {
    @Override
    public void handle(RoutingContext ctx) {
        long start = System.nanoTime();
        ctx.addBodyEndHandler(v -> {
            long duration = System.nanoTime() - start;
            HandlerPerformanceMonitor.update(ctx, duration);
        });
        ctx.next();
    }
}
