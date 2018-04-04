package com.foton.buffer.protocol.type;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.foton.buffer.protocol.plugin.VersionPlugin;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.buffer.ByteBufUtil;

public class VersionProperties {
	@JsonIgnore
	private ByteBuf version;
	@JsonProperty("version")
	private String versionHexString;
	private RangeProperties rangeProperties;
	@JsonIgnore
	private VersionPlugin versionPlugin;
	@JsonProperty("versionPlugin")
	private String versionPluginClassName;

	public ByteBuf getVersion() {
		return version;
	}

	public int getVersionValue() {
		if (version == null) {
			return 0;
		}
		switch (version.readableBytes()) {
		case 1:
			return version.getUnsignedByte(0);
		case 2:
			return version.getUnsignedShort(0);
		case 3:
			return version.getUnsignedMedium(0);
		case 4:
			return version.getInt(0);
		}

		throw new RuntimeException();
	}

	public VersionProperties setVersion(ByteBuf version) {
		this.version = version;
		return this;
	}

	public RangeProperties getRangeProperties() {
		return rangeProperties;
	}

	public VersionProperties setRangeProperties(RangeProperties rangeProperties) {
		this.rangeProperties = rangeProperties;
		return this;
	}

	public VersionPlugin getVersionPlugin() {
		return versionPlugin;
	}

	public VersionProperties setVersionPlugin(VersionPlugin versionPlugin) {
		this.versionPlugin = versionPlugin;
		return this;
	}

	public String getVersionPluginClassName() {
		if (versionPlugin == null) {
			return null;
		}
		return versionPlugin.getClass().getName();
	}

	public VersionProperties setVersionPluginClassName(String versionPluginClassName) {
		this.versionPluginClassName = versionPluginClassName;
		try {
			versionPlugin = (VersionPlugin) Class.forName(versionPluginClassName).getConstructor().newInstance();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return this;
	}

	public String getVersionHexString() {
		return ByteBufUtil.hexDump(version);
	}

	public VersionProperties setVersionHexString(String versionHexString) {
		this.versionHexString = versionHexString;
		this.version = ByteBufAllocator.DEFAULT.buffer();
		this.version.writeBytes(ByteBufUtil.decodeHexDump(versionHexString));
		return this;
	}
}
