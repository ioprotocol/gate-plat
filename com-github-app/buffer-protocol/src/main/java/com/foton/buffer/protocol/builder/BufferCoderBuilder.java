package com.foton.buffer.protocol.builder;

import com.foton.buffer.protocol.type.CoderProperties;
import com.foton.buffer.protocol.type.ColumnType;
import com.foton.buffer.protocol.type.LengthProperties;

public class BufferCoderBuilder {
    public static CoderProperties buildBuffer(String name, int length, byte[] invalidValue, boolean isMask) {
        return new CoderProperties().setColumnType(ColumnType.BUFFER).setName(name).setLength(length).setInvalidValue(invalidValue).setMask(isMask);
    }

    public static CoderProperties buildBuffer(String name, int length, byte[] invalidValue) {
        return new CoderProperties().setColumnType(ColumnType.BUFFER).setName(name).setLength(length).setInvalidValue(invalidValue);
    }

    public static CoderProperties buildBuffer(String name, int length) {
        return new CoderProperties().setColumnType(ColumnType.BUFFER).setName(name).setLength(length);
    }

    public static CoderProperties buildBuffer(String name, LengthProperties lengthProperties, byte[] invalidValue, boolean isMask) {
        return new CoderProperties().setColumnType(ColumnType.BUFFER).setName(name).setLengthProperties(lengthProperties).setInvalidValue(invalidValue).setMask(isMask);
    }

    public static CoderProperties buildBuffer(String name, LengthProperties lengthProperties, byte[] invalidValue) {
        return new CoderProperties().setColumnType(ColumnType.BUFFER).setName(name).setLengthProperties(lengthProperties).setInvalidValue(invalidValue);
    }

    public static CoderProperties buildBuffer(String name, LengthProperties lengthProperties) {
        return new CoderProperties().setColumnType(ColumnType.BUFFER).setName(name).setLengthProperties(lengthProperties);
    }
}
