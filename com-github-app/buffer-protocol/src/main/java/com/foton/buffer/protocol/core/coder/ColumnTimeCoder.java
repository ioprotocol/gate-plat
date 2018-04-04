package com.foton.buffer.protocol.core.coder;

import java.time.LocalTime;

import com.foton.buffer.protocol.core.ProtocolContext;
import com.foton.buffer.protocol.exception.TimeFormatNotFoundException;
import com.foton.buffer.protocol.type.CoderProperties;
import com.foton.buffer.protocol.utils.BCDUtil;

import io.netty.buffer.ByteBuf;
import io.vertx.core.json.JsonObject;

public class ColumnTimeCoder extends ColumnCoder {
	public ColumnTimeCoder(CoderProperties properties) {
		super(properties);
	}

	@Override
	protected JsonObject doDecode(ByteBuf byteBuf, JsonObject jsonObject, ProtocolContext protocolContext) {
		int ts = 0;
		switch (properties.getTimeFormat()) {
		case UTC: {
			ts = (int) readValue(byteBuf, 4, properties.getByteOrder());
			if (properties.getInvalidValue() != null) {
				if (ts == ((Number) properties.getInvalidValue()).intValue()) {
					return jsonObject;
				}
			}
			break;
		}
		case JTT808: {
			ts = (int) readValue(byteBuf, 3, properties.getByteOrder());
			byteBuf.readerIndex(byteBuf.readerIndex() - 3);
			if (properties.getInvalidValue() != null) {
				if (ts == ((Number) properties.getInvalidValue()).intValue()) {
					return jsonObject;
				}
			}
			LocalTime localTime = LocalTime.of(BCDUtil.bcd2value(byteBuf.readByte()), BCDUtil.bcd2value(byteBuf.readByte()), BCDUtil.bcd2value(byteBuf.readByte()));
			ts = localTime.toSecondOfDay();
			break;
		}
		case GBT32960:
			ts = (int) readValue(byteBuf, 3, properties.getByteOrder());
			byteBuf.readerIndex(byteBuf.readerIndex() - 3);
			if (properties.getInvalidValue() != null) {
				if (ts == ((Number) properties.getInvalidValue()).intValue()) {
					return jsonObject;
				}
			}
			LocalTime localTime = LocalTime.of(byteBuf.readByte(), byteBuf.readByte(), byteBuf.readByte());
			ts = localTime.toSecondOfDay();
			break;
		default:
			throw new TimeFormatNotFoundException(properties);
		}

		if (!properties.isMask()) {
			jsonObject.put(properties.getName(), ts);
		}
		return jsonObject;
	}

	@Override
	protected ByteBuf doEncode(ByteBuf byteBuf, JsonObject jsonObject, ProtocolContext protocolContext) {
		Integer ts = jsonObject.getInteger(properties.getName());

		if (ts == null) {
			if (properties.getDefaultValue() != null) {
				ts = ((Number) properties.getDefaultValue()).intValue();
			}
		}

		int invalidValue = -1;
		if (properties.getInvalidValue() != null) {
			invalidValue = ((Number) properties.getInvalidValue()).intValue();
		}

		switch (properties.getTimeFormat()) {
		case UTC: {
			if (ts == null) {
				ts = invalidValue;
			}
			writeValue(byteBuf, 4, ts, properties.getByteOrder());
			break;
		}
		case JTT808: {
			if (ts == null) {
				writeValue(byteBuf, 3, invalidValue, properties.getByteOrder());
				return byteBuf;
			}
			LocalTime localTime = LocalTime.ofSecondOfDay(ts);
			byteBuf.writeByte(BCDUtil.value2bcd(localTime.getHour()));
			byteBuf.writeByte(BCDUtil.value2bcd(localTime.getMinute()));
			byteBuf.writeByte(BCDUtil.value2bcd(localTime.getSecond()));
			break;
		}
		case GBT32960:
			if (ts == null) {
				writeValue(byteBuf, 3, invalidValue, properties.getByteOrder());
				return byteBuf;
			}
			LocalTime localTime = LocalTime.ofSecondOfDay(ts);
			byteBuf.writeByte(localTime.getHour());
			byteBuf.writeByte(localTime.getMinute());
			byteBuf.writeByte(localTime.getSecond());
			break;
		default:
			throw new TimeFormatNotFoundException(properties);
		}
		return byteBuf;
	}
}
