package com.github.app.spi.services;

import com.github.app.spi.config.AppServerConfig;
import io.vertx.core.json.JsonObject;

public interface SystemOperationService {
    void createDatabase(AppServerConfig serverConfig);

    void backup(AppServerConfig serverConfig);

    void restore(AppServerConfig serverConfig, String fileName);

    JsonObject list(Integer offset, Integer rows);

    void deleteSqlFile(String fileName);
}
