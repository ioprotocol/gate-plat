package com.foton.buffer.protocol.builder;

import com.foton.buffer.protocol.type.ByteOrder;
import com.foton.buffer.protocol.type.CoderProperties;
import com.foton.buffer.protocol.type.ColumnType;
import com.foton.buffer.protocol.type.ParasiticProperties;

public class IntegerCoderBuilder {

	public static CoderProperties build(String name, int length) {
		return new CoderProperties().setColumnType(ColumnType.INTEGER).setName(name).setLength(length);
	}

	public static CoderProperties build(String name, int length, long offset) {
		return new CoderProperties().setColumnType(ColumnType.INTEGER).setName(name).setLength(length).setOffset(offset);
	}

	public static CoderProperties build(String name, int length, boolean isBCD) {
		return new CoderProperties().setColumnType(ColumnType.INTEGER).setName(name).setLength(length).setBCD(isBCD);
	}

	public static CoderProperties build(String name, int length, ByteOrder byteOrder) {
		return new CoderProperties().setColumnType(ColumnType.INTEGER).setName(name).setLength(length).setByteOrder(byteOrder);
	}

	public static CoderProperties build(String name, int length, long offset, boolean isBCD) {
		return new CoderProperties().setColumnType(ColumnType.INTEGER).setName(name).setLength(length).setOffset(offset).setBCD(isBCD);
	}

	public static CoderProperties build(String name, int length, long offset, ByteOrder byteOrder) {
		return new CoderProperties().setColumnType(ColumnType.INTEGER).setName(name).setLength(length).setOffset(offset).setByteOrder(byteOrder);
	}

	public static CoderProperties build(String name, int length, long offset, boolean isBCD, ByteOrder byteOrder) {
		return new CoderProperties().setColumnType(ColumnType.INTEGER).setName(name).setLength(length).setOffset(offset).setBCD(isBCD).setByteOrder(byteOrder);
	}

	private CoderProperties properties;

	public IntegerCoderBuilder(String name, int length) {
		properties = new CoderProperties().setColumnType(ColumnType.INTEGER);
		properties.setName(name).setLength(length);
	}

	public IntegerCoderBuilder isReferenceLengthField(String referenceName) {
		properties.setModuleName(referenceName).setModuleLengthField(true);
		return this;
	}

	public IntegerCoderBuilder enableBCD() {
		properties.setBCD(true);
		return this;
	}

	public IntegerCoderBuilder enableSigned() {
		properties.setUnsigned(false);
		return this;
	}

	public IntegerCoderBuilder setOffset(long offset) {
		properties.setOffset(offset);
		return this;
	}

	public IntegerCoderBuilder setDefaultValue(long defaultValue) {
		properties.setDefaultValue(defaultValue);
		return this;

	}

	public IntegerCoderBuilder setPrecition(long precition) {
		properties.setPrecision(precition);
		return this;
	}

	public IntegerCoderBuilder setInvalidValue(long invalidValue) {
		properties.setInvalidValue(invalidValue);
		return this;
	}

	public IntegerCoderBuilder enableMask() {
		properties.setMask(true);
		return this;
	}

	public IntegerCoderBuilder setByteOrder(ByteOrder byteOrder) {
		properties.setByteOrder(byteOrder);
		return this;
	}

	public IntegerCoderBuilder setParasiticProperties(ParasiticProperties parasiticProperties) {
		properties.setParasiticProperties(parasiticProperties);
		return this;
	}

	public CoderProperties build() {
		return properties;
	}
}
