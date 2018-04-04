package com.foton.buffer.protocol.core.coder;

import com.foton.buffer.protocol.core.ProtocolContext;
import com.foton.buffer.protocol.type.CoderProperties;
import io.netty.buffer.ByteBuf;
import io.vertx.core.json.JsonObject;

public class ColumnStringCoder extends ColumnCoder {
	public ColumnStringCoder(CoderProperties properties) {
		super(properties);
	}

	@Override
	protected JsonObject doDecode(ByteBuf byteBuf, JsonObject jsonObject, ProtocolContext protocolContext) {
		int length = readLength(byteBuf);
		if (length > 0) {
			byte[] buf = new byte[length];
			byteBuf.readBytes(buf);
			if (!properties.isMask()) {
				jsonObject.put(properties.getName(), new String(buf, properties.getCharset()));
			}
		}
		return jsonObject;
	}

	@Override
	protected ByteBuf doEncode(ByteBuf byteBuf, JsonObject jsonObject, ProtocolContext protocolContext) {
		String msg = jsonObject.getString(properties.getName(), (String) properties.getDefaultValue());
		if (msg == null) {
			writeLength(byteBuf, 0);
		} else {
			byte[] buf = msg.getBytes(properties.getCharset());
			writeLength(byteBuf, buf.length);
			byteBuf.writeBytes(buf);
		}
		return byteBuf;
	}
}
