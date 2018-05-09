package com.github.gate.protocol.builder;

import com.github.gate.protocol.type.CoderProperties;
import com.github.gate.protocol.type.ColumnType;
import com.github.gate.protocol.type.LengthProperties;

public class ArrayBuilder {
    public static CoderProperties buildArray(String name, int length, CoderProperties itemProperties, boolean isMask) {
        return new CoderProperties().setColumnType(ColumnType.ARRAY).setName(name).setLength(length).setItemProperties(itemProperties).setMask(isMask);
    }

    public static CoderProperties buildArray(String name, int length, CoderProperties itemProperties) {
        return new CoderProperties().setColumnType(ColumnType.ARRAY).setName(name).setLength(length).setItemProperties(itemProperties);
    }

    public static CoderProperties buildArray(String name, LengthProperties lengthProperties, CoderProperties itemProperties, boolean isMask) {
        return new CoderProperties().setColumnType(ColumnType.ARRAY).setName(name).setLengthProperties(lengthProperties).setItemProperties(itemProperties).setMask(isMask);
    }

    public static CoderProperties buildArray(String name, LengthProperties lengthProperties, CoderProperties itemProperties) {
        return new CoderProperties().setColumnType(ColumnType.ARRAY).setName(name).setLengthProperties(lengthProperties).setItemProperties(itemProperties);
    }
}
