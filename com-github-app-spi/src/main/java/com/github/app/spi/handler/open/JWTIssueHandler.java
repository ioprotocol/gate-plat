package com.github.app.spi.handler.open;

import com.github.app.spi.config.AppServerConfig;
import com.github.app.spi.dao.domain.Account;
import com.github.app.spi.handler.OpenUriHandler;
import com.github.app.spi.services.AccountService;
import com.github.app.spi.services.LogService;
import com.github.app.spi.utils.AppServerConfigLoader;
import com.github.app.spi.utils.CaptchaFactory;
import com.github.app.utils.ServerEnvConstant;
import com.github.cage.Cage;
import com.github.cage.GCage;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.util.concurrent.RateLimiter;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufOutputStream;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.util.ReferenceCountUtil;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.auth.KeyStoreOptions;
import io.vertx.ext.auth.jwt.JWTAuth;
import io.vertx.ext.auth.jwt.JWTAuthOptions;
import io.vertx.ext.jwt.JWTOptions;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.util.concurrent.TimeUnit;

@Component
public class JWTIssueHandler implements OpenUriHandler {
    private JWTAuthOptions config;

    @Autowired
    private AccountService accountService;
    @Autowired
    private LogService logService;

    private static final String CAPTCHA_CODE = "captchaCode";

    private static final CaptchaFactory captchaFactory;

    private Cage cage = new GCage();
    private LoadingCache<String, RateLimiter> qpsLimiterCache = CacheBuilder.newBuilder()
            .expireAfterAccess(5*60, TimeUnit.SECONDS)
            .maximumSize(20000)
            .build(new CacheLoader<String, RateLimiter>() {
                @Override
                public RateLimiter load(String key) throws Exception {
                    return RateLimiter.create(10);
                }
            });

    static {
        AppServerConfig sysConfig = AppServerConfigLoader.getServerCfg();
        captchaFactory = new CaptchaFactory(CaptchaFactory.CaptchaModel.valueOf(sysConfig.getCaptchaModel().toUpperCase()), sysConfig.getCaptchaLength());
    }

    @Override
    public void registeUriHandler(Router router) {
        router.post().path("/auth").produces(CONTENT_TYPE).blockingHandler(this::issueJWTToken, false);
        router.get().path("/auth").blockingHandler(this::captchaCode, false);
        router.get().path("/captcha").blockingHandler(this::getCaptchaInfo, false);
    }

    public void getCaptchaInfo(RoutingContext routingContext) {
        AppServerConfig sysConfig = AppServerConfigLoader.getServerCfg();
        JsonObject captchaInfo = new JsonObject();
        captchaInfo.put("captchaEnabled", sysConfig.isCaptchaEnabled());
        captchaInfo.put("captchaModel", sysConfig.getCaptchaModel());
        captchaInfo.put("captchaLength", sysConfig.getCaptchaLength());
        responseSuccess(routingContext, captchaInfo);
    }

    public void captchaCode(RoutingContext routingContext) {
        routingContext.response().putHeader("Pragma", "No-cache");
        routingContext.response().putHeader("Cache-Control", "no-cache");
        routingContext.response().putHeader("Expires", "0");
        routingContext.response().putHeader("content-type", "image/jpeg");

        String captchaCode = captchaFactory.next();

        try {
            ByteBuf byteBuf = PooledByteBufAllocator.DEFAULT.buffer(4096);
            cage.draw(captchaCode, new ByteBufOutputStream(byteBuf));
            routingContext.session().put(CAPTCHA_CODE, captchaCode);
            routingContext.response().end(Buffer.buffer(byteBuf));
            routingContext.response().bodyEndHandler(v -> ReferenceCountUtil.safeRelease(byteBuf));
        } catch (Exception e) {
            responseOperationFailed(routingContext, e);
        }
    }

    public void issueJWTToken(RoutingContext routingContext) {

        String remoteAddress = routingContext.request().remoteAddress().toString();
        try {
            if (!qpsLimiterCache.get(remoteAddress).tryAcquire()) {
                responseOperationFailed(routingContext, "访问过于频繁");
                return;
            }
        } catch (Exception e) {
            responseOperationFailed(routingContext, e);
            return;
        }

        JsonObject jsonObject = mapTo(routingContext, JsonObject.class);

        String account = jsonObject.getString("account");
        String password = jsonObject.getString("password");
        String validateCode = jsonObject.getString("validateCode");

        AppServerConfig sysConfig = AppServerConfigLoader.getServerCfg();

        if (sysConfig.isCaptchaEnabled()) {
            if (StringUtils.isEmpty(validateCode)) {
                responseOperationFailed(routingContext, "验证码必须填写");
                return;
            }
        }

        if (StringUtils.isEmpty(account)) {
            responseOperationFailed(routingContext, "帐号必须填写");
            return;
        }

        if (StringUtils.isEmpty(password)) {
            responseOperationFailed(routingContext, "密码必须填写");
            return;
        }

        if (sysConfig.isCaptchaEnabled()) {
            if (!validateCode.equalsIgnoreCase(routingContext.session().get(CAPTCHA_CODE))) {
                responseOperationFailed(routingContext, "验证码输入有误");
                return;
            }

            routingContext.session().remove(CAPTCHA_CODE);
        }

        if (config == null) {
            String path = ServerEnvConstant.getAppServerCfgFilePath(sysConfig.getJwtCertificateFilePath());
            config = new JWTAuthOptions()
                    .setKeyStore(new KeyStoreOptions()
                            .setPath(path)
                            .setPassword(sysConfig.getJwtCertificatePassword()));
        }

        Account acc = accountService.authLogin(account, password);
        if (!ObjectUtils.isEmpty(acc)) {
            JWTAuth provider = JWTAuth.create(routingContext.vertx(), config);
            String token = provider.generateToken(new JsonObject().put("account", account),
                    new JWTOptions().setExpiresInMinutes(sysConfig.getJwtTokenExpiresInMinutes()));
            logService.addLog(account, "登录成功", "[Y]-->token:" + token + "");
            responseSuccess(routingContext, "", token);
        } else {
            logService.addLog(account, "登录失败", String.format("帐号:%s,密码:%s,IP:", account, password, routingContext.request().remoteAddress().toString()));
            responseOperationFailed(routingContext, "账号密码错误或账号已关闭");
        }
    }
}
