package com.github.app.api.plugin;

import io.vertx.core.Handler;
import io.vertx.ext.web.RoutingContext;

public interface ApiMetricTimeMeterHandler extends Handler<RoutingContext> {

    static ApiMetricTimeMeterHandler create() {
        return new ApiMetricTimeMeterHandlerImpl();
    }
}
