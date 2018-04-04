package com.foton.buffer.protocol.builder;

import com.foton.buffer.protocol.type.*;
import com.foton.buffer.protocol.plugin.CoderPlugin;


public class PluginCoderBuilder {

	public static CoderProperties buildPlugin(String name, int length, CoderPlugin coderPlugin) {
		return new CoderProperties().setName(name).setLength(length).setCoderPlugin(coderPlugin);
	}

	public static CoderProperties buildPlugin(String name, LengthProperties lengthProperties, CoderPlugin coderPlugin) {
		return new CoderProperties().setName(name).setLengthProperties(lengthProperties).setCoderPlugin(coderPlugin);
	}

	public static CoderProperties buildPlugin(String name, int length, String coderPluginClassName) throws Exception {
		return new CoderProperties().setName(name).setLength(length).setCoderPlugin((CoderPlugin) Class.forName(coderPluginClassName).getConstructor().newInstance());
	}

	public static CoderProperties buildPlugin(String name, LengthProperties lengthProperties, String pluginCoderClassName) throws Exception {
		return new CoderProperties().setName(name).setLengthProperties(lengthProperties).setCoderPlugin((CoderPlugin) Class.forName(pluginCoderClassName).getConstructor().newInstance());
	}
}
