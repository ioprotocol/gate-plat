package com.github.app.spi.handler.api;

import com.github.app.spi.dao.domain.Account;
import com.github.app.spi.dao.domain.Popedom;
import com.github.app.spi.handler.AuthUriHandler;
import com.github.app.spi.services.AccountService;
import com.github.app.spi.services.RolePodomService;
import com.github.app.spi.utils.RequestUtils;
import com.github.app.spi.utils.SessionConstant;
import com.github.app.utils.MD5Utils;
import io.vertx.core.MultiMap;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.core.shareddata.LocalMap;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.util.*;

@Component
public class AccountHandler implements AuthUriHandler {

    @Autowired
    private AccountService accountService;
    @Autowired
    private RolePodomService rolePodomService;

    @Override
    public void registeUriHandler(Router router) {
        router.post().path("/account").produces(CONTENT_TYPE).blockingHandler(this::save, false);
        router.delete().path("/account/:accountId").produces(CONTENT_TYPE).blockingHandler(this::delete, false);
        router.put().path("/account").produces(CONTENT_TYPE).blockingHandler(this::update, false);
        router.put().path("/account/enable").produces(CONTENT_TYPE).blockingHandler(this::enable, false);
        router.get().path("/account/:accountId").produces(CONTENT_TYPE).blockingHandler(this::queryOne, false);
        router.get().path("/account").produces(CONTENT_TYPE).blockingHandler(this::query, false);
        router.get().path("/account/current/login").produces(CONTENT_TYPE).blockingHandler(this::currentLogin, false);
        router.put().path("/account/resetpwd/:accountId").produces(CONTENT_TYPE).blockingHandler(this::resetPassword, false);
        router.put().path("/account/editpwd").produces(CONTENT_TYPE).blockingHandler(this::editPassword, false);
        router.put().path("/account/logout").produces(CONTENT_TYPE).blockingHandler(this::logOut, false);
        router.get().path("/account/online/session").produces(CONTENT_TYPE).blockingHandler(this::listSession, false);
    }

    @Override
    public void registePopedom(List<Popedom> list) {
        list.add(new Popedom.Builder().name("帐号添加").code("/.+/account/" + HttpMethod.POST.name()).remark("管理员的功能，可以添加各种角色的帐号").build());
        list.add(new Popedom.Builder().name("账号删除").code("/.+/account/.+/" + HttpMethod.DELETE.name()).remark("管理员的功能，可以删除系统中的所有帐号").build());
        list.add(new Popedom.Builder().name("账号修改").code("/.+/account/" + HttpMethod.PUT.name()).remark("基本功能，修改帐号的基本信息，如姓名、电话、邮箱等").build());
        list.add(new Popedom.Builder().name("账号停启用").code("/.+/account/enable/" + HttpMethod.PUT.name()).remark("管理员的功能，可以停用或者启用某个帐号").build());
        list.add(new Popedom.Builder().name("帐号查询单个").code("/.+/account/.+/" + HttpMethod.GET.name()).remark("管理员的功能，查询特定帐号的信息").build());
        list.add(new Popedom.Builder().name("帐号查询所有").code("/.+/account/" + HttpMethod.GET.name()).remark("管理员的功能，查询系统中的所有帐号信息").build());
        list.add(new Popedom.Builder().name("当前登录账号").code("/.+/account/current/login/" + HttpMethod.GET.name()).remark("基本功能，查询当前登录帐号的基本信息").build());
        list.add(new Popedom.Builder().name("帐号密码重置").code("/.+/account/resetpwd/.+/" + HttpMethod.PUT.name()).remark("管理员的功能，某个帐号密码忘记后，可以通过此接口对密码进行重置").build());
        list.add(new Popedom.Builder().name("修改帐号密码").code("/.+/account/editpwd/" + HttpMethod.PUT.name()).remark("基本功能，自助修改自己帐号的密码").build());
        list.add(new Popedom.Builder().name("退出帐号登录").code("/.+/account/logout/" + HttpMethod.PUT.name()).remark("基本功能，清除服务器session信息，并退出登录").build());
        list.add(new Popedom.Builder().name("在线用户").code("/.+/account/online/session/" + HttpMethod.GET.name()).remark("查询系统当前活跃用户").build());
    }

