package com.foton.buffer.protocol.core.coder;

import com.foton.buffer.protocol.core.ProtocolContext;
import com.foton.buffer.protocol.type.CoderProperties;
import com.foton.buffer.protocol.utils.BCDUtil;

import io.netty.buffer.ByteBuf;
import io.vertx.core.json.JsonObject;

public class ColumnIntegerCoder extends ColumnCoder {
	public ColumnIntegerCoder(CoderProperties properties) {
		super(properties);
	}

	@Override
	protected JsonObject doDecode(ByteBuf byteBuf, JsonObject jsonObject, ProtocolContext protocolContext) {
		long value = readValue(byteBuf, properties.getLength(), properties.getByteOrder());

		if (isValid(value, (Number) properties.getInvalidValue(), properties.getLength())) {
			if (properties.isBCD()) {
				value = BCDUtil.bcd2value(value);
			}

			if (!properties.isUnsigned()) {
				value = toSigned(value, properties.getLength());
			}

			if (properties.getPrecision() != null) {
				value = value * properties.getPrecision().longValue();
			}

			if (properties.getOffset() != null) {
				value = value - properties.getOffset().intValue();
			}

			if (!properties.isMask()) {
				if (properties.getLength() <= 4) {
					jsonObject.put(properties.getName(), (int) value);
				} else {
					jsonObject.put(properties.getName(), value);
				}
			}
		}
		return jsonObject;
	}

	@Override
	protected ByteBuf doEncode(ByteBuf byteBuf, JsonObject jsonObject, ProtocolContext protocolContext) {
		Long value = jsonObject.getLong(properties.getName(), null);
		if (value == null) {
			if (properties.getDefaultValue() != null) {
				value = ((Number) properties.getDefaultValue()).longValue();
			}
		}

		long invalidValue = -1L;
		if (properties.getInvalidValue() != null) {
			invalidValue = ((Number) properties.getInvalidValue()).longValue();
		}

		if (value == null) {
			writeValue(byteBuf, properties.getLength(), invalidValue, properties.getByteOrder());
			return byteBuf;
		}

		if (properties.getPrecision() != null) {
			value = value / properties.getPrecision().longValue();
		}

		if (properties.getOffset() != null) {
			value = value + properties.getOffset().intValue();
		}

		if (properties.isBCD()) {
			value = BCDUtil.value2bcd(value);
		}

		writeValue(byteBuf, properties.getLength(), value, properties.getByteOrder());
		return byteBuf;
	}

}
