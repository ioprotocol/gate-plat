package com.github.app.spi.services.impl;

import com.github.app.spi.config.AppServerConfig;
import com.github.app.spi.services.SystemOperationService;
import com.github.app.utils.MD5Utils;
import com.github.app.utils.ServerEnvConstant;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import org.apache.commons.exec.*;
import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;

@Component
public class SystemOperationServiceImpl implements SystemOperationService {
    private Logger logger = LogManager.getLogger(SystemOperationServiceImpl.class);

    @Override
    public void createDatabase(AppServerConfig serverConfig) {
        if(!serverConfig.getDataSourceDriverClassName().contains("mysql"))
            return;
        logger.info("begin import mysql data");
        String url = serverConfig.getDataSourceUrl();

        String host = parseHost(url);
        String port = parsePort(url);
        String databaseName = parseDatabaseName(url);

        CommandLine commandLine = new CommandLine("mysql");
        commandLine.addArgument("--host=" + host);
        commandLine.addArgument("-P" + port);
        commandLine.addArgument("-u" + serverConfig.getDataSourceUserName());
        commandLine.addArgument("-p" + serverConfig.getDataSourcePassword());

        String sql = "drop database if exists " + databaseName + "; CREATE DATABASE IF NOT EXISTS " + databaseName +" DEFAULT CHARSET utf8 COLLATE utf8_general_ci;";
        InputStream inputStream = new ByteArrayInputStream(sql.getBytes());
        exeMysqlImport(inputStream, commandLine);
    }

    @Override
    public void backup(AppServerConfig serverConfig) {
        if(!serverConfig.getDataSourceDriverClassName().contains("mysql"))
            return;
        logger.info("begin export mysql data");
        String url = serverConfig.getDataSourceUrl();
        String host = parseHost(url);
        String port = parsePort(url);
        String databaseName = parseDatabaseName(url);

        String outputFileName = ServerEnvConstant.getAppServerHome()
                + File.separator + "data"
                + File.separator + "backup"
                + File.separator + "" + LocalDateTime.now()
                .format(DateTimeFormatter.ofPattern("yyMMddHHmmss")) + ".sql";

        CommandLine commandLine = new CommandLine("mysqldump");
        commandLine.addArgument("--host=" + host, false);
        commandLine.addArgument("-P" + port);
        commandLine.addArgument("-u" + serverConfig.getDataSourceUserName());
        commandLine.addArgument("-p" + serverConfig.getDataSourcePassword());
        commandLine.addArgument(databaseName, false);

        Executor executor = new DefaultExecutor();
        executor.setExitValue(0);

        ExecuteWatchdog watchdog = new ExecuteWatchdog(5*60000);
        executor.setWatchdog(watchdog);

        try {
            ByteArrayOutputStream errorStream = new ByteArrayOutputStream();
            OutputStream outFileStream = new FileOutputStream(outputFileName);
            executor.setStreamHandler(new PumpStreamHandler(outFileStream, errorStream));

            String error = errorStream.toString("utf-8");
            logger.info(error);
            int exitValue = executor.execute(commandLine);
            logger.info("export executor finish, exit value is " + exitValue);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("export mysql data failed");
            logger.error(e.getLocalizedMessage());
        }
    }

    @Override
    public void restore(AppServerConfig serverConfig, String fileName) {
        if(!serverConfig.getDataSourceDriverClassName().contains("mysql"))
            return;
        logger.info("begin import mysql data");
        String url = serverConfig.getDataSourceUrl();

        String host = parseHost(url);
        String port = parsePort(url);
        String databaseName = parseDatabaseName(url);

        String sqlFileName = ServerEnvConstant.getAppServerHome()
                + File.separator + "data"
                + File.separator + "backup"
                + File.separator + fileName;

        CommandLine commandLine = new CommandLine("mysql");
        commandLine.addArgument("--host=" + host);
        commandLine.addArgument("-P" + port);
        commandLine.addArgument("-u" + serverConfig.getDataSourceUserName());
        commandLine.addArgument("-p" + serverConfig.getDataSourcePassword());
        commandLine.addArgument("-D" + databaseName);

        exeMysqlImport(sqlFileName, commandLine);
    }

    @Override
    public JsonObject list(Integer offset, Integer rows) {
        if (ObjectUtils.isEmpty(offset)) {
            offset = Integer.valueOf(0);
        }

        if (ObjectUtils.isEmpty(rows)) {
            rows = Integer.valueOf(20);
        }

        JsonArray jsonArray = new JsonArray();

        String path = ServerEnvConstant.getAppServerHome() + File.separator + "data" + File.separator + "backup";

        File dir = new File(path);

        LinkedList<File> files = (LinkedList<File>) FileUtils.listFiles(dir, null, true);
        Collections.sort(files, new CompratorByLastModified());

        for(int i = offset; i < files.size() && i < offset + rows; i++) {
            File file = files.get(i);
            JsonObject jsonObject = new JsonObject();
            jsonObject.put("name", file.getName());
            jsonObject.put("size", file.length());
            jsonObject.put("createTime", file.lastModified());
            jsonObject.put("code", MD5Utils.md5WithSalt(file.getName()));
            jsonArray.add(jsonObject);
        }

        return new JsonObject().put("list", jsonArray).put("total", files.size());
    }

    @Override
    public void deleteSqlFile(String fileName) {
        String path = ServerEnvConstant.getAppServerHome() + File.separator + "data" + File.separator + "backup";
        File file = new File(path + File.separator + fileName);
        if (file.exists()) {
            file.delete();
        }
    }

    /**
     *
     * @param fileName
     * @param commandLine
     */
    private void exeMysqlImport(String fileName, CommandLine commandLine) {
        try {
            exeMysqlImport(new FileInputStream(fileName), commandLine);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("import mysql data failed");
            logger.error(e.getLocalizedMessage());
        }
    }

    private void exeMysqlImport(InputStream inputStream, CommandLine commandLine) {
        Executor executor = new DefaultExecutor();
        executor.setExitValue(0);

        ExecuteWatchdog watchdog = new ExecuteWatchdog(5*60000);
        executor.setWatchdog(watchdog);
        ByteArrayOutputStream errorStream = new ByteArrayOutputStream();
        try {
            executor.setStreamHandler(new PumpStreamHandler(null, errorStream, inputStream));

            logger.info(commandLine.toString());
            int exitValue = executor.execute(commandLine);

            // some time later the result handler callback was invoked so we
            // can safely request the exit value
            logger.info("import executor finish, exit value is " + exitValue);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("import mysql data failed");
            logger.error(e.getLocalizedMessage());
            logger.error(errorStream.toString());
        }
    }

    private String parseHost(String uri) {
        String tmp = uri.substring(13);
        tmp = tmp.substring(0, tmp.indexOf(":"));
        return tmp;
    }

    private String parsePort(String uri) {
        String host = parseHost(uri);
        String tmp = uri.substring(uri.indexOf(host) + host.length() + 1);
        tmp = tmp.substring(0, tmp.indexOf("/"));
        return tmp;
    }

    private String parseDatabaseName(String uri) {
        String port = parsePort(uri);
        String tmp = uri.substring(uri.indexOf(port) + port.length() + 1);
        tmp = tmp.substring(0, tmp.indexOf("?"));
        return tmp;
    }

    static class CompratorByLastModified implements Comparator<File> {

        public int compare(File f1, File f2) {
            long diff = f1.lastModified() - f2.lastModified();
            if (diff > 0) {
                return -1;
            } else if (diff == 0) {
                return 0;
            } else {
                return 1;
            }
        }
    }
}