    public void enable(RoutingContext routingContext) {
        Account account = routingContext.getBodyAsJson().mapTo(Account.class);

        if (account.getAccountId() == 1) {
            responseOperationFailed(routingContext, "超级管理员帐号不允许禁用");
            return;
        }

        accountService.changeAccountStatus(account.getAccountId());
        responseSuccess(routingContext);
    }

    public void save(RoutingContext routingContext) {
        Account account = routingContext.getBodyAsJson().mapTo(Account.class);

        if (StringUtils.isEmpty(account.getAccount())) {
            responseOperationFailed(routingContext, "帐号不能为空");
            return;
        }

        if (ObjectUtils.isEmpty(account.getRoleId())) {
            responseOperationFailed(routingContext, "角色不能为空");
            return;
        }

        accountService.saveOrUpdate(account);
        responseSuccess(routingContext);
    }

    /**
     * 不允许修改账号密码状态角色信息
     */
    public void update(RoutingContext routingContext) {
        Account account = routingContext.getBodyAsJson().mapTo(Account.class);

        if (StringUtils.isEmpty(account.getAccount())) {
            responseOperationFailed(routingContext, "帐号不能为空");
            return;
        }

        if (ObjectUtils.isEmpty(account.getRoleId())) {
            responseOperationFailed(routingContext, "角色不能为空");
            return;
        }

        Account loginAccount = accountService.getAccountByAccountOrMobileOrEmail(routingContext.get("account"), null, null);
        /**
         * 如果当前登录的不是超级管理员帐号，不允许创建超级帐号
         */
        if (loginAccount.getRoleId() != 1) {
            if (!loginAccount.getAccount().equalsIgnoreCase(account.getAccount())) {
                // 除超级管理员，不允许其他角色修改除自己帐号外的其他帐号信息
                responseOperationFailed(routingContext, "不允许修改其他帐号信息");
            }
        }

        Account dbAccount = accountService.getAccountByAccountOrMobileOrEmail(account.getAccount(), null, null);

        dbAccount.setName(account.getName());
        dbAccount.setSex(account.getSex());
        dbAccount.setMobile(account.getMobile());
        dbAccount.setEmail(account.getEmail());
        dbAccount.setAddress(account.getAddress());
        dbAccount.setRemark(account.getRemark());

        accountService.saveOrUpdate(dbAccount);
        responseSuccess(routingContext);
    }

    public void delete(RoutingContext routingContext) {
        String accountId = routingContext.pathParam("accountId");
        if (StringUtils.isEmpty(accountId)) {
            responseOperationFailed(routingContext, "accountId参数缺失");
            return;
        }

        if (accountId.equalsIgnoreCase("1")) {
            responseOperationFailed(routingContext, "超级管理员帐号不允许删除");
            return;
        }

        accountService.deleteByAccountId(Integer.valueOf(accountId));
        responseSuccess(routingContext);
    }

    public void query(RoutingContext routingContext) {
        MultiMap params = routingContext.queryParams();

        List<Account> list = accountService.findByKeyWord(RequestUtils.getInteger(params, "roleId"), params.get("keyword"), RequestUtils.getInteger(params, "offset"), RequestUtils.getInteger(params, "rows"));
        long count = accountService.countByKeyWord(RequestUtils.getInteger(params, "roleId"), params.get("keyword"));

        Map map = new HashMap();
        map.put("total", count);
        map.put("list", list);

        responseSuccess(routingContext, map);
    }

    public void queryOne(RoutingContext routingContext) {
        String accountId = routingContext.pathParam("accountId");
        if (StringUtils.isEmpty(accountId)) {
            responseOperationFailed(routingContext, "accountId参数缺失");
            return;
        }
        Account account = accountService.getAccountByAccountId(Integer.valueOf(accountId));
        responseSuccess(routingContext, account);
    }

