package com.github.app.utils;

import java.io.File;

/**
 * 用户可以通过指定环境变量或系统属性，来改变程序的配置
 */
public class ServerEnvConstant {
    public static final String APP_HOME = "vertx.home";
    public static final String APP_LOG4J2_CFG_FILE = "vertx.log4j2.config";
    public static final String APP_SERVER_CFG_FILE = "vertx.server.config";

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
            path = getAppServerHome() + File.separator + "config" + File.separator + "server.json";
        }
        return path;
    }

    /**
     * 获取程序根目录
     *
     * @return
     */
    public static String getAppServerHome() {
        String path = getSystemEnv(APP_HOME);

        if (path == null || path.length() < 1) {
            throw new RuntimeException("system env:" + APP_HOME + " is empty");
        }

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
