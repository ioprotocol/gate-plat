package com.github.app.api.handler.open;

import java.io.ByteArrayOutputStream;
import java.io.File;

import com.github.app.api.config.AppServerConfig;
import com.github.app.utils.ServerEnvConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import com.github.app.api.dao.domain.Account;
import com.github.app.api.handler.UriHandler;
import com.github.app.api.services.AccountService;
import com.github.app.api.services.LogService;
import com.github.app.api.utils.CaptchaFactory;
import com.github.cage.Cage;
import com.github.cage.GCage;

import io.vertx.core.buffer.Buffer;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.auth.KeyStoreOptions;
import io.vertx.ext.auth.jwt.JWTAuth;
import io.vertx.ext.auth.jwt.JWTAuthOptions;
import io.vertx.ext.jwt.JWTOptions;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;

@Component
public class JWTIssueHandler implements UriHandler {
    private JWTAuthOptions config;

    @Autowired
    private AccountService accountService;
    @Autowired
    private LogService logService;

    private static final int CAPTCHA_LENGTH = 5;
    private static final int CAPTCHA_TYPE = 1;
    private static final String CAPTCHA_CODE = "captchaCode";

    private CaptchaFactory captchaFactory = new CaptchaFactory(CAPTCHA_TYPE, CAPTCHA_LENGTH);

    private Cage cage = new GCage();

    @Override
    public void registeUriHandler(Router router) {
        router.post().path("/auth").produces(CONTENT_TYPE).blockingHandler(this::issueJWTToken, false);
        router.get().path("/auth").blockingHandler(this::captchaCode, false);
    }

    public void captchaCode(RoutingContext routingContext) {
        routingContext.response().putHeader("Pragma", "No-cache");
        routingContext.response().putHeader("Cache-Control", "no-cache");
        routingContext.response().putHeader("Expires", "0");
        routingContext.response().putHeader("content-type", "image/jpeg");

        String captchaCode = captchaFactory.next();

        try {
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            cage.draw(captchaCode, os);

            Buffer buffer = Buffer.buffer();
            buffer.appendBytes(os.toByteArray());
            os.close();
            routingContext.session().put(CAPTCHA_CODE, captchaCode);
            routingContext.response().end(buffer);
        } catch (Exception e) {
            responseOperationFailed(routingContext, e);
        }
    }

    public void issueJWTToken(RoutingContext routingContext) {

        JsonObject jsonObject = new JsonObject(routingContext.getBodyAsString());

        String account = jsonObject.getString("account");
        String password = jsonObject.getString("password");
        String validateCode = jsonObject.getString("validateCode");

        if (StringUtils.isEmpty(validateCode)) {
            responseOperationFailed(routingContext, "验证码必须填写");
            return;
        }

        if (StringUtils.isEmpty(account)) {
            responseOperationFailed(routingContext, "帐号必须填写");
            return;
        }

        if (StringUtils.isEmpty(password)) {
            responseOperationFailed(routingContext, "密码必须填写");
            return;
        }

        if (!validateCode.equalsIgnoreCase(routingContext.session().get(CAPTCHA_CODE))) {
            responseOperationFailed(routingContext, "验证码输入有误");
            return;
        }

        routingContext.session().remove(CAPTCHA_CODE);

        if (config == null) {
            AppServerConfig sysConfig = routingContext.vertx().getOrCreateContext().config().mapTo(AppServerConfig.class);

            String path = ServerEnvConstant.getAppServerCfgFilePath(sysConfig.getJwtCertificateFilePath());
            logger.info("path:" + path);
            config = new JWTAuthOptions()
                    .setKeyStore(new KeyStoreOptions()
                            .setPath(path)
                            .setPassword(sysConfig.getJwtCertificatePassword()));
        }

        Account acc = accountService.authLogin(account, password);
        if (!ObjectUtils.isEmpty(acc)) {
            JWTAuth provider = JWTAuth.create(routingContext.vertx(), config);
            String token = provider.generateToken(new JsonObject().put("account", account),
                    new JWTOptions().setExpiresInMinutes(60 * 3));
            logService.addLog(account, "登录成功", "[Y]-->token:" + token + "");
            responseSuccess(routingContext, "", token);
        } else {
            logService.addLog(account, "登录失败", String.format("帐号:%s,密码:%s,IP:", account, password, routingContext.request().remoteAddress().toString()));
            responseOperationFailed(routingContext, "账号密码错误或账号已关闭");
        }
    }
}
