package com.github.runner;

public enum BootType {
    BACKUP("备份到文件"),
    RESTORE("从文件恢复"),
    ZK("ZooKeeper服务"),
    DAEMON("托管服务"),
    SERVER("启动后台服务");

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
