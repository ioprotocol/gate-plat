package com.github.gate.protocol.builder;

import com.github.gate.protocol.type.CoderProperties;
import com.github.gate.protocol.type.ColumnType;
import com.github.gate.protocol.type.LengthProperties;

import java.nio.charset.Charset;

public class StringCoderBuilder {

    public static CoderProperties buildString(String name, int length, String charset, String invalidValue, boolean isMask) {
        return new CoderProperties().setColumnType(ColumnType.STRING).setName(name).setLength(length).setCharset(Charset.forName(charset)).setInvalidValue(invalidValue).setMask(isMask);
    }

    public static CoderProperties buildString(String name, int length, String charset, String invalidValue) {
        return new CoderProperties().setColumnType(ColumnType.STRING).setName(name).setLength(length).setCharset(Charset.forName(charset)).setInvalidValue(invalidValue);
    }

    public static CoderProperties buildString(String name, int length, String charset) {
        return new CoderProperties().setColumnType(ColumnType.STRING).setName(name).setLength(length).setCharset(Charset.forName(charset));
    }

    public static CoderProperties buildString(String name, int length) {
        return new CoderProperties().setColumnType(ColumnType.STRING).setName(name).setLength(length);
    }

    public static CoderProperties buildString(String name, LengthProperties lengthProperties, String charset, String invalidValue, boolean isMask) {
        return new CoderProperties().setColumnType(ColumnType.STRING).setName(name).setLengthProperties(lengthProperties).setCharset(Charset.forName(charset)).setInvalidValue(invalidValue).setMask(isMask);
    }

    public static CoderProperties buildString(String name, LengthProperties lengthProperties, String charset, String invalidValue) {
        return new CoderProperties().setColumnType(ColumnType.STRING).setName(name).setLengthProperties(lengthProperties).setCharset(Charset.forName(charset)).setInvalidValue(invalidValue);
    }

    public static CoderProperties buildString(String name, LengthProperties lengthProperties, String charset) {
        return new CoderProperties().setColumnType(ColumnType.STRING).setName(name).setLengthProperties(lengthProperties).setCharset(Charset.forName(charset));
    }

    public static CoderProperties buildString(String name, LengthProperties lengthProperties) {
        return new CoderProperties().setColumnType(ColumnType.STRING).setName(name).setLengthProperties(lengthProperties);
    }
}
