package com.github.app.spi.plugin;

import io.vertx.core.Handler;
import io.vertx.ext.web.RoutingContext;
import org.apache.commons.lang.StringEscapeUtils;

public interface ValidatorInterceptor extends Handler<RoutingContext> {
    static ValidatorInterceptor create() {
        return new ValidatorInterceptorImpl();
    }

    static String escape(String value ) {
        return StringEscapeUtils.escapeSql(value);
    }
}
