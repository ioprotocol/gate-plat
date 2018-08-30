package com.github.app.utils;

import org.apache.commons.lang.StringUtils;

import java.io.File;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * 用户可以通过指定环境变量或系统属性，来改变程序的配置
 */
public class ServerEnvConstant {
    public static final String APP_HOME = "VERTX_HOME";
    public static final String APP_LOG4J2_CFG_FILE = "VERTX_LOG4J2_CONFIG";
    public static final String APP_SERVER_CFG_FILE = "VERTX_SERVER_CONFIG";
    public static final String APP_ZK_CFG_FILE = "VERTX_ZK_CONFIG";

    /**
     * 获取zk 配置文件的完整路径名
     *
     * @return
     */
    public static String getZkConfigFilePath() {
        String path = getSystemEnv(APP_ZK_CFG_FILE);

        if (StringUtils.isEmpty(path)) {
            path = getAppServerHome() + File.separator + "config" + File.separator + "zookeeper.conf";
        }
        return path;
    }

    /**
     * 获取log4j配置文件位置
     *
     * @return
     */
    public static String getLog4j2CfgFilePath() {
        String appLog4j2CfgFile = getSystemEnv(APP_LOG4J2_CFG_FILE);

        if (appLog4j2CfgFile == null || appLog4j2CfgFile.length() < 1) {
            appLog4j2CfgFile = getAppServerHome() + File.separator + "config" + File.separator + "log4j2.xml";
        }
        return appLog4j2CfgFile;
    }

    /**
     * 获取服务的配置文件位置
     *
     * @return
     */
    public static String getAppServerCfgFilePath() {
        String path = getSystemEnv(APP_SERVER_CFG_FILE);
        if (path == null || path.length() < 1) {
            path = getAppServerHome() + File.separator + "config" + File.separator + "server.conf";
        }
        return path;
    }

    /**
     * 获取配置文件的默认存放目录
     *
     * @return
     */
    public static String getAppServerCfgFilePath(String path) {
        return getAppServerHome() + File.separator + "config" + File.separator + path;
    }

    /**
     * 获取数据库的初始化脚本
     *
     * @return
     */
    public static String getAppServerDBInitSQLFilePath() {
        return getAppServerHome() + File.separator + "data" + File.separator + "dbinit.sql";
    }

    /**
     * 获取程序根目录
     *
     * @return
     */
    public static String getAppServerHome() {
        String path = getSystemEnv(APP_HOME);
        checkNotNull(path, "system env:VERTX_HOME is empty");
        return path;
    }

    /**
     * 通过系统属性设置程序的根目录
     *
     * @param path
     */
    public static void setAppServerHome(String path) {
        System.setProperty(APP_HOME, path);
    }

    private static String getSystemEnv(String key) {
        String value = System.getProperty(key);
        if (value == null || value.length() < 1) {
            value = System.getenv(key);
        }
        return value;
    }
}
