package com.foton.buffer.protocol.core.coder;

import com.foton.buffer.protocol.core.ProtocolContext;
import com.foton.buffer.protocol.type.ModuleProperties;
import io.netty.buffer.ByteBuf;
import io.vertx.core.json.JsonObject;

public abstract class ModuleCoderFilter extends AbstractCoder {
    protected ModuleProperties properties;

    public ModuleCoderFilter(ModuleProperties properties) {
        this.properties = properties;
    }

    @Override
    public JsonObject decode(ByteBuf byteBuf, JsonObject jsonObject, ProtocolContext protocolContext) {
        int readerIdx = byteBuf.readerIndex();
        if (properties.getModulePlugin() != null) {
            properties.getModulePlugin().beforeDecode(byteBuf, jsonObject, protocolContext);
        }
        JsonObject obj = doDecode(byteBuf, jsonObject, protocolContext);
        if (properties.getModulePlugin() != null) {
            properties.getModulePlugin().afterDecode(byteBuf, readerIdx, jsonObject, protocolContext);
        }
        return obj;
    }

    @Override
    public ByteBuf encode(ByteBuf byteBuf, JsonObject jsonObject, ProtocolContext protocolContext) {
        int writerIdx = byteBuf.writerIndex();
        if (properties.getModulePlugin() != null) {
            properties.getModulePlugin().beforeEncode(byteBuf, jsonObject, protocolContext);
        }
        ByteBuf bf = doEncode(byteBuf, jsonObject, protocolContext);
        if (properties.getModulePlugin() != null) {
            properties.getModulePlugin().afterEncode(byteBuf, writerIdx, jsonObject, protocolContext);
        }
        return bf;
    }

    public abstract JsonObject doDecode(ByteBuf byteBuf, JsonObject jsonObject, ProtocolContext protocolContext);

    public abstract ByteBuf doEncode(ByteBuf byteBuf, JsonObject jsonObject, ProtocolContext protocolContext);
}
