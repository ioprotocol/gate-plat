package com.foton.buffer.protocol.plugin;

import com.foton.buffer.protocol.core.ProtocolContext;
import io.netty.buffer.ByteBuf;
import io.vertx.core.json.JsonObject;

public interface ModulePlugin {

    void beforeEncode(ByteBuf byteBuf, JsonObject jsonObject, ProtocolContext protocolContext);

    void afterEncode(ByteBuf byteBuf, int beforeEncodeWriterIndex, JsonObject jsonObject, ProtocolContext protocolContext);

    void beforeDecode(ByteBuf byteBuf, JsonObject jsonObject, ProtocolContext protocolContext);

    void afterDecode(ByteBuf byteBuf, int beforeDecodeReaderIndex, JsonObject jsonObject, ProtocolContext protocolContext);
}
