package com.foton.buffer.protocol.builder;

import com.foton.buffer.protocol.type.CoderProperties;
import com.foton.buffer.protocol.type.ColumnType;
import com.foton.buffer.protocol.type.LengthProperties;

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
