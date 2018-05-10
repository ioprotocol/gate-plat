package com.github.app.spi.services.impl;

import com.github.app.spi.dao.domain.Popedom;
import com.github.app.spi.dao.domain.RolePopedom;
import com.github.app.spi.handler.UriHandler;
import com.github.app.spi.services.DBAutoInit;
import com.github.app.spi.services.RolePodomService;
import com.github.app.spi.utils.AppContext;
import com.github.app.utils.ServerEnvConstant;
import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
public class DBAutoInitImpl implements DBAutoInit {
    private Logger logger = LogManager.getLogger(DBAutoInitImpl.class);

    @Autowired
    private DataSource dataSource;
    @Autowired
    private RolePodomService rolePodomService;

    @Override
    public void init() throws IOException {
        JdbcTemplate jdbcTemplate = new JdbcTemplate();
        jdbcTemplate.setDataSource(dataSource);

        List<String> sqlLines = FileUtils.readLines(new File(ServerEnvConstant.getAppServerDBInitSQLFilePath()));
        // create base tables
        for (String sql : sqlLines) {
            logger.info(sql);
            jdbcTemplate.execute(sql);
        }

        /** 生成菜单元数据 **/
        List<Popedom> popedoms = new ArrayList<>();
        String[] beanNames = AppContext.getContext().getBeanNamesForType(UriHandler.class);
        for (String bean : beanNames) {
            Object beanObj = AppContext.getContext().getBean(bean);
            UriHandler uriHandler = (UriHandler) beanObj;
            uriHandler.registePopedom(popedoms);
        }
        rolePodomService.savePopedom(popedoms);

        popedoms = rolePodomService.findAllPopedom();
        /** 授予超级管理员全部的资源访问权限 **/
        List<RolePopedom> rolePopedoms = new ArrayList<>();
        for (Popedom popedom : popedoms) {
            RolePopedom rolePopedom = new RolePopedom();
            rolePopedom.setPopedomId(popedom.getPopedomId());
            rolePopedom.setRoleId(1);
            rolePopedoms.add(rolePopedom);
        }
        rolePodomService.addRolePopedoms(rolePopedoms);
    }
}
