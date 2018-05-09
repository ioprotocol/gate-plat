package com.github.app.api;

import io.vertx.core.Vertx;
import io.vertx.ext.mail.MailClient;
import io.vertx.ext.mail.MailConfig;
import io.vertx.ext.mail.MailMessage;
import io.vertx.ext.mail.StartTLSOptions;

public class EmailTest {

    public static void main(String[] args) throws InterruptedException {
        Vertx vertx = Vertx.vertx();

        MailConfig config = new MailConfig();
        config.setHostname("smtp.163.com");
        config.setPort(587);
        config.setStarttls(StartTLSOptions.REQUIRED);
        config.setUsername("xsy870712@163.com");
        config.setPassword("xsyxsy870712");
        MailClient mailClient = MailClient.createShared(vertx, config, "mail-client");

        MailMessage message = new MailMessage();
        message.setFrom("xsy870712@163.com");
        message.setTo("xushuyang@foton.com.cn");
        message.setCc("Another User <another@example.net>");
        message.setText("this is the plain message text");
        message.setHtml("this is html text <a href=\"http://vertx.io\">vertx.io</a>");

        mailClient.sendMail(message, hd -> {
            if (hd.succeeded()) {
                System.out.println("send success");
            } else {
                hd.cause().printStackTrace();
            }
        });

//        Thread.sleep(30 * 1000);
    }
}
