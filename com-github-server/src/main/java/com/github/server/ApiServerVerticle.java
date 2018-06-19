package com.github.server;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.github.app.spi.config.AppServerConfig;
import com.github.app.spi.config.SpringApplication;
import com.github.app.spi.dao.domain.Popedom;
import com.github.app.spi.handler.AuthUriHandler;
import com.github.app.spi.handler.OpenUriHandler;
import com.github.app.spi.handler.UriHandler;
import com.github.app.spi.handler.common.FailureHandler;
import com.github.app.spi.handler.common.WelcomHandler;
import com.github.app.spi.handler.interceptor.AuthInterceptor;
import com.github.app.spi.plugin.ApiMetricTimeMeterHandler;
import com.github.app.spi.plugin.ValidatorInterceptor;
import com.github.app.spi.services.DBAutoInit;
import com.github.app.spi.utils.PopedomContext;
import com.github.app.utils.ServerEnvConstant;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerOptions;
import io.vertx.core.json.Json;
import io.vertx.core.net.JksOptions;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.*;
import io.vertx.ext.web.sstore.LocalSessionStore;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static com.github.app.spi.utils.SessionConstant.SESSION_STORE_MAP;

public class ApiServerVerticle extends AbstractVerticle {
    private Logger logger = LogManager.getLogger(ApiServerVerticle.class);

    private ApplicationContext applicationContext;
    private HttpServer server;
    private Router router;

    @Override
    public void start(Future<Void> startFuture) {

        Json.mapper.configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true).configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true).configure(JsonParser.Feature.ALLOW_UNQUOTED_CONTROL_CHARS, true).configure(SerializationFeature.WRITE_NULL_MAP_VALUES, false).configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false).configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false).setSerializationInclusion(JsonInclude.Include.NON_NULL);

        vertx.executeBlocking(future -> {
            applicationContext = new AnnotationConfigApplicationContext(SpringApplication.class);

            AppServerConfig serverConfig = config().mapTo(AppServerConfig.class);

            if (serverConfig.isDataSourceAutoInit()) {
                DBAutoInit dbAutoInit = applicationContext.getBean(DBAutoInit.class);
                try {
                    dbAutoInit.init();
                } catch (Exception e) {
                    e.printStackTrace();
                    startFuture.fail(e);
                    return;
                }
            }

            HttpServerOptions options = new HttpServerOptions().setSsl(false);
            if (serverConfig.isTlsEnabled()) {
                options.setSsl(true);
                options.setKeyCertOptions(new JksOptions().setPath(ServerEnvConstant.getAppServerCfgFilePath(serverConfig.getTlsCertificateFilePath()))
                        .setPassword(serverConfig.getTlsCertificatePassword()));
            } else {
                options.setSsl(false);
            }

            server = vertx.createHttpServer(options);

            router = Router.router(vertx);

            setupRouter(router);

            server.requestHandler(router::accept).listen(serverConfig.getApiPort(), ar -> {
                if (ar.succeeded()) {
                    logger.info("api server start successfully, bind port: " + server.actualPort());
                    future.complete();
                } else {
                    logger.error("api server start failed, reason:" + ar.cause().getLocalizedMessage());
                    future.fail(ar.cause());
                }
            });
        }, result -> {
            if (result.succeeded()) {
                startFuture.complete();
            } else {
                startFuture.fail(result.cause());
            }
        });
    }

    @Override
    public void stop(Future<Void> stopFuture) throws Exception {
        router.clear();
        server.close(future -> {
            if (future.succeeded()) {
                stopFuture.complete();
            }
        });
    }

    private void setupRouter(Router rootRouter) {
        rootRouter.route().handler(CorsHandler.create(".*").allowedMethod(HttpMethod.GET).allowedMethod(HttpMethod.POST).allowedMethod(HttpMethod.DELETE).allowedMethod(HttpMethod.PUT).allowedMethod(HttpMethod.OPTIONS).allowCredentials(true).allowedHeader("X-Token").allowedHeader("Cookie").allowedHeader("Accept").allowedHeader("Accept-Encoding").allowedHeader("Accept-Language").allowedHeader("Connection").allowedHeader("Host").allowedHeader("Referer").allowedHeader("Origin").allowedHeader("Content-Type"));

        CookieHandler cookieHandler = CookieHandler.create();
        SessionHandler sessionHandler = SessionHandler.create(LocalSessionStore.create(vertx, SESSION_STORE_MAP, 30 * 60 * 1000));

        rootRouter.route().pathRegex("^(?!/static).*").handler(cookieHandler);
        rootRouter.route().pathRegex("^(?!/static).*").handler(sessionHandler);

        rootRouter.route().handler(LoggerHandler.create());
        rootRouter.route().handler(TimeoutHandler.create(8000));
        rootRouter.route().pathRegex("^(?!/static).*").handler(ResponseTimeHandler.create());
        rootRouter.route().pathRegex("^(?!/static).*").handler(ApiMetricTimeMeterHandler.create());
        rootRouter.route().pathRegex("^(?!/static).*").handler(ResponseContentTypeHandler.create());
        rootRouter.route().pathRegex("^(?!/static).*").handler(BodyHandler.create());
        rootRouter.route().pathRegex("^(?!/static).*").handler(ValidatorInterceptor.create());

        loadHandlers(rootRouter, WelcomHandler.class);
        loadHandlers(rootRouter, FailureHandler.class);
        rootRouter.route("/static/*").blockingHandler(StaticHandler.create()
                .setAllowRootFileSystemAccess(true)
                .setDefaultContentEncoding("UTF-8")
                .setWebRoot(ServerEnvConstant.getAppServerHome() + File.separator + "web"), false);

        /**
         * apiRouter will validate the token in the global interceptor
         */
        Router apiRouter = Router.router(vertx);
        loadHandlers(apiRouter, AuthInterceptor.class);
        loadHandlers(apiRouter, AuthUriHandler.class);
        rootRouter.mountSubRouter("/api", apiRouter);

        /**
         * openRouter is fully opened api, have no interceptor to validate token and other user info
         */
        Router openRouter = Router.router(vertx);
        loadHandlers(openRouter, OpenUriHandler.class);
        rootRouter.mountSubRouter("/open", openRouter);
    }

    private void loadHandlers(Router router, Class<?> cls) {
        List<Popedom> popedoms = new ArrayList<>();

        String[] beanNames = applicationContext.getBeanNamesForType(cls);
        for (String bean : beanNames) {
            Object beanObj = applicationContext.getBean(bean);
            UriHandler uriHandler = (UriHandler) beanObj;
            uriHandler.registeUriHandler(router);
            uriHandler.registePopedom(popedoms);
        }

        PopedomContext.getInstance().addPopedoms(popedoms);
    }

    /**
     * 动态挂载handler
     */
    public void addRoute(UriHandler uriHandler) {
        uriHandler.registeUriHandler(router);
    }
}
