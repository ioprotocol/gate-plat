package com.github.gate.coder;

import com.github.gate.protocol.ProtocolFactory;
import com.github.gate.protocol.core.ProtocolCoder;
import com.github.gate.protocol.type.ProtocolProperties;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageCodec;
import io.vertx.core.json.JsonObject;

import java.util.List;

public class JsonCoder extends MessageToMessageCodec<ByteBuf, JsonObject> {
    private ProtocolFactory protocolFactory = new ProtocolFactory();

    public JsonCoder(ProtocolProperties properties) {
        protocolFactory.addProtocolCoder(new ProtocolCoder(properties));
    }

    @Override
    protected void encode(ChannelHandlerContext ctx, JsonObject msg, List<Object> out) throws Exception {
        out.add(protocolFactory.encode(ctx.alloc().buffer(), msg));
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf msg, List<Object> out) throws Exception {
        out.add(protocolFactory.decode(msg));
    }
}
