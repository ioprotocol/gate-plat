package com.foton.buffer.protocol.type;

public class BitProperties {
    // 位名称
    private String name;
    // 位长度
    private int length = 0;
    // 默认值
    private Integer defaultValue;
    // 无效值
    private Integer invalidValue;
    // 是否代表某个引用的长度
    private boolean isModuleLengthField = false;
    // 是否在解码时屏蔽输出
    private boolean isMask = false;

    public BitProperties() {
    }

    public BitProperties(String name, int length) {
        this.name = name;
        this.length = length;
    }

    public BitProperties(String name, int length, Integer defaultValue) {
        this.name = name;
        this.length = length;
        this.defaultValue = defaultValue;
    }

    public BitProperties(String name, int length, Integer defaultValue, Integer invalidValue) {
        this.name = name;
        this.length = length;
        this.defaultValue = defaultValue;
        this.invalidValue = invalidValue;
    }

    public BitProperties(String name, int length, Integer defaultValue, Integer invalidValue, boolean isMask) {
        this.name = name;
        this.length = length;
        this.defaultValue = defaultValue;
        this.invalidValue = invalidValue;
        this.isMask = isMask;
    }

    public String getName() {
        return name;
    }

    public BitProperties setName(String name) {
        this.name = name;
        return this;
    }

    public int getLength() {
        return length;
    }

    public BitProperties setLength(int length) {
        this.length = length;
        return this;
    }

    public Integer getInvalidValue() {
        return invalidValue;
    }

    public BitProperties setInvalidValue(Integer invalidValue) {
        this.invalidValue = invalidValue;
        return this;
    }

    public boolean isMask() {
        return isMask;
    }

    public BitProperties setMask(boolean mask) {
        isMask = mask;
        return this;
    }

    public Integer getDefaultValue() {
        return defaultValue;
    }

    public BitProperties setDefaultValue(Integer defaultValue) {
        this.defaultValue = defaultValue;
        return this;
    }

    public boolean isModuleLengthField() {
        return isModuleLengthField;
    }

    public BitProperties setModuleLengthField(boolean moduleLengthField) {
        isModuleLengthField = moduleLengthField;
        return this;
    }
}
