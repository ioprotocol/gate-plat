package com.foton.buffer.protocol.builder;

import com.foton.buffer.protocol.exception.ModuleCoderEmptyException;
import com.foton.buffer.protocol.plugin.ModulePlugin;
import com.foton.buffer.protocol.type.LengthProperties;
import com.foton.buffer.protocol.type.ModuleProperties;
import com.foton.buffer.protocol.type.CoderProperties;
import com.foton.buffer.protocol.utils.JacksonUtils;

public class ModuleBuilder {
	private ModuleProperties properties;

	public ModuleBuilder(String group, String name, Integer id) {
		properties = new ModuleProperties().setGroup(group).setName(name).setId(id);
	}

	public ModuleBuilder setIdProperties(LengthProperties lengthProperties) {
		properties.setIdProperties(lengthProperties);
		return this;
	}

	public ModuleBuilder setLengthProperties(LengthProperties lengthProperties) {
		properties.setLengthProperties(lengthProperties);
		return this;
	}

	public ModuleBuilder enableIdCodec() {
		properties.setIdCodec(true);
		return this;
	}

	public ModuleBuilder enableIdOutput() {
		properties.setIdMask(false);
		return this;
	}

	public ModuleBuilder enableLengthCodec() {
		properties.setLengthCodec(true);
		return this;
	}

	public ModuleBuilder enableLengthOutput() {
		properties.setLengthMask(false);
		return this;
	}

	public ModuleBuilder enableFlat() {
		properties.setFlat(true);
		return this;
	}

	public ModuleBuilder addCoder(CoderProperties coderProperties) {
		properties.addCoderProperties(coderProperties);
		return this;
	}

	public ModuleBuilder setPlugin(ModulePlugin modulePlugin) {
		properties.setModulePlugin(modulePlugin);
		return this;
	}

	public ModuleProperties build() {
		if(properties.getColumnList() == null || properties.getColumnList().size() < 1)
			throw new ModuleCoderEmptyException(properties);

		if (properties.isFlat()) {
			if (properties.getName() == null) {
				properties.setName(properties.getColumnList().get(0).getName());
			}
		}
		return properties;
	}
}
