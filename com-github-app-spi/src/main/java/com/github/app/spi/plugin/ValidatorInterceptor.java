package com.github.app.spi.plugin;

import io.vertx.core.Handler;
import io.vertx.ext.web.RoutingContext;

public interface ValidatorInterceptor extends Handler<RoutingContext> {
    static ValidatorInterceptor create() {
        return new ValidatorInterceptorImpl();
    }
}
