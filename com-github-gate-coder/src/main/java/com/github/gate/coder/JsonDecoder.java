package com.github.gate.coder;

import com.github.gate.protocol.ProtocolFactory;
import com.github.gate.protocol.core.ProtocolCoder;
import com.github.gate.protocol.type.ProtocolProperties;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.vertx.core.json.JsonObject;

public class JsonDecoder extends SimpleChannelInboundHandler<ByteBuf> {
    private ProtocolFactory protocolFactory;

    public JsonDecoder(ProtocolProperties... protocolProperties) {
        this.protocolFactory = new ProtocolFactory();
        for (ProtocolProperties properties : protocolProperties) {
            protocolFactory.addProtocolCoder(new ProtocolCoder(properties));
        }
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ByteBuf msg) throws Exception {
        JsonObject jsonObject = protocolFactory.decode(msg);
        if (jsonObject != null) {
        }
    }
}
