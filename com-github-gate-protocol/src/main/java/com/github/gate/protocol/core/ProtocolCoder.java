package com.github.gate.protocol.core;

import com.github.gate.protocol.core.coder.ModuleCoder;
import com.github.gate.protocol.type.*;
import com.github.gate.protocol.utils.RangeUtils;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.buffer.ByteBufUtil;
import io.vertx.core.json.JsonObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class ProtocolCoder implements ProtocolContext {

	private String namespace;
	private VersionProperties versionProperties;
	private ValidProperties validProperties;
	private String mainGroupName;
	private String mainModuleName;

	/**
	 * 协议的模组集合
	 */
	private Map<String, Map<Integer, ModuleCoder>> moduleMap = new HashMap<>();
	private Map<String, Map<String, Integer>> moduleIdMap = new HashMap<>();

	/**
	 * 反向寄生的关系
	 */
	private Map<String, CoderProperties> parasiticPropertiesHashMap = new HashMap<>();

	public ProtocolCoder() {
	}

	public ProtocolCoder(ProtocolProperties protocolProperties) {
	    this.namespace = protocolProperties.getNamespace();
	    this.versionProperties =  protocolProperties.getVersionProperties();
		this.validProperties = protocolProperties.getValidProperties();
		this.mainGroupName = protocolProperties.getMainGroupName();
		this.mainModuleName = protocolProperties.getMainModuleName();
		for (ModuleProperties moduleProperties : protocolProperties.getModules()) {
			adddModule(moduleProperties);
		}
	}

	public void adddModule(ModuleProperties properties) {
		if (moduleMap.containsKey(properties.getGroup())) {
			moduleMap.get(properties.getGroup()).put(properties.getId(), new ModuleCoder(properties));
		} else {
			moduleMap.put(properties.getGroup(), new HashMap<>());
			moduleMap.get(properties.getGroup()).put(properties.getId(), new ModuleCoder(properties));
		}

		if (moduleIdMap.containsKey(properties.getGroup())) {
			moduleIdMap.get(properties.getGroup()).put(properties.getName(), properties.getId());
		} else {
			moduleIdMap.put(properties.getGroup(), new HashMap<>());
			moduleIdMap.get(properties.getGroup()).put(properties.getName(), properties.getId());
		}

		for (CoderProperties p : properties.getColumnList()) {
			if (p.getParasiticProperties() != null) {
				parasiticPropertiesHashMap.put(p.getParasiticProperties().getName(), p);
			}
		}
	}

	public ProtocolCoder setVersionProperties(VersionProperties versionProperties) {
		this.versionProperties = versionProperties;
		return this;
	}

	public ProtocolCoder setValidProperties(ValidProperties validProperties) {
		this.validProperties = validProperties;
		return this;
	}

	public boolean isVersionMatch(ByteBuf byteBuf) {
		try {
			if (versionProperties == null) {
				return true;
			}
			if (versionProperties.getVersionPlugin() != null) {
				return ByteBufUtil.equals(versionProperties.getVersionPlugin().findVersion(byteBuf), versionProperties.getVersion());
			}
			return ByteBufUtil.equals(RangeUtils.getRange(byteBuf, versionProperties.getRangeProperties()), versionProperties.getVersion());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	public int getVersion() {
		if (versionProperties == null) {
			return 0;
		} else {
			return versionProperties.getVersionValue();
		}
	}

	public JsonObject decode(ByteBuf byteBuf) {
		JsonObject obj;
		if (validProperties != null) {
			if (validProperties.isValid(byteBuf)) {
				obj = getModuleCoder(mainGroupName, mainModuleName).decode(byteBuf, new JsonObject(), this);
			} else {
				return null;
			}
		} else {
			obj = getModuleCoder(mainGroupName, mainModuleName).decode(byteBuf, new JsonObject(), this);
		}
		obj.put("namespace", namespace);
		if (versionProperties != null) {
			obj.put("version", versionProperties.getVersionValue());
		}
		return obj;
	}

	public ByteBuf encode(JsonObject jsonObject) {
		if (validProperties != null) {
			ByteBuf buf = getModuleCoder(mainGroupName, mainModuleName).encode(ByteBufAllocator.DEFAULT.buffer(), jsonObject, this);
			validProperties.setValidValue(buf);
			return buf;
		} else {
			return getModuleCoder(mainGroupName, mainModuleName).encode(ByteBufAllocator.DEFAULT.buffer(), jsonObject, this);
		}
	}

	@Override
	public ICoder getModuleCoder(String groupName, Integer id) {
		if (moduleMap.containsKey(groupName)) {
			return moduleMap.get(groupName).get(id);
		} else {
			return null;
		}
	}

	@Override
	public ICoder getModuleCoder(String groupName, String moduleName) {
		if (!moduleIdMap.containsKey(groupName)) {
			return null;
		}
		return getModuleCoder(groupName, moduleIdMap.get(groupName).get(moduleName));
	}

	@Override
	public ModuleProperties getGroupModuleProperties(String groupName) {
		if (!moduleMap.containsKey(groupName))
			return null;

		Set<Integer> set = moduleMap.get(groupName).keySet();
		return moduleMap.get(groupName).get(set.iterator().next()).getProperties();
	}

	@Override
	public Integer getModuleId(String groupName, String moduleName) {
		if (moduleMap.containsKey(groupName))
			return moduleIdMap.get(groupName).get(moduleName);
		else
			return null;
	}

	@Override
	public CoderProperties getReferenceCoderProperties(String referenceFieldName) {
		return parasiticPropertiesHashMap.get(referenceFieldName);
	}

	public String getNamespace() {
		return namespace;
	}

	public ProtocolCoder setNamespace(String namespace) {
		this.namespace = namespace;
		return this;
	}

	public String getMainGroupName() {
		return mainGroupName;
	}

	public ProtocolCoder setMainGroupName(String mainGroupName) {
		this.mainGroupName = mainGroupName;
		return this;
	}

	public String getMainModuleName() {
		return mainModuleName;
	}

	public ProtocolCoder setMainModuleName(String mainModuleName) {
		this.mainModuleName = mainModuleName;
		return this;
	}

	public VersionProperties getVersionProperties() {
		return versionProperties;
	}

	public ValidProperties getValidProperties() {
		return validProperties;
	}

	public ProtocolProperties getProperties() {
		ProtocolProperties protocolProperties = new ProtocolProperties();
		protocolProperties.setNamespace(namespace);
		protocolProperties.setVersionProperties(versionProperties);
		protocolProperties.setValidProperties(validProperties);
		protocolProperties.setMainGroupName(mainGroupName);
		protocolProperties.setMainModuleName(mainModuleName);
		moduleMap.forEach( (k, v) -> {
			v.forEach((i, m) -> {
				protocolProperties.addModuleProperties(m.getProperties());
			});
		});
		return protocolProperties;
	}
}
