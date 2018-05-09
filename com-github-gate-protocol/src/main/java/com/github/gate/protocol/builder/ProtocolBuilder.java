package com.github.gate.protocol.builder;

import com.github.gate.protocol.core.ProtocolCoder;
import com.github.gate.protocol.plugin.VersionPlugin;
import com.github.gate.protocol.type.ModuleProperties;
import com.github.gate.protocol.type.RangeProperties;
import com.github.gate.protocol.type.ValidProperties;
import com.github.gate.protocol.type.VersionProperties;
import com.github.gate.protocol.valid.ValidMethod;
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
