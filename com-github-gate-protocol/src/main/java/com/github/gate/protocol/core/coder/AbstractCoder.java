package com.github.gate.protocol.core.coder;

import com.github.gate.protocol.core.ICoder;
import com.github.gate.protocol.exception.LengthPropertiesException;
import com.github.gate.protocol.type.ByteOrder;
import com.github.gate.protocol.type.LengthProperties;

import io.netty.buffer.ByteBuf;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;

public abstract class AbstractCoder implements ICoder {
	protected static final Logger logger = LoggerFactory.getLogger(AbstractCoder.class);

	/**
	 * 读取长度字段
	 * 
	 * @param byteBuf
	 * @param properties
	 * @return
	 */
	protected int readLengthPropertiesValue(ByteBuf byteBuf, LengthProperties properties) {
		int length = 0;
		switch (properties.getLength()) {
		case 1:
			length = byteBuf.readUnsignedByte();
			break;
		case 2:
			if (properties.getByteOrder() == null || properties.getByteOrder() == ByteOrder.BIG_ENDIAN) {
				length = byteBuf.readUnsignedShort();
			} else {
				length = byteBuf.readUnsignedShortLE();
			}
			break;
		case 3:
			if (properties.getByteOrder() == null || properties.getByteOrder() == ByteOrder.BIG_ENDIAN) {
				length = byteBuf.readUnsignedMedium();
			} else {
				length = byteBuf.readUnsignedMediumLE();
			}
			break;
		case 4:
			if (properties.getByteOrder() == null || properties.getByteOrder() == ByteOrder.BIG_ENDIAN) {
				length = byteBuf.readInt();
			} else {
				length = byteBuf.readIntLE();
			}
			break;
		default:
			throw new LengthPropertiesException(properties);
		}

		return length;
	}

	/**
	 * 写入长度字段
	 * 
	 * @param byteBuf
	 * @param value
	 * @param properties
	 */
	protected void writeLengthPropertiesValue(ByteBuf byteBuf, Integer value, LengthProperties properties) {
		switch (properties.getLength()) {
		case 1:
			byteBuf.writeByte(value);
			break;
		case 2:
			if (properties.getByteOrder() == null || properties.getByteOrder() == ByteOrder.BIG_ENDIAN) {
				byteBuf.writeShort(value);
			} else {
				byteBuf.writeShortLE(value);
			}
			break;
		case 3:
			if (properties.getByteOrder() == null || properties.getByteOrder() == ByteOrder.BIG_ENDIAN) {
				byteBuf.writeMedium(value);
			} else {
				byteBuf.writeMediumLE(value);
			}
			break;
		case 4:
			if (properties.getByteOrder() == null || properties.getByteOrder() == ByteOrder.BIG_ENDIAN) {
				byteBuf.writeInt(value);
			} else {
				byteBuf.writeIntLE(value);
			}
			break;
		default:
			throw new LengthPropertiesException(properties);
		}
	}

	protected void setLengthPropertiesValue(ByteBuf byteBuf, int value, int index, LengthProperties properties) {
		switch (properties.getLength()) {
		case 1:
			byteBuf.setByte(index, value);
			break;
		case 2:
			if (properties.getByteOrder() == null || properties.getByteOrder() == ByteOrder.BIG_ENDIAN) {
				byteBuf.setShort(index, value);
			} else {
				byteBuf.setShortLE(index, value);
			}
			break;
		case 3:
			if (properties.getByteOrder() == null || properties.getByteOrder() == ByteOrder.BIG_ENDIAN) {
				byteBuf.setMedium(index, value);
			} else {
				byteBuf.setMediumLE(index, value);
			}
			break;
		case 4:
			if (properties.getByteOrder() == null || properties.getByteOrder() == ByteOrder.BIG_ENDIAN) {
				byteBuf.setInt(index, value);
			} else {
				byteBuf.setIntLE(index, value);
			}
			break;
		default:
			throw new LengthPropertiesException(properties);
		}
	}
}
