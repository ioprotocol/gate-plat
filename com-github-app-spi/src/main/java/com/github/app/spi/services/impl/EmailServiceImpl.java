package com.github.app.spi.services.impl;

import com.github.app.spi.services.EmailService;
import com.github.app.spi.utils.AppServerConfigLoader;
import com.github.app.spi.utils.VertxFactory;
import io.vertx.core.WorkerExecutor;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.mail.*;
import org.apache.commons.mail.resolver.DataSourceUrlResolver;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.CompletableFuture;

@Component
@Lazy
public class EmailServiceImpl implements EmailService {
    private static final int EMAIL_TIMEOUT = 12000;
    private WorkerExecutor executor;

    @PostConstruct
    public void init() {
        executor = VertxFactory.vertx().createSharedWorkerExecutor("email-woker", Runtime.getRuntime().availableProcessors(), EMAIL_TIMEOUT);
    }

    @PreDestroy
    public void destroy() {
        if (executor != null) {
            executor.close();
            executor = null;
        }
    }

    private Email buildBaseEmailInfo(Email email) throws EmailException {
        email.setHostName(AppServerConfigLoader.getServerCfg().getEmailHostname());
        email.setSmtpPort(AppServerConfigLoader.getServerCfg().getEmailSmtpPort());
        email.setAuthenticator(new DefaultAuthenticator(AppServerConfigLoader.getServerCfg().getEmailUserName(),
                AppServerConfigLoader.getServerCfg().getEmailPassword()));
        email.setSSLOnConnect(AppServerConfigLoader.getServerCfg().isEmailSslOnConnect());
        if (StringUtils.isEmpty(AppServerConfigLoader.getServerCfg().getEmailFromName())) {
            email.setFrom(AppServerConfigLoader.getServerCfg().getEmailFrom());
        } else {
            email.setFrom(AppServerConfigLoader.getServerCfg().getEmailFrom(),
                    AppServerConfigLoader.getServerCfg().getEmailFromName());
        }
        return email;
    }

    @Override
    public CompletableFuture<Void> sendEmail(String subject, String message, String... toEmailAddress) {
        CompletableFuture<Void> resultFuture = new CompletableFuture<>();

        executor.executeBlocking(future -> {
            try {
                Email email = buildBaseEmailInfo(new SimpleEmail());
                email.setSubject(subject);
                email.setMsg(message);
                email.addTo(toEmailAddress);
                email.send();
                future.complete();
            } catch (Exception e) {
                future.fail(e);
            }
        }, true, result -> {
            if (result.succeeded()) {
                resultFuture.complete(null);
            } else {
                resultFuture.completeExceptionally(result.cause());
            }
        });

        return resultFuture;
    }

    @Override
    public CompletableFuture<Void> sendEmail(String subject, String message, EmailAttachment attachment, String... toEmailAddress) {
        CompletableFuture<Void> resultFuture = new CompletableFuture<>();

        executor.executeBlocking(future -> {
            try {
                MultiPartEmail email = new MultiPartEmail();
                buildBaseEmailInfo(email);
                email.setSubject(subject);
                email.setMsg(message);
                email.addTo(toEmailAddress);
                email.attach(attachment);
                email.send();
                future.complete();
            } catch (Exception e) {
                future.fail(e);
            }
        }, true, result -> {
            if (result.succeeded()) {
                resultFuture.complete(null);
            } else {
                resultFuture.completeExceptionally(result.cause());
            }
        });

        return resultFuture;
    }

    @Override
    public CompletableFuture<Void> sendEmail(String subject, String html, URL datasourceResolver, String... toEmailAddress) {
        CompletableFuture<Void> resultFuture = new CompletableFuture<>();

        executor.executeBlocking(future -> {
            try {
                ImageHtmlEmail email = new ImageHtmlEmail();
                buildBaseEmailInfo(email);
                email.setSubject(subject);
                email.setHtmlMsg(html);
                email.addTo(toEmailAddress);
                email.setDataSourceResolver(new DataSourceUrlResolver(datasourceResolver));
                email.setTextMsg(html);
                email.send();
                future.complete();
            } catch (Exception e) {
                future.fail(e);
            }
        }, true, result -> {
            if (result.succeeded()) {
                resultFuture.complete(null);
            } else {
                resultFuture.completeExceptionally(result.cause());
            }
        });

        return resultFuture;
    }

    public static void main(String[] args) throws MalformedURLException {
        String html = ".... <img src=\"http://www.apache.org/images/feather.gif\"> ....";
        EmailServiceImpl emailService = new EmailServiceImpl();
        emailService.init();
        emailService.sendEmail("Email for you", html, new URL("http://www.apache.org"), "xushuyang@foton.com.cn")
                .thenRun(() -> System.out.println("finish")).exceptionally(exp -> {
            exp.printStackTrace();
            return null;
        });
    }
}
