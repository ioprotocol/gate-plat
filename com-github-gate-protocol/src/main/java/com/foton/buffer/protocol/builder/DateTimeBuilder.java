package com.foton.buffer.protocol.builder;

import com.foton.buffer.protocol.type.ByteOrder;
import com.foton.buffer.protocol.type.CoderProperties;
import com.foton.buffer.protocol.type.ColumnType;
import com.foton.buffer.protocol.type.TimeFormat;

public class DateTimeBuilder {
    public static CoderProperties buildDateTime(String name, TimeFormat timeFormat, boolean isMask, ByteOrder byteOrder) {
        return new CoderProperties().setColumnType(ColumnType.DATETIME).setName(name).setTimeFormat(timeFormat).setMask(isMask).setByteOrder(byteOrder);
    }

    public static CoderProperties buildDateTime(String name, TimeFormat timeFormat, boolean isMask) {
        return new CoderProperties().setColumnType(ColumnType.DATETIME).setName(name).setTimeFormat(timeFormat).setMask(isMask);
    }

    public static CoderProperties buildDateTime(String name, TimeFormat timeFormat) {
        return new CoderProperties().setColumnType(ColumnType.DATETIME).setName(name).setTimeFormat(timeFormat);
    }

    public static CoderProperties buildDate(String name, TimeFormat timeFormat, boolean isMask, ByteOrder byteOrder) {
        return new CoderProperties().setColumnType(ColumnType.DATE).setName(name).setTimeFormat(timeFormat).setMask(isMask).setByteOrder(byteOrder);
    }

    public static CoderProperties buildDate(String name, TimeFormat timeFormat, boolean isMask) {
        return new CoderProperties().setColumnType(ColumnType.DATE).setName(name).setTimeFormat(timeFormat).setMask(isMask);
    }

    public static CoderProperties buildDate(String name, TimeFormat timeFormat) {
        return new CoderProperties().setColumnType(ColumnType.DATE).setName(name).setTimeFormat(timeFormat);
    }

    public static CoderProperties buildTime(String name, TimeFormat timeFormat, boolean isMask, ByteOrder byteOrder) {
        return new CoderProperties().setColumnType(ColumnType.TIME).setName(name).setTimeFormat(timeFormat).setMask(isMask).setByteOrder(byteOrder);
    }

    public static CoderProperties buildTime(String name, TimeFormat timeFormat, boolean isMask) {
        return new CoderProperties().setColumnType(ColumnType.TIME).setName(name).setTimeFormat(timeFormat).setMask(isMask);
    }

    public static CoderProperties buildTime(String name, TimeFormat timeFormat) {
        return new CoderProperties().setColumnType(ColumnType.TIME).setName(name).setTimeFormat(timeFormat);
    }
}
