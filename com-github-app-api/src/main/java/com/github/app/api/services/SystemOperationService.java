package com.github.app.api.services;

import com.github.app.api.config.AppServerConfig;
import io.vertx.core.json.JsonObject;

public interface SystemOperationService {
    void install(AppServerConfig serverConfig);

    void backup(AppServerConfig serverConfig);

    void restore(AppServerConfig serverConfig, String fileName);

    JsonObject list(Integer offset, Integer rows);

    void deleteSqlFile(String fileName);
}
