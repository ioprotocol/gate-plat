package com.foton.buffer.protocol.builder;

import com.foton.buffer.protocol.exception.BitSetLengthNotMatchException;
import com.foton.buffer.protocol.type.BitProperties;
import com.foton.buffer.protocol.type.ByteOrder;
import com.foton.buffer.protocol.type.CoderProperties;
import com.foton.buffer.protocol.type.ColumnType;

public class BitSetBuilder {
    private CoderProperties properties;

    public static CoderProperties buildBitSet(int length, ByteOrder byteOrder, BitProperties[] bitProperties) {
        return check(new CoderProperties().setColumnType(ColumnType.BIT).setLength(length).setByteOrder(byteOrder).setBitProperties(bitProperties));
    }

    public static CoderProperties buildBitSet(int length, BitProperties[] bitProperties) {
        return check(new CoderProperties().setColumnType(ColumnType.BIT).setLength(length).setBitProperties(bitProperties));
    }

    public BitSetBuilder(int length) {
        properties = new CoderProperties().setColumnType(ColumnType.BIT).setLength(length);
    }

    public BitSetBuilder setByteOrder(ByteOrder byteOrder) {
        properties.setByteOrder(byteOrder);
        return this;
    }

    public BitSetBuilder addBit(BitProperties bitProperties) {
        if (properties.getBitProperties() == null) {
            properties.setBitProperties(new BitProperties[]{bitProperties});
        } else {
            BitProperties[] a = new BitProperties[properties.getBitProperties().length + 1];
            System.arraycopy(properties.getBitProperties(), 0, a, 0, a.length -1);
            a[a.length - 1] = bitProperties;
            properties.setBitProperties(a);
        }
        return this;
    }

    public BitSetBuilder addReferenceBit(String moduleName, BitProperties bitProperties) {
        bitProperties.setModuleLengthField(true);
        addBit(bitProperties);
        properties.setModuleName(moduleName);
        properties.setModuleLengthField(true);
        return this;
    }

    public CoderProperties build() {
        return check(properties);
    }

    private static CoderProperties check(CoderProperties coderProperties) {
        int bitLength = 0;
        for (BitProperties bitProperties : coderProperties.getBitProperties()) {
            bitLength += bitProperties.getLength();
        }

        if (bitLength/8 != coderProperties.getLength()) {
            throw new BitSetLengthNotMatchException(coderProperties);
        }
        return coderProperties;
    }
}
