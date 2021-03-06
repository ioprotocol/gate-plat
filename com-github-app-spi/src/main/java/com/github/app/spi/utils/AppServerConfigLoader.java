package com.github.app.spi.utils;

import com.github.app.spi.config.AppServerConfig;
import com.github.app.utils.FieldParser;
import com.github.app.utils.JacksonUtils;
import com.github.app.utils.ServerEnvConstant;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.Map;
import java.util.Properties;

public class AppServerConfigLoader {
    private static AppServerConfig sysConfig;

    public static AppServerConfig getServerCfg() {
        if (sysConfig != null) {
            return sysConfig;
        }

        try {
            String path = ServerEnvConstant.getAppServerCfgFilePath();
            Properties properties = new Properties();
            BufferedReader bf = new BufferedReader(new InputStreamReader(new FileInputStream(path),"UTF-8"));
            properties.load(bf);
            sysConfig = new AppServerConfig();
            FieldParser.update((Map) properties, sysConfig);
            return sysConfig;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void main(String[] args) {
        System.out.println(JacksonUtils.serializePretty(getServerCfg()));
    }
}
