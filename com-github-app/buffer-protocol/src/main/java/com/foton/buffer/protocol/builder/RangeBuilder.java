package com.foton.buffer.protocol.builder;

import com.foton.buffer.protocol.type.ByteOrder;
import com.foton.buffer.protocol.type.CoderProperties;
import com.foton.buffer.protocol.type.RangeProperties;

public class RangeBuilder {
    private RangeProperties properties;

	public static RangeProperties buildByIndex(int start, int end) {
		return new RangeProperties().setStart(start).setEnd(end);
	}

	public static RangeProperties buildByStartIndexAndEndOffset(int start, int endOffset) {
		return new RangeProperties().setStart(start).setEndOffset(endOffset);
	}

	public static RangeProperties buildByStartAndLength(int start, int length) {
		return new RangeProperties().setStart(start).setLength(length);
	}

	public static RangeProperties buildByEndAndLength(int end, int length) {
		return new RangeProperties().setEnd(end).setLength(length);
	}

	public static RangeProperties buildByOffset(int startOffset, int endOffset) {
		return new RangeProperties().setStartOffset(startOffset).setEndOffset(endOffset);
	}

	public static RangeProperties buildByVariedField(int startIndex, int startLength, int endIndex, int endLength) {
		CoderProperties startProperties = null;
		IntegerCoderBuilder builder1 = new IntegerCoderBuilder(null, startLength);
		startProperties = builder1.build();

		IntegerCoderBuilder builder2 = new IntegerCoderBuilder(null, endLength);

		CoderProperties endProperties = builder2.build();
		return new RangeProperties().setStartIndex(startIndex).setEndIndex(endIndex).setStartProperties(startProperties).setEndProperties(endProperties);
	}

	public RangeBuilder() {
		properties = new RangeProperties();
	}

	public RangeBuilder setStart(int start) {
		properties.setStart(start);
		return this;
	}

	public RangeBuilder setEnd(int end) {
		properties.setEnd(end);
		return this;
	}

	public RangeBuilder setLength(int length) {
		properties.setLength(length);
		return this;
	}

	public RangeBuilder setStartOffset(int offset) {
		properties.setStartOffset(offset);
		return this;
	}

	public RangeBuilder setEndOffset(int offset) {
		properties.setEndOffset(offset);
		return this;
	}

	public RangeBuilder setStartField(int index, int fieldLength) {
		properties.setStartIndex(index);
		properties.setStartProperties(IntegerCoderBuilder.build(null, fieldLength));
		return this;
	}

	public RangeBuilder setStartFieldOffset(int offset) {
		properties.getStartProperties().setOffset(offset);
		return this;
	}

	public RangeBuilder enableStartFieldLittleEndian() {
		properties.getStartProperties().setByteOrder(ByteOrder.LITTLE_ENDIAN);
		return this;
	}

	public RangeBuilder enableStartFieldBCD() {
		properties.getStartProperties().setBCD(true);
		return this;
	}

	public RangeBuilder setEndField(int index, int fieldLength) {
		properties.setEndIndex(index);
		properties.setEndProperties(IntegerCoderBuilder.build(null, fieldLength));
		return this;
	}

	public RangeBuilder setEndFieldOffset(int offset) {
		properties.getEndProperties().setOffset(offset);
		return this;
	}

	public RangeBuilder enableEndFieldLittleEndian() {
		properties.getEndProperties().setByteOrder(ByteOrder.LITTLE_ENDIAN);
		return this;
	}

	public RangeBuilder enableEndFieldBCD() {
		properties.getEndProperties().setBCD(true);
		return this;
	}
}
