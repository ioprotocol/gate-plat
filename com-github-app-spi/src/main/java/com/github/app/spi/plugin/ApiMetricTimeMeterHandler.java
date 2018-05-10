package com.github.app.spi.plugin;

import io.vertx.core.Handler;
import io.vertx.ext.web.RoutingContext;

public interface ApiMetricTimeMeterHandler extends Handler<RoutingContext> {

    static ApiMetricTimeMeterHandler create() {
        return new ApiMetricTimeMeterHandlerImpl();
    }
}
