package com.github.server.config;

import com.github.gate.coder.CoderConfig;
import com.google.common.collect.Lists;

import java.util.List;

public class TcpGatewayConfig {
    private String host;
    private Integer port;
    private boolean tcpNoDelay = true;
    private boolean tcpKeepAlive = true;
    private boolean reusePort = true;
    private List<CoderConfig> coderConfigs = Lists.newArrayList();

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public boolean isTcpNoDelay() {
        return tcpNoDelay;
    }

    public void setTcpNoDelay(boolean tcpNoDelay) {
        this.tcpNoDelay = tcpNoDelay;
    }

    public boolean isTcpKeepAlive() {
        return tcpKeepAlive;
    }

    public void setTcpKeepAlive(boolean tcpKeepAlive) {
        this.tcpKeepAlive = tcpKeepAlive;
    }

    public boolean isReusePort() {
        return reusePort;
    }

    public void setReusePort(boolean reusePort) {
        this.reusePort = reusePort;
    }

    public List<CoderConfig> getCoderConfigs() {
        return coderConfigs;
    }

    public void setCoderConfigs(List<CoderConfig> coderConfigs) {
        this.coderConfigs = coderConfigs;
    }
}
