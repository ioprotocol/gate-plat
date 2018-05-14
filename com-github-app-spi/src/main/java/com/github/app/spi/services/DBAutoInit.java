package com.github.app.spi.services;

import java.io.IOException;

public interface DBAutoInit {
    /**
     * 1. 创建数据库
     * 2. 创建表结构、索引
     * 3. 导入初始化数据
     * 4. 授予超级管理员全部权限
     *
     * @throws IOException
     */
    void init() throws IOException;

    /**
     * 菜单等资源增量升级
     */
    void popedomUpgrade();
}
