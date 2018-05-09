package com.github.app.api.handler.common;

import com.github.app.api.handler.JsonResponse;
import com.github.app.api.handler.UriHandler;
import com.github.app.utils.JacksonUtils;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

@Component
public class FailureHandler implements UriHandler {

    @Override
    public void registeUriHandler(Router router) {
        router.route("/*").produces(CONTENT_TYPE).failureHandler(this::failure);
    }

    public void failure(RoutingContext routingContext) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        if (routingContext.failure() != null) {
            routingContext.failure().printStackTrace();
            routingContext.failure().printStackTrace(new PrintStream(baos));
        }

        String exception = baos.toString();
        JsonResponse response = new JsonResponse(CODE_API_OPERATION_FAILED, exception);

        /**
         * 捕获handler的异常，打印错误日志
         */

        logger.error("\n{}:{} \nheader:{}\n params:{}\n body:{}\n response:{}\n\n",
                routingContext.request().method().name(),
                routingContext.request().absoluteURI(),
                JacksonUtils.serializePretty(routingContext.request().headers().entries()),
                JacksonUtils.serializePretty(routingContext.request().params()),
                routingContext.getBodyAsString(),
                JacksonUtils.serializePretty(response));

        routingContext.response().end(JacksonUtils.serialize(response));
    }
}
