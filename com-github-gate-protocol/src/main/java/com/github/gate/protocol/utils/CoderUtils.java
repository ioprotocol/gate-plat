package com.github.gate.protocol.utils;

import com.github.gate.protocol.type.ByteOrder;
import com.github.gate.protocol.type.CoderProperties;
import io.netty.buffer.ByteBuf;

public class CoderUtils {

	public static int getUnsignedInteger(ByteBuf byteBuf, int index, CoderProperties properties) {
		int value = 0;
		switch (properties.getLength()) {
		case 1:
			value = byteBuf.getUnsignedByte(index);
			break;
		case 2:
			if (properties.getByteOrder() == ByteOrder.BIG_ENDIAN) {
				value = byteBuf.getUnsignedShort(index);
			} else {
				value = byteBuf.getUnsignedShortLE(index);
			}
			break;
		case 3:
			if (properties.getByteOrder() == ByteOrder.BIG_ENDIAN) {
				value = byteBuf.getUnsignedMedium(index);
			} else {
				value = byteBuf.getUnsignedMediumLE(index);
			}
			break;
		case 4:
			if (properties.getByteOrder() == ByteOrder.BIG_ENDIAN) {
				value = byteBuf.getInt(index);
			} else  {
				value = byteBuf.getIntLE(index);
			}
			break;
		default:
			throw new RuntimeException("un support operation for getInteger");
		}

		if (properties.isBCD()) {
			value = BCDUtil.bcd2value(value);
		}

		if (properties.getOffset() != null) {
			value = value + properties.getOffset().intValue();
		}

		return value;
	}
}
