package com.foton.buffer.protocol.builder;

import com.foton.buffer.protocol.core.ProtocolCoder;
import com.foton.buffer.protocol.plugin.VersionPlugin;
import com.foton.buffer.protocol.type.ModuleProperties;
import com.foton.buffer.protocol.type.RangeProperties;
import com.foton.buffer.protocol.type.ValidProperties;
import com.foton.buffer.protocol.type.VersionProperties;
import com.foton.buffer.protocol.valid.ValidMethod;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;

import java.util.List;

public class ProtocolBuilder {
	private ProtocolCoder coder;

	public ProtocolBuilder create(String namespace) {
		coder = new ProtocolCoder().setNamespace(namespace);
		return this;
	}

	public ProtocolBuilder setVersion(int version, int length, RangeProperties properties) {
		ByteBuf buf = null;
		switch (length) {
		case 1:
			buf = ByteBufAllocator.DEFAULT.buffer(1);
			buf.writeByte(version);
			break;
		case 2:
			buf = ByteBufAllocator.DEFAULT.buffer(2);
			buf.writeShort(version);
			break;
		case 3:
			buf = ByteBufAllocator.DEFAULT.buffer(3);
			buf.writeMedium(version);
			break;
		case 4:
			buf = ByteBufAllocator.DEFAULT.buffer(4);
			buf.writeInt(version);
			break;
		}
		coder.setVersionProperties(new VersionProperties().setVersion(buf).setRangeProperties(properties));
		return this;
	}

	public ProtocolBuilder setVersion(int version, int length, RangeProperties properties, VersionPlugin plugin) {
		setVersion(version, length, properties);
		coder.getVersionProperties().setVersionPlugin(plugin);
		return this;
	}

	public ProtocolBuilder setValid(RangeProperties validRange, RangeProperties validValue, ValidMethod method) {
		coder.setValidProperties(new ValidProperties().setValidRangeProperties(validRange).setValidValueProperties(validValue).setValidMethod(method));
		return this;
	}

	public ProtocolBuilder setMainModule(String group, String moduleName) {
		coder.setMainGroupName(group).setMainModuleName(moduleName);
		return this;
	}

	public ProtocolBuilder addModule(ModuleProperties properties) {
		coder.adddModule(properties);
		return this;
	}

	public ProtocolBuilder addModule(List<ModuleProperties> modulePropertiesList) {
		for (ModuleProperties moduleProperties : modulePropertiesList) {
			addModule(moduleProperties);
		}
		return this;
	}

	public ProtocolCoder build() {
		return coder;
	}
}
