package com.github.app.spi.plugin;

import io.vertx.core.MultiMap;
import io.vertx.core.buffer.Buffer;
import io.vertx.ext.web.RoutingContext;

import java.util.Iterator;
import java.util.Map;

public class ValidatorInterceptorImpl implements ValidatorInterceptor {
    @Override
    public void handle(RoutingContext routingContext) {
        //  http query params
        processSqlEscape(routingContext.request().params());
        //  http body forms
        processSqlEscape(routingContext.request().formAttributes());

        if(routingContext.getBody() != null) {
            routingContext.setBody(Buffer.buffer(ValidatorInterceptor.escape(routingContext.getBodyAsString())));
        }
        routingContext.next();
    }

    private void processSqlEscape(MultiMap multiMap) {
        Iterator<Map.Entry<String, String>> it = multiMap.iterator();
        while (it.hasNext()) {
            Map.Entry<String, String> entry = it.next();
            entry.setValue(ValidatorInterceptor.escape(entry.getValue()));
        }
    }
}
