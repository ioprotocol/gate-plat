package com.github.app.api.handler.api;

import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.unit.Async;
import io.vertx.ext.unit.TestContext;
import io.vertx.ext.unit.junit.RunTestOnContext;
import io.vertx.ext.unit.junit.VertxUnitRunner;
import io.vertx.ext.web.client.HttpResponse;
import io.vertx.ext.web.client.WebClient;
import org.junit.Before;
import org.junit.Rule;
import org.junit.runner.RunWith;

@RunWith(VertxUnitRunner.class)
public class BaseRestClientTest {
    protected WebClient client;
    protected String token;
    protected String host = "127.0.0.1";
    protected int port = 8080;

    @Rule
    public RunTestOnContext rule = new RunTestOnContext();

    @Before
    public void setup(TestContext context) {
        client = WebClient.create(rule.vertx());

        Async async = context.async();
        client.post(port, host, "/open/auth")
                .sendJsonObject(new JsonObject()
                                .put("account", "administrator")
                                .put("password", "123456"),
                        ar -> {
                            if (ar.succeeded()) {
                                token = ar.result().bodyAsJsonObject().getString("data");
                            } else {
                                context.assertFalse(false, "token can't catch");
                            }
                            async.complete();
                        });
    }

    protected void post(String uri, JsonObject jsonObject, Handler<AsyncResult<HttpResponse<Buffer>>> handler) {
        client.post(port, host, uri)
                .putHeader("Content-type", "application/json;charset=UTF-8")
                .putHeader("X-Token", token)
                .timeout(8*1000)
                .sendJsonObject(jsonObject, handler);
    }

    protected void get(String uri, Handler<AsyncResult<HttpResponse<Buffer>>> handler) {
        client.get(port, host, uri)
                .putHeader("Content-type", "application/json;charset=UTF-8")
                .putHeader("X-Token", token)
                .timeout(8*1000)
                .send(handler);
    }

    protected void put(String uri, JsonObject jsonObject, Handler<AsyncResult<HttpResponse<Buffer>>> handler) {
        client.put(port, host, uri)
                .putHeader("Content-type", "application/json;charset=UTF-8")
                .putHeader("X-Token", token)
                .timeout(8*1000)
                .sendJsonObject(jsonObject, handler);
    }

    protected void delete(String uri, JsonObject jsonObject, Handler<AsyncResult<HttpResponse<Buffer>>> handler) {
        client.delete(port, host, uri)
                .putHeader("Content-type", "application/json;charset=UTF-8")
                .putHeader("X-Token", token)
                .timeout(8*1000)
                .sendJsonObject(jsonObject, handler);
    }
}
