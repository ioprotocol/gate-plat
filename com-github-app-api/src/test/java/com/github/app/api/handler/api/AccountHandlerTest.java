package com.github.app.api.handler.api;

import io.vertx.core.json.JsonObject;
import io.vertx.ext.unit.Async;
import io.vertx.ext.unit.TestContext;
import org.junit.Test;

public class AccountHandlerTest extends BaseRestClientTest{

//    @Test
    public void testCurrentLogin(TestContext context) {
        Async async = context.async();
        get("/api/account", rep -> {
            JsonObject jsonObject = rep.result().bodyAsJsonObject();
            System.out.println(jsonObject.toString());
            context.assertEquals(jsonObject.getInteger("code"), 0);
            async.complete();
        });
    }

    @Test
    public void testAuthFilter(TestContext context) {
        Async async = context.async();
        post("/api/log",new JsonObject(), rep -> {
            JsonObject jsonObject = rep.result().bodyAsJsonObject();
            System.out.println(jsonObject.toString());
            context.assertEquals(jsonObject.getInteger("code"), 0);
            async.complete();
        });
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Async async1 = context.async();
        put("/api/log",new JsonObject(), rep -> {
            JsonObject jsonObject = rep.result().bodyAsJsonObject();
            System.out.println(jsonObject.toString());
            context.assertEquals(jsonObject.getInteger("code"), 0);
            async1.complete();
        });
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Async async2 = context.async();
        get("/api/log?par1=2&par2=3", rep -> {
            JsonObject jsonObject = rep.result().bodyAsJsonObject();
            System.out.println(jsonObject.toString());
            context.assertEquals(jsonObject.getInteger("code"), 0);
            async2.complete();
        });
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Async async3 = context.async();
        get("/api/log/123", rep -> {
            JsonObject jsonObject = rep.result().bodyAsJsonObject();
            System.out.println(jsonObject.toString());
            context.assertEquals(jsonObject.getInteger("code"), 0);
            async3.complete();
        });
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Async async4 = context.async();
        get("/api/log/pwd/test", rep -> {
            JsonObject jsonObject = rep.result().bodyAsJsonObject();
            System.out.println(jsonObject.toString());
            context.assertEquals(jsonObject.getInteger("code"), 0);
            async4.complete();
        });
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Async async5 = context.async();
        delete("/api/log/123", new JsonObject(), rep -> {
            JsonObject jsonObject = rep.result().bodyAsJsonObject();
            System.out.println(jsonObject.toString());
            context.assertEquals(jsonObject.getInteger("code"), 0);
            async5.complete();
        });
    }
}