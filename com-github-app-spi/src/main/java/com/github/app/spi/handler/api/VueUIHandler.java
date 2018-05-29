package com.github.app.spi.handler.api;

import com.github.app.spi.dao.domain.Popedom;
import com.github.app.spi.handler.AuthUriHandler;
import io.vertx.ext.web.Router;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 该类主要注册前端页面上需要管理的权限信息
 */
@Component
public class VueUIHandler implements AuthUriHandler {
    @Override
    public void registeUriHandler(Router router) {

    }

    @Override
    public void registePopedom(List<Popedom> list) {
        list.add(Popedom.builder().name("vue[主面板]").remark("网页权限，系统首页").code("vuedashboard").build());
        list.add(Popedom.builder().name("vue[系统管理]").remark("网页权限，系统管理菜单").code("vuesystemmgr").build());
        list.add(Popedom.builder().name("vue[账号管理]").remark("网页权限，帐号管理页面").code("vueaccountmgr").build());
        list.add(Popedom.builder().name("vue[在线用户]").remark("网页权限，在线用户页面").code("vueonlineaccount").build());
        list.add(Popedom.builder().name("vue[角色管理]").remark("网页权限，角色管理页面").code("vuerolemgr").build());
        list.add(Popedom.builder().name("vue[数据备份]").remark("网页权限，数据备份页面").code("vuedbback").build());
        list.add(Popedom.builder().name("vue[日志管理]").remark("网页权限，日志管理页面").code("vuelogmgr").build());
        list.add(Popedom.builder().name("vue[性能统计]").remark("网页权限，性能统计页面").code("vueperformance").build());
    }
}
