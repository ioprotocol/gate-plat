package com.github.gate.protocol.core;

import com.github.gate.protocol.type.CoderProperties;
import com.github.gate.protocol.type.ModuleProperties;

public interface ProtocolContext {
	ICoder getModuleCoder(String groupName, Integer id);

	ICoder getModuleCoder(String groupName, String moduleName);

	ModuleProperties getGroupModuleProperties(String groupName);

	Integer getModuleId(String groupName, String moduleName);

	CoderProperties getReferenceCoderProperties(String referenceFieldName);
}
