package com.github.gate.coder;

import com.github.gate.protocol.ProtocolFactory;
import com.github.gate.protocol.core.ProtocolCoder;
import com.github.gate.protocol.type.ProtocolProperties;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import io.vertx.core.json.JsonObject;

public class JsonEncoder extends MessageToByteEncoder<JsonObject> {
    private ProtocolFactory protocolFactory;

    public JsonEncoder(ProtocolProperties... protocolProperties) {
        this.protocolFactory = new ProtocolFactory();
        for (ProtocolProperties properties : protocolProperties) {
            protocolFactory.addProtocolCoder(new ProtocolCoder(properties));
        }
    }

    @Override
    protected void encode(ChannelHandlerContext ctx, JsonObject msg, ByteBuf out) throws Exception {
        protocolFactory.encode(out, msg);
    }
}
