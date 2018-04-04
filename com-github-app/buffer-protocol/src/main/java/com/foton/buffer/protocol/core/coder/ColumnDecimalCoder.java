package com.foton.buffer.protocol.core.coder;

import com.foton.buffer.protocol.core.ProtocolContext;
import com.foton.buffer.protocol.type.CoderProperties;
import com.foton.buffer.protocol.utils.BCDUtil;
import io.netty.buffer.ByteBuf;
import io.vertx.core.json.JsonObject;

import java.math.BigDecimal;

public class ColumnDecimalCoder extends ColumnCoder {
	public ColumnDecimalCoder(CoderProperties properties) {
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

			double dvalue = value;
			if (properties.getPrecision() != null) {
				dvalue = dvalue * properties.getPrecision().doubleValue();
			}

			if (properties.getOffset() != null) {
				dvalue = dvalue - properties.getOffset().doubleValue();
			}

			if (properties.getDigit() != null) {
				dvalue = new BigDecimal(dvalue).setScale(properties.getDigit(), BigDecimal.ROUND_HALF_UP).doubleValue();
			}

			if (!properties.isMask()) {
				jsonObject.put(properties.getName(), dvalue);
			}
		}
		return jsonObject;
	}

	@Override
	protected ByteBuf doEncode(ByteBuf byteBuf, JsonObject jsonObject, ProtocolContext protocolContext) {
		Double value = jsonObject.getDouble(properties.getName());

		if (value == null) {
			if (properties.getDefaultValue() != null) {
				value = ((Number) properties.getDefaultValue()).doubleValue();
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

		if (properties.getOffset() != null) {
			value = value + properties.getOffset().doubleValue();
		}

		if (properties.getPrecision() != null) {
			value = value / properties.getPrecision().doubleValue();
		}

		long val = value.longValue();
		if (properties.isBCD()) {
			val = BCDUtil.value2bcd(val);
		}

		writeValue(byteBuf, properties.getLength(), val, properties.getByteOrder());
		return byteBuf;
	}
}
