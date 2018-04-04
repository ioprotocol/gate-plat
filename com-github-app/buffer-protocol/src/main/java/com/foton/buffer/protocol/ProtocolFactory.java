package com.foton.buffer.protocol;

import com.foton.buffer.protocol.core.ProtocolCoder;
import io.netty.buffer.ByteBuf;
import io.vertx.core.json.JsonObject;
import java.util.*;

public class ProtocolFactory {
	private String defNamespace;
	private Integer defVersion;

	private Map<String, Map<Integer, ProtocolCoder>> protocolFactory = new HashMap<>();

	public void addProtocolCoder(ProtocolCoder coder) {
		if (protocolFactory.containsKey(coder.getNamespace())) {
			protocolFactory.get(coder.getNamespace()).put(coder.getVersion(), coder);
		} else {
			protocolFactory.put(coder.getNamespace(), new HashMap<>());
			protocolFactory.get(coder.getNamespace()).put(coder.getVersion(), coder);
		}
	}

	public ProtocolFactory setDefaultProtocol(String namespace, Integer version) {
		this.defNamespace = namespace;
		this.defVersion = version;
		return this;
	}

	public ProtocolFactory setDefaultProtocol(String namespace) {
		this.defNamespace = namespace;
		return this;
	}

	public JsonObject decode(ByteBuf byteBuf) {
		if (defNamespace != null) {
			if (defVersion != null) {
				return protocolFactory.get(defNamespace).get(defVersion).decode(byteBuf);
			} else {
				for (Integer version : protocolFactory.get(defNamespace).keySet()) {
					if (protocolFactory.get(defNamespace).get(version).isVersionMatch(byteBuf)) {
						return protocolFactory.get(defNamespace).get(version).decode(byteBuf);
					}
				}
			}
		} else {
			for (String namespace : protocolFactory.keySet()) {
				for (Integer version : protocolFactory.get(namespace).keySet()) {
					if (protocolFactory.get(namespace).get(version).isVersionMatch(byteBuf)) {
						return protocolFactory.get(namespace).get(version).decode(byteBuf);
					}
				}
			}
		}
		return null;
	}

	public ByteBuf encode(JsonObject jsonObject) {
		String namespace = jsonObject.getString("namespace");
		Integer version = jsonObject.getInteger("version");
		if (version == null) {
			version = 0;
		}
		return protocolFactory.get(namespace).get(version).encode(jsonObject);
	}

}
