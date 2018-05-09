package com.github.gate.protocol.core.coder;

import java.util.ArrayList;
import java.util.List;

import com.github.gate.protocol.core.ProtocolContext;
import com.github.gate.protocol.exception.ModuleCoderException;
import com.github.gate.protocol.exception.ModuleInitException;
import com.github.gate.protocol.exception.ProtocolRuntimeException;
import com.github.gate.protocol.type.CoderProperties;
import com.github.gate.protocol.type.ColumnType;
import com.github.gate.protocol.type.ModuleProperties;

import io.netty.buffer.ByteBuf;
import io.vertx.core.json.JsonObject;

public class ModuleCoder extends ModuleCoderFilter {
	private List<ColumnCoder> coders = new ArrayList<>();

	public ModuleCoder(ModuleProperties properties) {
		super(properties);
		for (CoderProperties p : properties.getColumnList()) {
			try {
				switch (p.getColumnType()) {
				case DATETIME:
					coders.add(new ColumnDateTimeCoder(p));
					break;
				case DATE:
					coders.add(new ColumnDateCoder(p));
					break;
				case TIME:
					coders.add(new ColumnTimeCoder(p));
					break;
				case BIT:
					coders.add(new ColumnBitCoder(p));
					break;
				case ARRAY:
					coders.add(new ArrayCoder(p));
					break;
				case BUFFER:
					coders.add(new ColumnBufferCoder(p));
					break;
				case STRING:
					coders.add(new ColumnStringCoder(p));
					break;
				case DECIMAL:
					coders.add(new ColumnDecimalCoder(p));
					break;
				case INTEGER:
					coders.add(new ColumnIntegerCoder(p));
					break;
				case REFERENCE:
					coders.add(new ModuleReferenceCoder(p));
					break;
				}
			} catch (Exception e) {
				throw new ModuleInitException(e, p, properties);
			}
		}
	}

	@Override
	public JsonObject doDecode(ByteBuf byteBuf, JsonObject jsonObject, ProtocolContext protocolContext) {
		JsonObject obj = null;
		if (properties.isFlat()) {
			obj = jsonObject;
		} else {
			obj = new JsonObject();
		}

		if (properties.isIdCodec()) {
			int id = readLengthPropertiesValue(byteBuf, properties.getIdProperties());
			if (!properties.isIdMask()) {
				obj.put("id", id);
			}
		}

		if (properties.isLengthCodec()) {
			int length = readLengthPropertiesValue(byteBuf, properties.getLengthProperties());
			if (!properties.isLengthMask()) {
				obj.put("length", length);
			}
		}

		for (ColumnCoder coder : coders) {
			try {
				coder.decode(byteBuf, obj, protocolContext);
			} catch (Exception e) {
				if (e instanceof ProtocolRuntimeException) {
					throw e;
				} else {
					throw new ModuleCoderException(e, byteBuf, coder.getProperties(), jsonObject);
				}
			}
		}

		if (!properties.isFlat())
			jsonObject.put(properties.getName(), obj);

		return jsonObject;
	}

	@Override
	public ByteBuf doEncode(ByteBuf byteBuf, JsonObject jsonObject, ProtocolContext protocolContext) {
		JsonObject obj = null;
		if (properties.isFlat()) {
			obj = jsonObject;
		} else {
			obj = jsonObject.getJsonObject(properties.getName());
		}

		if (properties.isIdCodec()) {
			writeLengthPropertiesValue(byteBuf, properties.getId(), properties.getIdProperties());
		}

		int markLengthFieldIndex = byteBuf.writerIndex();
		if (properties.isLengthCodec()) {
			writeLengthPropertiesValue(byteBuf, 0, properties.getLengthProperties());
		}
		int markModuleCodersBegin = byteBuf.writerIndex();

		int moduleWriterStartIndex = 0;
		ColumnCoder moduleLengthFieldCoder = null;
		int moduleLengthFieldWriteIndex = 0;

		for (ColumnCoder coder : coders) {
			try {
				// 判断是否是模块长度字段
				if (coder.getProperties().isModuleLengthField()) {
					moduleLengthFieldCoder = coder;
					moduleLengthFieldWriteIndex = byteBuf.writerIndex();
				}

				moduleWriterStartIndex = byteBuf.writerIndex();

				coder.encode(byteBuf, obj, protocolContext);

				if (moduleLengthFieldCoder != null) {
					if (coder.getProperties().getColumnType() == ColumnType.REFERENCE) {
						if (moduleLengthFieldCoder.getProperties().getModuleName().equalsIgnoreCase(coder.getProperties().getName())) {
							int length = byteBuf.writerIndex() - moduleWriterStartIndex;
							moduleLengthFieldCoder.setModuleReferenceLength(obj, length);
							/**
							 * 重新修改长度字段并编码
							 */
							moduleLengthFieldCoder.encode(byteBuf.slice(moduleLengthFieldWriteIndex, moduleLengthFieldCoder.properties.getLength()).writerIndex(0), obj, protocolContext);
						}
					}
				}
			} catch (Exception e) {
				if (e instanceof ProtocolRuntimeException) {
					throw e;
				} else {
					throw new ModuleCoderException(e, byteBuf, coder.getProperties(), jsonObject);
				}
			}
		}

		int length = byteBuf.writerIndex() - markModuleCodersBegin;
		if (properties.isLengthCodec()) {
			setLengthPropertiesValue(byteBuf, length, markLengthFieldIndex, properties.getLengthProperties());
		}
		return byteBuf;
	}

	public ModuleProperties getProperties() {
		return properties;
	}
}
