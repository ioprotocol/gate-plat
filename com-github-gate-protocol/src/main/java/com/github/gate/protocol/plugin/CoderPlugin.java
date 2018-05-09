package com.github.gate.protocol.plugin;

import com.github.gate.protocol.core.ProtocolContext;
import com.github.gate.protocol.type.CoderProperties;
import io.netty.buffer.ByteBuf;
import io.vertx.core.json.JsonObject;

public interface CoderPlugin {

    JsonObject decode(JsonObject jsonObject, ByteBuf byteBuf, CoderProperties properties, ProtocolContext protocolContext);

    ByteBuf encode(JsonObject jsonObject, ByteBuf byteBuf, CoderProperties properties, ProtocolContext protocolContext);
}
