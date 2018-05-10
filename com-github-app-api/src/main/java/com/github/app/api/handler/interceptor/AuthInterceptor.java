package com.github.app.api.handler.interceptor;

import com.github.app.api.config.AppServerConfig;
import com.github.app.api.dao.domain.Account;
import com.github.app.api.dao.domain.Popedom;
import com.github.app.api.handler.UriHandler;
import com.github.app.api.services.AccountService;
import com.github.app.api.services.LogService;
import com.github.app.api.services.RolePodomService;
import com.github.app.api.utils.AppServerConfigLoader;
import com.github.app.api.utils.AuthUtils;
import com.github.app.api.utils.PopedomContext;
import com.github.app.api.utils.SessionConstant;
import com.github.app.utils.ServerEnvConstant;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.auth.KeyStoreOptions;
import io.vertx.ext.auth.User;
import io.vertx.ext.auth.jwt.JWTAuth;
import io.vertx.ext.auth.jwt.JWTAuthOptions;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
public class AuthInterceptor implements UriHandler {
    private static JWTAuthOptions jwtAuthOptions;

    static {
        AppServerConfig sysConfig = AppServerConfigLoader.getServerCfg();
        jwtAuthOptions = new JWTAuthOptions().setKeyStore(new KeyStoreOptions().setPath(ServerEnvConstant.getAppServerCfgFilePath(sysConfig.getJwtCertificateFilePath())).setPassword(sysConfig.getJwtCertificatePassword()));
    }

    private JWTAuth jwtAuth;

    @Autowired
    private AccountService accountService;
    @Autowired
    private LogService logService;
    @Autowired
    private RolePodomService rolePodomService;

    @Override
    public void registeUriHandler(Router router) {
        router.route().path("/*").order(0).blockingHandler(this::loginAuthentication, false);
        router.route().path("/*").order(1).blockingHandler(this::operationAuthentication, false);
    }

    /**
     * 身份验证拦截器 验证服务调用者是否已登录系统
     */
    public void loginAuthentication(RoutingContext routingContext) {
        try {
            String token = routingContext.request().getHeader("X-Token");

            if (StringUtils.isEmpty(token)) {
                response(routingContext, CODE_JWT_TOKEN_MISSING, "token must be filled");
                return;
            }

            if (jwtAuth == null) {
                jwtAuth = JWTAuth.create(routingContext.vertx(), jwtAuthOptions);
            }

            jwtAuth.authenticate(new JsonObject().put("jwt", token), res -> {
                if (res.succeeded()) {
                    User theUser = res.result();
                    String account = theUser.principal().getString("account");

                    // 验证帐号是否被停用或者删除，如果停用或者删除，立即阻止此帐号的所有操作
                    Account acc = accountService.getAccountByAccountOrMobileOrEmail(account, null, null);
                    if (acc == null || acc.getIsEnable() == 0) {
                        response(routingContext, CODE_JWT_TOKEN_INVALIDATE, "帐号已停用");
                        return;
                    }

                    /**
                     * 登记session 信息
                     */
                    if (StringUtils.isEmpty(routingContext.session().get(SessionConstant.SESSION_ACCOUNT))) {
                        routingContext.session().put(SessionConstant.SESSION_ACCOUNT, account);
                        routingContext.session().put(SessionConstant.SESSION_ROLE_NAME, acc.getRole().getName());
                        routingContext.session().put(SessionConstant.SESSION_FIRST_ACTIVE_TIME, System.currentTimeMillis());
                        routingContext.session().put(SessionConstant.SESSION_IP_ADDRESS, routingContext.request().remoteAddress().toString());
                    }

                    routingContext.put("account", account);
                    routingContext.next();
                } else {
                    res.cause().printStackTrace();
                    response(routingContext, CODE_JWT_TOKEN_INVALIDATE, "token失效请重新登录");
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            responseOperationFailed(routingContext, e);
        }
    }

    /**
     * 操作拦截器 验证服务调用者是否有权限操作资源
     */
    public void operationAuthentication(RoutingContext routingContext) {
        String accountStr = routingContext.get("account");

        Account account = accountService.getAccountByAccountOrMobileOrEmail(accountStr, null, null);
        String remark = AuthUtils.buildRequestInfo(routingContext);
        Popedom popedom = PopedomContext.getInstance().match(routingContext);

        // 接口未设置权限信息或者登录账号为超级管理员，直接放行
        if (popedom == null || account.getRoleId() == 1) {
            String code = AuthUtils.authCode(routingContext);
            if (popedom != null) {
                code = popedom.getName();
            }
            logService.addLog(account, code, "[Y]" + remark);
            routingContext.next();
            return;
        }

        if (rolePodomService.isAuthOperation(popedom.getCode(), account.getRoleId())) {
            logService.addLog(account, popedom.getName(), "[Y]" + remark);
            routingContext.next();
        } else {
            logService.addLog(account, popedom.getName(), "[N]" + remark);
            responseOperationAuthFailed(routingContext, "你没有权限进行此操作");
        }
    }

}
