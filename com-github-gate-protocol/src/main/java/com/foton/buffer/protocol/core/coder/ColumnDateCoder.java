package com.foton.buffer.protocol.core.coder;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

import com.foton.buffer.protocol.core.ProtocolContext;
import com.foton.buffer.protocol.exception.TimeFormatNotFoundException;
import com.foton.buffer.protocol.type.CoderProperties;
import com.foton.buffer.protocol.utils.BCDUtil;

import io.netty.buffer.ByteBuf;
import io.vertx.core.json.JsonObject;

public class ColumnDateCoder extends ColumnCoder {
	public ColumnDateCoder(CoderProperties properties) {
		super(properties);
	}

	@Override
	protected JsonObject doDecode(ByteBuf byteBuf, JsonObject jsonObject, ProtocolContext protocolContext) {
		long ts = 0;
		switch (properties.getTimeFormat()) {
		case UTC: {
			ts = readValue(byteBuf, 8, properties.getByteOrder());
			if (properties.getInvalidValue() != null) {
				if (ts == ((Number) properties.getInvalidValue()).longValue()) {
					return jsonObject;
				}
			}
			break;
		}
		case JTT808: {
			ts = readValue(byteBuf, 3, properties.getByteOrder());
			byteBuf.readerIndex(byteBuf.readerIndex() - 3);
			if (properties.getInvalidValue() != null) {
				if (ts == ((Number) properties.getInvalidValue()).longValue()) {
					return jsonObject;
				}
			}
			LocalDateTime localDateTime = LocalDateTime.of(BCDUtil.bcd2value(byteBuf.readByte()) + 2000, BCDUtil.bcd2value(byteBuf.readByte()), BCDUtil.bcd2value(byteBuf.readByte()), 0, 0, 0, 0);
			ts = localDateTime.toEpochSecond(ZoneOffset.of("+8")) * 1000;
			break;
		}
		case GBT32960: {
			ts = readValue(byteBuf, 3, properties.getByteOrder());
			byteBuf.readerIndex(byteBuf.readerIndex() - 3);
			if (properties.getInvalidValue() != null) {
				if (ts == ((Number) properties.getInvalidValue()).longValue()) {
					return jsonObject;
				}
			}
			LocalDateTime localDateTime = LocalDateTime.of(byteBuf.readByte() + 2000, byteBuf.readByte(), byteBuf.readByte(), 0, 0, 0, 0);
			ts = localDateTime.toEpochSecond(ZoneOffset.of("+8")) * 1000;
			break;
		}
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
		Long ts = jsonObject.getLong(properties.getName());
		if (ts == null) {
			if (properties.getDefaultValue() != null) {
				ts = ((Number) properties.getDefaultValue()).longValue();
			}
		}

		long invalidValue = -1L;
		if (properties.getInvalidValue() != null) {
			invalidValue = ((Number) properties.getInvalidValue()).longValue();
		}

		switch (properties.getTimeFormat()) {
		case GBT32960: {
			if (ts == null) {
				writeValue(byteBuf, 3, invalidValue, properties.getByteOrder());
				return byteBuf;
			}
			LocalDateTime localDateTime = LocalDateTime.ofEpochSecond(ts / 1000, 0, ZoneOffset.of("+8"));
			byteBuf.writeByte(localDateTime.getYear() - 2000);
			byteBuf.writeByte(localDateTime.getMonthValue());
			byteBuf.writeByte(localDateTime.getDayOfMonth());
			break;
		}
		case JTT808: {
			if (ts == null) {
				writeValue(byteBuf, 3, invalidValue, properties.getByteOrder());
				return byteBuf;
			}
			LocalDateTime localDateTime = LocalDateTime.ofEpochSecond(ts / 1000, 0, ZoneOffset.of("+8"));
			byteBuf.writeByte(BCDUtil.value2bcd(localDateTime.getYear() - 2000));
			byteBuf.writeByte(BCDUtil.value2bcd(localDateTime.getMonthValue()));
			byteBuf.writeByte(BCDUtil.value2bcd(localDateTime.getDayOfMonth()));
			break;
		}
		case UTC: {
			if (ts == null) {
				ts = invalidValue;
			}
			writeValue(byteBuf, 8, ts, properties.getByteOrder());
			break;
		}
		default:
			throw new TimeFormatNotFoundException(properties);
		}
		return byteBuf;
	}
}
