package com.foton.buffer.protocol.core.coder;

import com.foton.buffer.protocol.core.ProtocolContext;
import com.foton.buffer.protocol.exception.BitSetLengthOutofRangeException;
import com.foton.buffer.protocol.type.BitProperties;
import com.foton.buffer.protocol.type.CoderProperties;

import io.netty.buffer.ByteBuf;
import io.vertx.core.json.JsonObject;

public class ColumnBitCoder extends ColumnCoder {
	public ColumnBitCoder(CoderProperties properties) {
		super(properties);
	}

	@Override
	protected JsonObject doDecode(ByteBuf byteBuf, JsonObject jsonObject, ProtocolContext protocolContext) {
		long value = readValue(byteBuf, properties.getLength(), properties.getByteOrder());
		for (BitProperties bitProperties : properties.getBitProperties()) {
			int tmp = 0;
			for (int i = 0; i < bitProperties.getLength(); i++) {
				tmp = tmp | ((int) (value & 0x01) << i);
				value = value >>> 1;
			}
			if (!bitProperties.isMask()) {
				if (bitProperties.getInvalidValue() == null) {
					jsonObject.put(bitProperties.getName(), tmp);
				} else {
					if (bitProperties.getInvalidValue() != tmp) {
						jsonObject.put(bitProperties.getName(), tmp);
					}
				}
			}
		}
		return jsonObject;
	}

	@Override
	protected ByteBuf doEncode(ByteBuf byteBuf, JsonObject jsonObject, ProtocolContext protocolContext) {
		long value = 0;
		int index = 0;
		for (BitProperties bitProperties : properties.getBitProperties()) {
			if (bitProperties.getLength() > 32) {
				throw new BitSetLengthOutofRangeException(bitProperties);
			}
			int invaidValue = bitProperties.getDefaultValue() != null ? bitProperties.getDefaultValue() : (bitProperties.getInvalidValue() != null ? bitProperties.getInvalidValue() : -1);
			int tmp = jsonObject.getInteger(bitProperties.getName(), invaidValue);
			int tmp1 = tmp << (32 - bitProperties.getLength());
			tmp1 = tmp1 >>> (32 - bitProperties.getLength());
			value = value | (tmp1 << index);
			index += bitProperties.getLength();
		}

		writeValue(byteBuf, properties.getLength(), value, properties.getByteOrder());
		return byteBuf;
	}

}
