package com.github.app.runner;

public enum BootType {
    INSTALL("初始安装"), BACKUP("备份到文件"), RESTORE("从文件恢复"), SERVER("启动后台服务");

    private String name;

    BootType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
