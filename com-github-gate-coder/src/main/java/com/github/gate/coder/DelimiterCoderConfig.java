package com.github.gate.coder;

public class DelimiterCoderConfig implements CoderConfig{

    private String header;
    private String tailer;

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public String getTailer() {
        return tailer;
    }

    public void setTailer(String tailer) {
        this.tailer = tailer;
    }

    @Override
    public String getName() {
        return "DelimiterCoder";
    }
}
