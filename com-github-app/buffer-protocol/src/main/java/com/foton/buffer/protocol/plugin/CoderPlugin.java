package com.foton.buffer.protocol.plugin;

import com.foton.buffer.protocol.core.ProtocolContext;
import com.foton.buffer.protocol.type.CoderProperties;
import io.netty.buffer.ByteBuf;
import io.vertx.core.json.JsonObject;

public interface CoderPlugin {

    JsonObject decode(JsonObject jsonObject, ByteBuf byteBuf, CoderProperties properties, ProtocolContext protocolContext);

    ByteBuf encode(JsonObject jsonObject, ByteBuf byteBuf, CoderProperties properties, ProtocolContext protocolContext);
}
