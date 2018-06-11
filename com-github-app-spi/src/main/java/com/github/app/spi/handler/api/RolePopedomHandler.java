package com.github.app.spi.handler.api;

import com.fasterxml.jackson.core.type.TypeReference;
import com.github.app.spi.dao.domain.Popedom;
import com.github.app.spi.dao.domain.Role;
import com.github.app.spi.dao.domain.RolePopedom;
import com.github.app.spi.handler.AuthUriHandler;
import com.github.app.spi.services.AccountService;
import com.github.app.spi.services.RolePodomService;
import com.github.app.spi.utils.RequestUtils;
import com.github.app.utils.JacksonUtils;
import io.vertx.core.MultiMap;
import io.vertx.core.http.HttpMethod;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class RolePopedomHandler implements AuthUriHandler {

    @Autowired
    private RolePodomService rolePodomService;
    @Autowired
    private AccountService accountService;

    @Override
    public void registeUriHandler(Router router) {
        router.post().path("/role").produces(CONTENT_TYPE).blockingHandler(this::saveOrUpdate, false);
        router.delete().path("/role/:roleId").produces(CONTENT_TYPE).blockingHandler(this::delete, false);
        router.put().path("/role").produces(CONTENT_TYPE).blockingHandler(this::saveOrUpdate, false);
        router.get().path("/role/:roleId").produces(CONTENT_TYPE).blockingHandler(this::queryOne, false);
        router.get().path("/role").produces(CONTENT_TYPE).blockingHandler(this::query, false);
        router.put().path("/role/popedom").produces(CONTENT_TYPE).blockingHandler(this::saveRolePopedom, false);
        router.get().path("/role/popedom/:roleId").produces(CONTENT_TYPE).blockingHandler(this::getRolePopedom, false);
        router.get().path("/role/popedom/vue/:roleId").produces(CONTENT_TYPE).blockingHandler(this::getRolePopedomVue, false);
        router.get().path("/role/popedom/api/:roleId").produces(CONTENT_TYPE).blockingHandler(this::getRolePopedomApi, false);
    }

    @Override
    public void registePopedom(List<Popedom> list) {
        list.add(new Popedom.Builder().name("增加角色").remark("管理员功能，创建系统角色").code("/.+/role/" + HttpMethod.POST.name()).build());
        list.add(new Popedom.Builder().name("删除角色").remark("管理员功能，删除系统角色").code("/.+/role/.+/" + HttpMethod.DELETE.name()).build());
        list.add(new Popedom.Builder().name("更新角色").remark("管理员功能，更新系统角色信息").code("/.+/role/" + HttpMethod.PUT.name()).build());
        list.add(new Popedom.Builder().name("查询角色").remark("查询系统的单个角色信息").code("/.+/role/.+/" + HttpMethod.GET.name()).build());
        list.add(new Popedom.Builder().name("查询所有角色").remark("查询系统的所有角色信息").code("/.+/role/" + HttpMethod.GET.name()).build());
        list.add(new Popedom.Builder().name("权限修改").remark("管理员功能，分配每个系统角色的操作权限").code("/.+/role/popedom/" + HttpMethod.PUT.name()).build());
        list.add(new Popedom.Builder().name("角色权限查询").remark("管理员功能，查询每个角色拥有的操作权限集合").code("/.+/role/popedom/.+/" + HttpMethod.GET.name()).build());
        list.add(new Popedom.Builder().name("界面权限查询").remark("基本功能，查询角色拥有的前端界面集合(可见)").code("/.+/role/popedom/vue/.+/" + HttpMethod.GET.name()).build());
        list.add(new Popedom.Builder().name("接口权限查询").remark("管理员功能，查询角色拥有的后台接口权限").code("/.+/role/popedom/api/.+/" + HttpMethod.GET.name()).build());
    }

    public void saveOrUpdate(RoutingContext routingContext) {
        Role role = mapTo(routingContext.getBody(), Role.class);
        if (StringUtils.isEmpty(role.getName())) {
            responseOperationFailed(routingContext, "name must be filled");
            return;
        }

        rolePodomService.saveOrUpdate(role);
        responseSuccess(routingContext);
    }
    
    public void delete(RoutingContext routingContext) {
        String roleId = routingContext.pathParam("roleId");
        if (StringUtils.isEmpty(roleId)) {
            responseOperationFailed(routingContext, "roleId must be supply");
            return;
        }

        if (roleId.equalsIgnoreCase("1")) {
            responseOperationFailed(routingContext, "超级管理员角色不允许删除");
            return;
        }

        if (accountService.isRoleHasAccount(Integer.valueOf(roleId))) {
            responseOperationFailed(routingContext, "该角色拥有账号，不允许删除");
            return;
        }

        rolePodomService.deleteRoleById(Integer.valueOf(roleId));
        responseSuccess(routingContext);
    }

    public void queryOne(RoutingContext routingContext) {
        String roleId = routingContext.pathParam("roleId");
        if (StringUtils.isEmpty(roleId)) {
            responseOperationFailed(routingContext, "roleId must be supply");
            return;
        }

        Role role = rolePodomService.getRoleById(Integer.valueOf(roleId));
        responseSuccess(routingContext, role);
    }

    public void query(RoutingContext routingContext) {
        MultiMap params = routingContext.queryParams();

        List<Role> list = rolePodomService.list(RequestUtils.getInteger(params, "offset"), RequestUtils.getInteger(params, "rows"));
        long count = rolePodomService.count();

        Map data = new HashMap<>();
        data.put("list", list);
        data.put("total", count);

        responseSuccess(routingContext, data);
    }

    public void saveRolePopedom(RoutingContext routingContext) {
        String json = routingContext.getBodyAsString();

        List<RolePopedom> list = JacksonUtils.deserialize(json, new TypeReference<List<RolePopedom>>(){});

        if(list.get(0).getRoleId() == 1) {
            responseOperationFailed(routingContext, "超级管理员权限不允许编辑");
            return;
        }

        rolePodomService.deleteRolePopedomById(list.get(0).getRoleId(), null);

        if(list.size() != 1 && list.get(0).getPopedomId() != -1) {
            rolePodomService.addRolePopedoms(list);
        }

        responseSuccess(routingContext);
    }

    public void getRolePopedom(RoutingContext routingContext) {
        String roleId = routingContext.pathParam("roleId");
        if (StringUtils.isEmpty(roleId)) {
            responseOperationFailed(routingContext, "roleId must be supply");
            return;
        }

        List<Popedom> list = rolePodomService.findPopedomByRoleId(Integer.valueOf(roleId));
        Map map = new HashMap();
        map.put("rolePopedoms", list);
        map.put("popedoms", rolePodomService.findAllPopedom());
        responseSuccess(routingContext, map);
    }

    public void getRolePopedomVue(RoutingContext routingContext) {
        String roleId = routingContext.pathParam("roleId");
        if (StringUtils.isEmpty(roleId)) {
            responseOperationFailed(routingContext, "roleId must be supply");
            return;
        }

        List<Popedom> list = rolePodomService.findVuePopedomByRoleId(Integer.valueOf(roleId));
        responseSuccess(routingContext, list);
    }

    public void getRolePopedomApi(RoutingContext routingContext) {
        String roleId = routingContext.pathParam("roleId");
        if (StringUtils.isEmpty(roleId)) {
            responseOperationFailed(routingContext, "roleId must be supply");
            return;
        }

        List<Popedom> list = rolePodomService.findApiPopedomByRoleId(Integer.valueOf(roleId));
        responseSuccess(routingContext, list);
    }
}
