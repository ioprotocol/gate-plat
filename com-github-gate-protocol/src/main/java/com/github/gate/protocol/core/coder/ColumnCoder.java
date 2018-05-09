package com.github.gate.protocol.core.coder;

import com.github.gate.protocol.core.ProtocolContext;
import com.foton.buffer.protocol.exception.*;
import com.github.gate.protocol.type.BitProperties;
import com.github.gate.protocol.type.ByteOrder;
import com.github.gate.protocol.type.CoderProperties;
import com.github.gate.protocol.type.ColumnType;
import com.github.gate.protocol.exception.*;
import io.netty.buffer.ByteBuf;
import io.vertx.core.json.JsonObject;

import java.util.Map;

public abstract class ColumnCoder extends AbstractCoder {
	protected CoderProperties properties;

	public ColumnCoder(CoderProperties properties) {
		this.properties = properties;
	}

	public void setModuleReferenceLength(JsonObject jsonObject, int length) {
		if (properties.getColumnType() != ColumnType.BIT) {
			jsonObject.put(properties.getName(), length);
		} else {
			for (BitProperties bitProperties : properties.getBitProperties()) {
				if (bitProperties.isModuleLengthField()) {
					jsonObject.put(bitProperties.getName(), length);
				}
			}
		}
	}

	@Override
	public JsonObject decode(ByteBuf byteBuf, JsonObject jsonObject, ProtocolContext protocolContext) {
		try {
			if (properties.getParasiticProperties() != null) {
				if (properties.getParasiticProperties().isActive(jsonObject.getInteger(properties.getParasiticProperties().getName()))) {
					if (properties.getCoderPlugin() != null) {
						return properties.getCoderPlugin().decode(jsonObject, byteBuf, properties, protocolContext);
					} else {
						return doDecode(byteBuf, jsonObject, protocolContext);
					}
				} else {
					return jsonObject;
				}
			} else {
				if (properties.getCoderPlugin() != null) {
					return properties.getCoderPlugin().decode(jsonObject, byteBuf, properties, protocolContext);
				} else {
					return doDecode(byteBuf, jsonObject, protocolContext);
				}
			}
		} catch (Exception e) {
			if (e instanceof ProtocolRuntimeException) {
				throw e;
			} else {
				throw new ColumnCoderDecodeException(e, byteBuf, properties, jsonObject);
			}
		}
	}

	@Override
	public ByteBuf encode(ByteBuf byteBuf, JsonObject jsonObject, ProtocolContext protocolContext) {
		try {
			if (protocolContext.getReferenceCoderProperties(properties.getName()) != null) {
				if (protocolContext.getReferenceCoderProperties(properties.getName()).getParasiticProperties().isAutoEncode()) {
					/**
					 * 如果此字段存在寄生关系，则根据被寄生字段自动填充此字段
					 */
					if (protocolContext.getReferenceCoderProperties(properties.getName()).getColumnType() == ColumnType.REFERENCE) {
						// 如果是模块依赖，自动填充模块的ID
						JsonObject obj = jsonObject.getJsonObject(protocolContext.getReferenceCoderProperties(properties.getName()).getName());
						for (Map.Entry<String, Object> entry : obj) {
							jsonObject.put(properties.getName(), protocolContext.getModuleId(protocolContext.getReferenceCoderProperties(properties.getName()).getGroupName(), entry.getKey()));
						}
					} else {
						if (jsonObject.containsKey(protocolContext.getReferenceCoderProperties(properties.getName()).getName())) {
							jsonObject.put(properties.getName(), protocolContext.getReferenceCoderProperties(properties.getName()).getParasiticProperties().getExpectValue());
						}
					}
				}
			}

			// 如果此模块属于寄生字段或者模块，jsonObject 没有值，不参与编码
			if (properties.getParasiticProperties() != null) {
				if (!jsonObject.containsKey(properties.getName())) {
					return byteBuf;
				}
			}

			if (properties.getCoderPlugin() != null) {
				return properties.getCoderPlugin().encode(jsonObject, byteBuf, properties, protocolContext);
			} else {
				return doEncode(byteBuf, jsonObject, protocolContext);
			}
		} catch (Exception e) {
			throw new ColumnCoderEncodeException(e, byteBuf, properties, jsonObject);
		}
	}

	protected abstract JsonObject doDecode(ByteBuf byteBuf, JsonObject jsonObject, ProtocolContext protocolContext);

	protected abstract ByteBuf doEncode(ByteBuf byteBuf, JsonObject jsonObject, ProtocolContext protocolContext);

