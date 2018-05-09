package com.github.gate.protocol.core.coder;

import com.github.gate.protocol.core.ICoder;
import com.github.gate.protocol.core.ProtocolContext;
import com.github.gate.protocol.type.CoderProperties;
import io.netty.buffer.ByteBuf;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

public class ArrayCoder extends ColumnCoder {
	private ICoder coder;

	public ArrayCoder(CoderProperties properties) {
		super(properties);
		switch (properties.getItemProperties().getColumnType()) {
		case DATETIME:
			coder = new ColumnDateTimeCoder(properties.getItemProperties());
			break;
		case DATE:
			coder = new ColumnDateCoder(properties.getItemProperties());
			break;
		case TIME:
			coder = new ColumnTimeCoder(properties.getItemProperties());
			break;
		case BIT:
			coder = new ColumnBitCoder(properties.getItemProperties());
			break;
		case BUFFER:
			coder = new ColumnBufferCoder(properties.getItemProperties());
			break;
		case STRING:
			coder = new ColumnStringCoder(properties.getItemProperties());
			break;
		case DECIMAL:
			coder = new ColumnDecimalCoder(properties.getItemProperties());
			break;
		case INTEGER:
			coder = new ColumnIntegerCoder(properties.getItemProperties());
			break;
		case REFERENCE:
			coder = new ModuleReferenceCoder(properties.getItemProperties());
			break;
		}
	}

	@Override
	protected JsonObject doDecode(ByteBuf byteBuf, JsonObject jsonObject, ProtocolContext protocolContext) {
		int length = readLength(byteBuf);
		JsonArray jsonArray = new JsonArray();
		if (coder instanceof ModuleReferenceCoder) {
			for (int i = 0; i < length; i++) {
				jsonArray.add(coder.decode(byteBuf, new JsonObject(), protocolContext));
			}
		} else if (coder instanceof ColumnBitCoder) {
			for (int i = 0; i < length; i++) {
				jsonArray.add(coder.decode(byteBuf, new JsonObject(), protocolContext));
			}
		} else { // 只要不是引用，解析的都是基本数据类型
			JsonObject tmpObj = new JsonObject();
			for (int i = 0; i < length; i++) {
				tmpObj = coder.decode(byteBuf, tmpObj, protocolContext);
				jsonArray.add(tmpObj.getValue(properties.getItemProperties().getName()));
				tmpObj.clear();
			}
		}

		if (!properties.isMask()) {
			jsonObject.put(properties.getName(), jsonArray);
		}

		return jsonObject;
	}

	@Override
	protected ByteBuf doEncode(ByteBuf byteBuf, JsonObject jsonObject, ProtocolContext protocolContext) {
		JsonArray jsonArray = jsonObject.getJsonArray(properties.getName());
		writeLength(byteBuf, jsonArray.size());
		if (coder instanceof ModuleReferenceCoder) {
		    for(int i = 0; i < jsonArray.size(); i++) {
				coder.encode(byteBuf, jsonArray.getJsonObject(i), protocolContext);
			}
		} else if (coder instanceof ColumnBitCoder) {
			for(int i = 0; i < jsonArray.size(); i++) {
				coder.encode(byteBuf, jsonArray.getJsonObject(i), protocolContext);
            }
        } else {
			JsonObject tmpObj = new JsonObject();
			for(int i = 0; i < jsonArray.size(); i++) {
				tmpObj.put(properties.getItemProperties().getName(), jsonArray.getValue(i));
				coder.encode(byteBuf, jsonArray.getJsonObject(i), protocolContext);
				tmpObj.clear();
            }
		}
		return byteBuf;
	}

}
