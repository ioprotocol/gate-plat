package com.foton.buffer.protocol.builder;

import com.foton.buffer.protocol.type.ByteOrder;
import com.foton.buffer.protocol.type.CoderProperties;
import com.foton.buffer.protocol.type.ColumnType;
import com.foton.buffer.protocol.type.ParasiticProperties;

public class DecimalCoderBuild {
    private CoderProperties properties;

    public static CoderProperties build(String name, int length, double precision, int digit) {
        return new CoderProperties().setName(name).setLength(length).setPrecision(precision).setDigit(digit);
    }

    public DecimalCoderBuild(String name, int length, double precision, int digit) {
        properties = new CoderProperties().setColumnType(ColumnType.DECIMAL);
        properties.setName(name).setLength(length).setPrecision(precision).setDigit(digit);
    }

    public DecimalCoderBuild enableBCD() {
        properties.setBCD(true);
        return this;
    }

    public DecimalCoderBuild enableSigned() {
        properties.setUnsigned(false);
        return this;
    }

    public DecimalCoderBuild setOffset(double offset) {
        properties.setOffset(offset);
        return this;
    }

    public DecimalCoderBuild setDefaultValue(double defaultValue) {
        properties.setDefaultValue(defaultValue);
        return this;
    }

    public DecimalCoderBuild setInvalidValue(long invalidValue) {
        properties.setInvalidValue(invalidValue);
        return this;
    }

    public DecimalCoderBuild enableMask() {
        properties.setMask(true);
        return this;
    }

    public DecimalCoderBuild setByteOrder(ByteOrder byteOrder) {
        properties.setByteOrder(byteOrder);
        return this;
    }

    public DecimalCoderBuild setParasiticProperties(ParasiticProperties parasiticProperties) {
        properties.setParasiticProperties(parasiticProperties);
        return this;
    }

    public CoderProperties build() {
        return properties;
    }
}
