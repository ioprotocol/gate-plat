package com.github.gate.coder;

public class EscapCoderConfig implements CoderConfig {
    private String raw;
    private String escap;

    public String getRaw() {
        return raw;
    }

    public void setRaw(String raw) {
        this.raw = raw;
    }

    public String getEscap() {
        return escap;
    }

    public void setEscap(String escap) {
        this.escap = escap;
    }

    @Override
    public String getName() {
        return "EscapCoder";
    }
}
