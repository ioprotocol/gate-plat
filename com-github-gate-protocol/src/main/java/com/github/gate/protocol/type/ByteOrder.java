package com.github.gate.protocol.type;

public enum ByteOrder {
    BIG_ENDIAN("big_endian"), LITTLE_ENDIAN("little_endian");

    ByteOrder(String value) {
        this.value = value;
    }

    private String value;

    public String getValue() {
        return value;
    }

    public ByteOrder setValue(String value) {
        this.value = value;
        return this;
    }

    public static ByteOrder parse(String value) {
        if (value.equalsIgnoreCase(BIG_ENDIAN.getValue())) {
            return BIG_ENDIAN;
        } else if (value.equalsIgnoreCase(LITTLE_ENDIAN.getValue())) {
            return LITTLE_ENDIAN;
        }
        throw new RuntimeException("un support ByteOrder value");
    }
}
