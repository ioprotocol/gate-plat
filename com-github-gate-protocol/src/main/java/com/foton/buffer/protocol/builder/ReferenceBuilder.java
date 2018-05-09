package com.foton.buffer.protocol.builder;

import com.foton.buffer.protocol.type.*;

public class ReferenceBuilder {
    public static CoderProperties buildHardReference(String name, String group, String moduleName) {
        return new CoderProperties().setColumnType(ColumnType.REFERENCE).setReference(Reference.HARD).setName(name).setGroupName(group).setModuleName(moduleName);
    }

    public static CoderProperties buildLoopReference(String name, String group, int length) {
        return new CoderProperties().setColumnType(ColumnType.REFERENCE).setReference(Reference.LOOP).setName(name).setGroupName(group).setLength(length);
    }

    public static CoderProperties buildLoopReference(String name, String group, LengthProperties lengthProperties) {
        return new CoderProperties().setColumnType(ColumnType.REFERENCE).setReference(Reference.LOOP).setName(name).setGroupName(group).setLengthProperties(lengthProperties);
    }

    public static CoderProperties buildSoftReference(String name, String group, ParasiticProperties parasiticProperties) {
        return new CoderProperties().setColumnType(ColumnType.REFERENCE).setReference(Reference.SOFT).setName(name).setGroupName(group).setParasiticProperties(parasiticProperties);
    }

    public static CoderProperties buildUntilLoopReference(String name, String group, int length) {
        return new CoderProperties().setColumnType(ColumnType.REFERENCE).setReference(Reference.UNTIL_LOOP).setName(name).setGroupName(group).setLength(length);
    }
}
