package com.github.gate.coder;

import com.github.gate.protocol.type.ProtocolProperties;

public class JsonCoderConfig implements CoderConfig {
    private ProtocolProperties protocolProperties;

    public ProtocolProperties getProtocolProperties() {
        return protocolProperties;
    }

    public void setProtocolProperties(ProtocolProperties protocolProperties) {
        this.protocolProperties = protocolProperties;
    }

    @Override
    public String getName() {
        return "JsonCoder";
    }
}
