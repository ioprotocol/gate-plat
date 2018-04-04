package com.foton.buffer.protocol.core.coder;

import com.foton.buffer.protocol.core.ProtocolContext;
import com.foton.buffer.protocol.type.CoderProperties;
import io.netty.buffer.ByteBuf;
import io.vertx.core.json.JsonObject;

public class ColumnBufferCoder extends ColumnCoder {
	public ColumnBufferCoder(CoderProperties properties) {
		super(properties);
	}

	@Override
	protected JsonObject doDecode(ByteBuf byteBuf, JsonObject jsonObject, ProtocolContext protocolContext) {
		int length = readLength(byteBuf);
		if (length > 0) {
			byte[] buf = new byte[length];
			byteBuf.readBytes(buf);
			if (!properties.isMask()) {
				jsonObject.put(properties.getName(), buf);
			}
		}
		return jsonObject;
	}

	@Override
	protected ByteBuf doEncode(ByteBuf byteBuf, JsonObject jsonObject, ProtocolContext protocolContext) {
		byte[] buf = jsonObject.getBinary(properties.getName(), (byte[]) properties.getDefaultValue());
		if (buf == null) {
			writeLength(byteBuf, 0);
		} else {
			writeLength(byteBuf, buf.length);
			byteBuf.writeBytes(buf);
		}
		return byteBuf;
	}
}
