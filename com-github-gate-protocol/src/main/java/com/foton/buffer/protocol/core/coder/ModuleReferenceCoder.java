package com.foton.buffer.protocol.core.coder;

import java.util.Map;

import com.foton.buffer.protocol.core.ICoder;
import com.foton.buffer.protocol.core.ProtocolContext;
import com.foton.buffer.protocol.exception.ModuleIDConfigException;
import com.foton.buffer.protocol.exception.ModuleNotFoundException;
import com.foton.buffer.protocol.exception.ReferenceEncodeMissingArgsException;
import com.foton.buffer.protocol.exception.ReferenceTypeNotFoundException;
import com.foton.buffer.protocol.type.CoderProperties;
import com.foton.buffer.protocol.type.LengthProperties;
import com.foton.buffer.protocol.type.ModuleProperties;

import io.netty.buffer.ByteBuf;
import io.vertx.core.json.JsonObject;

public class ModuleReferenceCoder extends ColumnCoder {

	public ModuleReferenceCoder(CoderProperties properties) {
		super(properties);
	}

	@Override
	protected JsonObject doDecode(ByteBuf byteBuf, JsonObject jsonObject, ProtocolContext protocolContext) {
		switch (properties.getReference()) {
		case HARD: {
			JsonObject obj = protocolContext.getModuleCoder(properties.getGroupName(), properties.getModuleName()).decode(byteBuf, new JsonObject(), protocolContext);
			jsonObject.put(properties.getName(), obj);
			break;
		}
		case LOOP: {
			JsonObject obj = new JsonObject();
			int length = readLength(byteBuf);

			ModuleProperties moduleProperties = protocolContext.getGroupModuleProperties(properties.getGroupName());
			if (moduleProperties.isIdCodec()) {
				throw new ModuleIDConfigException(moduleProperties, properties);
			}
			for (int i = 0; i < length; i++) {
				int id = readLengthPropertiesValue(byteBuf, moduleProperties.getIdProperties());
				protocolContext.getModuleCoder(properties.getGroupName(), id).decode(byteBuf, obj, protocolContext);
			}
			jsonObject.put(properties.getName(), obj);
			break;
		}
		case UNTIL_LOOP: {
			ModuleProperties moduleProperties = protocolContext.getGroupModuleProperties(properties.getGroupName());
			if (moduleProperties.isIdCodec()) {
				throw new ModuleIDConfigException(moduleProperties, properties);
			}
			JsonObject obj = new JsonObject();
			while (byteBuf.writerIndex() - byteBuf.readerIndex() > properties.getLength()) {
				int id = readLengthPropertiesValue(byteBuf, moduleProperties.getIdProperties());
				ICoder coder = protocolContext.getModuleCoder(properties.getGroupName(), id);
				if (coder == null) {
					throw new ModuleNotFoundException(byteBuf, "not found module id:" + id, properties, obj, jsonObject);
				}
				coder.decode(byteBuf, obj, protocolContext);
			}
			jsonObject.put(properties.getName(), obj);
			break;
		}
		case SOFT: {
			int moduleId = jsonObject.getInteger(properties.getParasiticProperties().getName());
			JsonObject obj = protocolContext.getModuleCoder(properties.getGroupName(), moduleId).decode(byteBuf, new JsonObject(), protocolContext);
			jsonObject.put(properties.getName(), obj);
			break;
		}
		default:
			throw new ReferenceTypeNotFoundException(properties);
		}
		return jsonObject;
	}

	@Override
	protected ByteBuf doEncode(ByteBuf byteBuf, JsonObject jsonObject, ProtocolContext protocolContext) {
		switch (properties.getReference()) {
		case HARD: {
			if (jsonObject.getJsonObject(properties.getName()) == null) {
				throw new ReferenceEncodeMissingArgsException(properties.getName() + " missing", jsonObject);
			}
			protocolContext.getModuleCoder(properties.getGroupName(), properties.getModuleName()).encode(byteBuf, jsonObject.getJsonObject(properties.getName()), protocolContext);
			break;
		}
		case LOOP: {
			int writerIndex = byteBuf.writerIndex();
			writeLength(byteBuf, 0);
			int objSize = 0;
			// objs 可以为空，相当于模块数组的长度为0
			JsonObject objs = jsonObject.getJsonObject(properties.getName());
			if (objs != null) {
				for (Map.Entry<String, Object> entry : objs) {
					Integer id = protocolContext.getModuleId(properties.getGroupName(), entry.getKey());
					if (id == null) {
						logger.error("module group:" + properties.getGroupName() + " moduleName:" + entry.getKey() + " find no Module");
						continue;
					}
					LengthProperties idProperties = protocolContext.getGroupModuleProperties(properties.getGroupName()).getIdProperties();
					if (idProperties == null) {
						logger.error("module group:" + properties.getGroupName() + " find no module info");
						continue;
					}
					// write module id
					writeLengthPropertiesValue(byteBuf, id, idProperties);// write module length and module items
					protocolContext.getModuleCoder(properties.getGroupName(), entry.getKey()).encode(byteBuf, objs, protocolContext);
					objSize++;
				}
			}
			setLengthPropertiesValue(byteBuf, objSize, writerIndex, properties.getLengthProperties());
			break;
		}
		case UNTIL_LOOP: {
			// objs 可以为空，本身属于浮动模块
			JsonObject objs = jsonObject.getJsonObject(properties.getName());
			if (objs != null) {
				for (Map.Entry<String, Object> entry : objs) {
					Integer id = protocolContext.getModuleId(properties.getGroupName(), entry.getKey());
					if (id == null) {
						logger.error("module group:" + properties.getGroupName() + " moduleName:" + entry.getKey() + " find no Module");
						continue;
					}
					LengthProperties idProperties = protocolContext.getGroupModuleProperties(properties.getGroupName()).getIdProperties();
					if (idProperties == null) {
						logger.error("module group:" + properties.getGroupName() + " find no module info");
						continue;
					}
					// write module id
					writeLengthPropertiesValue(byteBuf, id, idProperties);
					// write module length and module items
					protocolContext.getModuleCoder(properties.getGroupName(), entry.getKey()).encode(byteBuf, objs, protocolContext);
				}
			}
			break;
		}
		case SOFT: {
			JsonObject obj = jsonObject.getJsonObject(properties.getName());
			if (obj == null) {
				throw new ReferenceEncodeMissingArgsException(properties.getName() + " missing", jsonObject);
			}
			for (Map.Entry<String, Object> entry : obj) {
				protocolContext.getModuleCoder(properties.getGroupName(), entry.getKey()).encode(byteBuf, obj, protocolContext);
			}
			break;
		}
		default:
			throw new ReferenceTypeNotFoundException(properties);
		}
		return byteBuf;
	}

}