    public void currentLogin(RoutingContext routingContext) {
        String acc = routingContext.get("account");
        if (StringUtils.isEmpty(acc)) {
            responseOperationFailed(routingContext, "未登录");
            return;
        }

        Account account = accountService.getAccountByAccountOrMobileOrEmail(acc, null, null);
        List<Popedom> list = rolePodomService.findVuePopedomByRoleId(account.getRoleId());

        Map map = new HashMap();
        map.put("account", account);
        Set<String> codes = new HashSet<>();
        for (Popedom popedom : list) {
            codes.add(popedom.getCode());
        }
        map.put("list", codes);

        responseSuccess(routingContext, map);
    }

    public void resetPassword(RoutingContext routingContext) {
        String accountId = routingContext.pathParam("accountId");
        if (StringUtils.isEmpty(accountId)) {
            responseOperationFailed(routingContext, "accountId参数缺失");
            return;
        }
        accountService.resetPassword(Integer.valueOf(accountId));
        responseSuccess(routingContext);
    }

    public void editPassword(RoutingContext routingContext) {
        JsonObject jsonObject = routingContext.getBodyAsJson();

        if (!jsonObject.containsKey("oldPwd")) {
            responseOperationFailed(routingContext, "oldPwd参数缺失");
            return;
        }

        if (!jsonObject.containsKey("newPwd")) {
            responseOperationFailed(routingContext, "newPwd参数缺失");
            return;
        }

        if (!jsonObject.containsKey("newPwdCheck")) {
            responseOperationFailed(routingContext, "newPwdCheck参数缺失");
            return;
        }

        String acc = routingContext.get("account");
        if (StringUtils.isEmpty(acc)) {
            responseOperationFailed(routingContext, "未登录");
            return;
        }

        Account account = accountService.getAccountByAccountOrMobileOrEmail(acc, null, null);

        if (!MD5Utils.validateMd5WithSalt(jsonObject.getString("oldPwd"), account.getPassword())) {
            responseOperationFailed(routingContext, "原始密码输入有误");
            return;
        }

        if (!jsonObject.getString("newPwd").equals(jsonObject.getString("newPwdCheck"))) {
            responseOperationFailed(routingContext, "新密码两次输入不一致");
            return;
        }

        account.setPassword(jsonObject.getString("newPwd"));
        accountService.saveOrUpdate(account);
        responseSuccess(routingContext);
    }

    public void logOut(RoutingContext routingContext) {
        LocalMap<String, Session> localMap = routingContext.vertx().sharedData().getLocalMap(SessionConstant.SESSION_STORE_MAP);
        localMap.remove(routingContext.session().id());
        routingContext.session().destroy();
        responseSuccess(routingContext);
    }

    public void listSession(RoutingContext routingContext) {
        MultiMap params = routingContext.queryParams();
        Integer offset = RequestUtils.getInteger(params, "offset");
        Integer rows = RequestUtils.getInteger(params, "rows");

        if (ObjectUtils.isEmpty(offset)) {
            offset = 0;
        }
        if (ObjectUtils.isEmpty(rows)) {
            rows = 20;
        }

        List<Session> list = new ArrayList<>();
        LocalMap<String, Session> localMap = routingContext.vertx().sharedData().getLocalMap(SessionConstant.SESSION_STORE_MAP);
        localMap.forEach((k, v) -> {
            list.add(v);
        });

        list.sort((o1, o2) -> (int) (o2.lastAccessed() - o1.lastAccessed()));

        JsonArray jsonArray = new JsonArray();

        for (int i = offset; i < offset.intValue() + rows.intValue() && i < list.size(); i++) {
            JsonObject item = new JsonObject();
            item.put("account", (String) list.get(i).get(SessionConstant.SESSION_ACCOUNT));
            item.put("roleName", (String) list.get(i).get(SessionConstant.SESSION_ROLE_NAME));
            item.put("firstActiveTime", (Long) list.get(i).get(SessionConstant.SESSION_FIRST_ACTIVE_TIME));
            item.put("lastActiveTime", list.get(i).lastAccessed());
            item.put("remoteIpAddress", (String) list.get(i).get(SessionConstant.SESSION_IP_ADDRESS));
            jsonArray.add(item);
        }

        JsonObject map = new JsonObject();
        map.put("total", list.size());
        map.put("list", jsonArray);

        responseSuccess(routingContext, map);
    }

}
