package com.github.app.api.dao.domain;

import io.vertx.core.json.JsonObject;
import org.junit.Test;

public class AccountTest {

    public static Account create() {
        Account account = new Account();
        account.setAccount("test");
        account.setPassword("test");
        account.setRoleId(1);
        account.setName("测试书记局");
        account.setSex("男");
        account.setEmail("test@163.com");
        account.setMobile("test15315086265");
        account.setIsEnable((short) 1);
        account.setRole(new Role());
        account.getRole().setRoleId(1);
        account.getRole().setName("admin");
        return account;
    }

    @Test
    public void test() {
        JsonObject jsonObject = JsonObject.mapFrom(create());
        System.out.println(jsonObject.toString());
    }
}