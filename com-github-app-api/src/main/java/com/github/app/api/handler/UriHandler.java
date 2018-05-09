package com.github.app.api.handler;

import com.github.app.api.dao.domain.Popedom;
import com.github.app.utils.JacksonUtils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.List;

import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;

/**
 * json format { "code" : 0, "msg"  : "success", "data": {} }
 */
public interface UriHandler {

    Logger logger = LogManager.getLogger(UriHandler.class);

    String CONTENT_TYPE = "application/json;charset=UTF-8";

    /**
     * api operation success
     */
    int CODE_SUCCESS = 0;
    /**
     * JWT token illegal
     */
    int CODE_JWT_TOKEN_INVALIDATE = -1;
    /**
     * JWT token timeout
     */
    int CODE_JWT_TOKEN_TIMEOUT = -2;
    /**
     * JWT token not filled into http headers with name {X-Token}
     */
    int CODE_JWT_TOKEN_MISSING = -3;
    /**
     * This user have login
     */
    int CODE_USER_HAVE_LOGINED = -4;
    /**
     * api operation failed
     */
    int CODE_API_OPERATION_FAILED = -5;
    /**
     * api operation authentication failed
     */
    int CODE_API_AUTHENTICATION_FAILED = -6;


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
        JsonResponse response = new JsonResponse(code, msg, data);

        if (logger.isInfoEnabled()) {
            logger.info("\n{}:{} \nheader:{}\n params:{}\n body:{}\n response:{}\n\n",
                    routingContext.request().method().name(),
                    routingContext.request().absoluteURI(),
                    JacksonUtils.serializePretty(routingContext.request().headers().entries()),
                    JacksonUtils.serializePretty(routingContext.request().params().entries()),
                    routingContext.getBodyAsString(),
                    JacksonUtils.serializePretty(response));
        }

        if (data instanceof JsonObject || data instanceof JsonArray) {
            routingContext.response().end(JsonObject.mapFrom(response).toBuffer());
        } else {
            routingContext.response().end(JacksonUtils.serialize(response));
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
