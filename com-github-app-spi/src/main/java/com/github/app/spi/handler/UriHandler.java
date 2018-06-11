package com.github.app.spi.handler;

import com.github.app.spi.dao.domain.Popedom;
import com.github.app.utils.JacksonUtils;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.PooledByteBufAllocator;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.List;

import static com.github.app.spi.handler.JsonResponse.*;

/**
 * json format { "code" : 0, "msg"  : "success", "data": {} }
 */
public interface UriHandler {

    Logger logger = LogManager.getLogger(UriHandler.class);

    String CONTENT_TYPE = "application/json;charset=UTF-8";


    /**
     * @param router
     */
    void registeUriHandler(Router router);

    /**
     * 注册系统权限信息
     */
    default void registePopedom(List<Popedom> list) {
    }

    /**
     * @param routingContext
     * @param code
     * @param msg
     */
    default void response(RoutingContext routingContext, Integer code, String msg) {
        response(routingContext, code, msg, null);
    }

    /**
     * @param routingContext
     * @param code
     * @param data
     */
    default void response(RoutingContext routingContext, Integer code, Object data) {
        response(routingContext, code, null, data);
    }

    /**
     * @param routingContext
     * @param code
     * @param msg
     * @param data
     */
    default void response(RoutingContext routingContext, Integer code, String msg, Object data) {
        JsonResponse response = JsonResponse.create(code, msg, data);

        ByteBuf byteBuf = PooledByteBufAllocator.DEFAULT.buffer(512);
        try {
            JacksonUtils.serialize(response, byteBuf);
            if (logger.isInfoEnabled()) {
                logger.info("\n{}:{} \nheader:{}\n params:{}\n body:{}\n response:{}\n\n",
                        routingContext.request().method().name(),
                        routingContext.request().absoluteURI(),
                        JacksonUtils.serializePretty(routingContext.request().headers().entries()),
                        JacksonUtils.serializePretty(routingContext.request().params().entries()),
                        routingContext.getBodyAsString(),
                        JacksonUtils.serializePretty(response));
            }

            routingContext.response().end(Buffer.buffer(byteBuf));
        } finally {
            response.recycle();
        }
    }

    /**
     * 权限认证失败
     */
    default void responseOperationAuthFailed(RoutingContext routingContext, String msg) {
        response(routingContext, CODE_API_AUTHENTICATION_FAILED, msg);
    }

    /**
     * @param routingContext
     * @param msg
     */
    default void responseOperationFailed(RoutingContext routingContext, String msg) {
        response(routingContext, CODE_API_OPERATION_FAILED, msg);
    }

    /**
     * @param routingContext
     */
    default void responseOperationFailed(RoutingContext routingContext) {
        if (routingContext.failed()) {
            responseOperationFailed(routingContext, routingContext.failure());
        } else {
            responseOperationFailed(routingContext, "unknow server error");
        }
    }

    /**
     * @param routingContext
     * @param e
     */
    default void responseOperationFailed(RoutingContext routingContext, Exception e) {
        responseOperationFailed(routingContext, e.getCause());
    }

    /**
     * @param routingContext
     * @param throwable
     */
    default void responseOperationFailed(RoutingContext routingContext, Throwable throwable) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        throwable.printStackTrace(new PrintStream(baos));
        String exception = baos.toString();
        responseOperationFailed(routingContext, exception);
    }

    /**
     * @param routingContext
     */
    default void responseSuccess(RoutingContext routingContext) {
        responseSuccess(routingContext, "success");
    }

    /**
     * @param routingContext
     * @param msg
     */
    default void responseSuccess(RoutingContext routingContext, String msg) {
        responseSuccess(routingContext, msg, null);
    }

    /**
     * @param routingContext
     * @param data
     */
    default void responseSuccess(RoutingContext routingContext, Object data) {
        responseSuccess(routingContext, null, data);
    }

    default void responseSuccess(RoutingContext routingContext, JsonObject data) {
        responseSuccess(routingContext, null, data);
    }

    default void responseSuccess(RoutingContext routingContext, JsonArray data) {
        responseSuccess(routingContext, null, data);
    }

    /**
     * @param routingContext
     * @param msg
     * @param data
     */
    default void responseSuccess(RoutingContext routingContext, String msg, Object data) {
        response(routingContext, CODE_SUCCESS, msg, data);
    }

}
