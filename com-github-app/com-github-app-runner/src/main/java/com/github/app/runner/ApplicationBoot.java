package com.github.app.runner;

import com.github.app.api.runner.BackupMySqlDatabaseRunner;
import com.github.app.api.runner.InitMySqlDataBaseRunner;
import com.github.app.api.runner.RestoreMySqlDatabaseRunner;
import com.github.app.api.runner.ServerRunner;
import com.github.app.deploy.HotDeployRunner;
import com.github.app.utils.Runner;
import com.github.app.utils.ServerEnvConstant;
import io.vertx.core.cli.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.LoggerContext;
import java.io.File;
import java.net.URI;
import java.util.*;

public class ApplicationBoot {
    static Optional<CommandLine> cli(String[] args) {
        CLI cli = CLI.create("ApplicationBooter")
                .setSummary("An application runner instance")
                .addOption(new Option()
                        .setLongName("name")
                        .setShortName("name")
                        .setDescription("vert.x config file (in json format)")
                        .setRequired(false)
                );

        // parsing
        CommandLine commandLine = null;
        try {
            List<String> userCommandLineArguments = Arrays.asList(args);
            commandLine = cli.parse(userCommandLineArguments);
        } catch(CLIException e) {
            // usage
            StringBuilder builder = new StringBuilder();
            cli.usage(builder);
            System.out.println(builder.toString());
            System.exit(-1);
        }
        return Optional.ofNullable(commandLine);
    }

    static Map<String, Runner> runnerMap = new HashMap<>();
    static {
        addRunner(new ServerRunner());
        addRunner(new InitMySqlDataBaseRunner());
        addRunner(new BackupMySqlDatabaseRunner());
        addRunner(new RestoreMySqlDatabaseRunner());
        addRunner(new HotDeployRunner());
    }

    private static void addRunner(Runner runner) {
        runnerMap.put(runner.name(), runner);
    }

    public static void main(String[] args) throws Exception {
        setup();

        Optional<CommandLine> commandLine = cli(args);
        if(commandLine.isPresent()) {
            String name = commandLine.get().getRawValueForOption(commandLine.get().cli().getOption("name"));
            if(name != null && runnerMap.containsKey(name)) {
                runnerMap.get(name).start(args);
            } else {
                System.out.println(buildUsage());
                System.exit(-1);
            }
        } else {
            System.out.println(buildUsage());
            System.exit(-1);
        }
    }

    private static String buildUsage() {
        StringBuilder builder = new StringBuilder();
        runnerMap.forEach((name, runner) -> {
            runner.usage(builder);
            builder.append("\n");
        });
        return builder.toString();
    }

    public static void setup() throws Exception {
        /**
         * set log4j2 cfg
         */
        LoggerContext ctx = (LoggerContext) LogManager.getContext(false);
        String path = ServerEnvConstant.getLog4j2CfgFilePath();

        File file = new File(path);
        URI configURI = file.toURI();
        ctx.setConfigLocation(configURI);
        ctx.reconfigure();
        /**
         * set vert.x log impl
         */
        System.setProperty("vertx.logger-delegate-factory-class-name", "io.vertx.core.logging.Log4j2LogDelegateFactory");
    }
}
