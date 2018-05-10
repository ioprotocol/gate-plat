package com.github.app.spi.handler.common;

import com.github.app.spi.handler.UriHandler;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import org.springframework.stereotype.Component;

@Component
public class WelcomHandler implements UriHandler {

    @Override
    public void registeUriHandler(Router router) {
        router.route("/").produces(CONTENT_TYPE).blockingHandler(this::welcome, false);
    }

    public void welcome(RoutingContext routingContext) {
        responseSuccess(routingContext, "welcome to app service api, version v1.0.0");
    }

}
