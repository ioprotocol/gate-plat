package com.foton.buffer.protocol.core;

import io.netty.buffer.ByteBuf;
import io.vertx.core.json.JsonObject;

public interface ICoder {

    JsonObject decode(ByteBuf byteBuf, JsonObject jsonObject, ProtocolContext protocolContext);

    ByteBuf encode(ByteBuf byteBuf, JsonObject jsonObject, ProtocolContext protocolContext);
}