	protected void writeValue(ByteBuf byteBuf, int length, long value, ByteOrder byteOrder) {
		if (byteOrder == null)
			byteOrder = ByteOrder.BIG_ENDIAN;

		switch (length) {
		case 0:
			break;
		case 1:
			byteBuf.writeByte((int) value);
			break;
		case 2:
			if (byteOrder == ByteOrder.BIG_ENDIAN) {
				byteBuf.writeShort((int) value);
			} else {
				byteBuf.writeShortLE((int) value);
			}
			break;
		case 3:
			if (byteOrder == ByteOrder.BIG_ENDIAN) {
				byteBuf.writeMedium((int) value);
			} else {
				byteBuf.writeMediumLE((int) value);
			}
			break;
		case 4:
			if (byteOrder == ByteOrder.BIG_ENDIAN) {
				byteBuf.writeInt((int) value);
			} else {
				byteBuf.writeIntLE((int) value);
			}
			break;
		case 5:
			if (byteOrder == ByteOrder.BIG_ENDIAN) {
				byteBuf.writeByte((int) (value >>> 32));
				byteBuf.writeInt((int) value);
			} else {
				byteBuf.writeIntLE((int) value);
				byteBuf.writeByte((int) (value >>> 32));
			}
			break;
		case 6:
			if (byteOrder == ByteOrder.BIG_ENDIAN) {
				byteBuf.writeShort((int) (value >>> 32));
				byteBuf.writeInt((int) value);
			} else {
				byteBuf.writeIntLE((int) value);
				byteBuf.writeShortLE((int) (value >>> 32));
			}
			break;
		case 7:
			if (byteOrder == ByteOrder.BIG_ENDIAN) {
				byteBuf.writeMedium((int) (value >>> 32));
				byteBuf.writeInt((int) value);
			} else {
				byteBuf.writeIntLE((int) value);
				byteBuf.writeMediumLE((int) (value >>> 32));
			}
			break;
		case 8:
			if (byteOrder == ByteOrder.BIG_ENDIAN) {
				byteBuf.writeLong(value);
			} else {
				byteBuf.writeLongLE(value);
			}
			break;
		default:
			throw new NumberValueOutofRangeException(length);
		}
	}

	protected long readValue(ByteBuf byteBuf, int length, ByteOrder byteOrder) {
		if (byteOrder == null)
			byteOrder = ByteOrder.BIG_ENDIAN;

		long value = 0;
		switch (length) {
		case 0:
			break;
		case 1:
			value = byteBuf.readUnsignedByte();
			break;
		case 2:
			if (byteOrder == ByteOrder.BIG_ENDIAN) {
				value = byteBuf.readUnsignedShort();
			} else {
				value = byteBuf.readUnsignedShortLE();
			}
			break;
		case 3:
			if (byteOrder == ByteOrder.BIG_ENDIAN) {
				value = byteBuf.readUnsignedMedium();
			} else {
				value = byteBuf.readUnsignedMediumLE();
			}
			break;
		case 4:
			if (byteOrder == ByteOrder.BIG_ENDIAN) {
				value = byteBuf.readUnsignedInt();
			} else {
				value = byteBuf.readUnsignedIntLE();
			}
			break;
		case 5:
			if (byteOrder == ByteOrder.BIG_ENDIAN) {
				value = byteBuf.readUnsignedInt();
				value = value << 8;
				value = value | byteBuf.readUnsignedByte();
			} else {
				value = byteBuf.readUnsignedIntLE();
				value = value | (byteBuf.readUnsignedByte() << 32);
			}
			break;
		case 6:
			if (byteOrder == ByteOrder.BIG_ENDIAN) {
				value = byteBuf.readUnsignedInt();
				value = value << 16;
				value = value | byteBuf.readUnsignedShort();
			} else {
				value = byteBuf.readUnsignedIntLE();
				value = value | (byteBuf.readUnsignedShortLE() << 32);
			}
			break;
		case 7:
			if (byteOrder == ByteOrder.BIG_ENDIAN) {
				value = byteBuf.readUnsignedInt();
				value = value << 24;
				value = value | byteBuf.readUnsignedMedium();
			} else {
				value = byteBuf.readUnsignedIntLE();
				value = value | (byteBuf.readUnsignedMediumLE() << 32);
			}
			break;
		case 8:
			if (byteOrder == ByteOrder.BIG_ENDIAN) {
				value = byteBuf.readLong();
			} else {
				value = byteBuf.readLongLE();
			}
			break;
		default:
			throw new NumberValueOutofRangeException(length);
		}
		return value;
	}

	protected boolean isValid(long value, Number invalidValue, int length) {
		if (invalidValue == null) {
			return true;
		}

		switch (length) {
		case 0:
		case 1:
		case 2:
		case 3:
			return invalidValue.intValue() != value;
		case 4:
			return invalidValue.intValue() != toSigned(value, 4);
		case 5:
		case 6:
		case 7:
		case 8:
			return invalidValue.longValue() != value;
		}

		return true;
	}

	protected long toSigned(long value, int length) {
		switch (length) {
		case 1:
			value = (byte) value;
			break;
		case 2:
			value = (short) value;
			break;
		case 3:
			if ((value & 0x800000L) != 0) {
				value |= 0xffffffff_ff000000L;
			}
			break;
		case 4:
			value = (int) value;
			break;
		case 5:
			if ((value & 0x80_000000L) != 0) {
				value |= 0xffffff00_00000000L;
			}
			break;
		case 6:
			if ((value & 0x8000_000000L) != 0) {
				value |= 0xffff0000_00000000L;
			}
			break;
		case 7:
			if ((value & 0x800000_000000L) != 0) {
				value |= 0xff000000_00000000L;
			}
			break;
		case 8:
			break;
		default:
			break;
		}
		return value;
	}

	protected int readLength(ByteBuf byteBuf) {
		if (properties.getLength() != null)
			return properties.getLength();
		return super.readLengthPropertiesValue(byteBuf, properties.getLengthProperties());
	}

	protected void writeLength(ByteBuf byteBuf, int length) {
		if (properties.getLength() != null) {
			if (properties.getLength() != length) {
				throw new LengthNotMatchException("Coder config length:" + properties.getLength(), "actual:" + length, properties);
			}
			return;
		}
		super.writeLengthPropertiesValue(byteBuf, length, properties.getLengthProperties());
	}

	public CoderProperties getProperties() {
		return properties;
	}
}
