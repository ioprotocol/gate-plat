package com.github.app.api.dao.domain;

public class LogTest {

    public static Log create() {
        Log log = new Log();
        log.setLogId(System.currentTimeMillis());
        log.setCode("code" + System.currentTimeMillis());
        log.setDescription("desc");
        log.setOpAccount("admin");
        log.setOpName("admin");
        log.setRoleId(1);
        log.setRoleName("admin");
        return log;
    }
}