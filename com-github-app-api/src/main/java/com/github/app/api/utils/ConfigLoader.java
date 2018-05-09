package com.github.app.api.utils;

import com.github.app.utils.ServerEnvConstant;
import io.vertx.core.json.JsonObject;
import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;

public class ConfigLoader {
    private static JsonObject sysConfig;

    public static JsonObject getServerCfg() {
        if (sysConfig != null) {
            return sysConfig;
        }

        String path = ServerEnvConstant.getAppServerCfgFilePath();
        try {
            String confFileContent = FileUtils.readFileToString(new File(path), "UTF-8");
            String rep = ServerEnvConstant.getAppServerHome();
            if (System.getProperty("os.name").toLowerCase().startsWith("win")) {
                rep = rep.replace("\\", File.separator + File.separator + File.separator + File.separator);
            }
            confFileContent = confFileContent.replaceAll("\\$\\{" + ServerEnvConstant.APP_HOME + "}", rep);
            sysConfig = new JsonObject(confFileContent);
            return sysConfig;
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.exit(-1);
        return null;
    }
}
