package com.github.gate.coder;

import java.util.List;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;

public class DelimiterEncoder extends MessageToMessageEncoder<ByteBuf> {
    private static final int DEFAULT_MAX_BYTES_IN_MESSAGE = 8092;

    private final int maxBytesInMessage;
    private final byte[] headerDelimiter;
    private final byte[] tailerDelimiter;

    public DelimiterEncoder(String headerDelimiter, String tailerDelimiter) {
        this(ByteBufUtil.decodeHexDump(headerDelimiter), ByteBufUtil.decodeHexDump(tailerDelimiter));
    }

    public DelimiterEncoder(byte[] headerDelimiter, byte[] tailerDelimiter) {
        this.maxBytesInMessage = DEFAULT_MAX_BYTES_IN_MESSAGE;
        this.headerDelimiter = headerDelimiter;
        this.tailerDelimiter = tailerDelimiter;
    }

    @Override
    protected void encode(ChannelHandlerContext ctx, ByteBuf msg, List<Object> out) throws Exception {
        ByteBuf outMsg = ctx.alloc().buffer(msg.readableBytes() + headerDelimiter.length + tailerDelimiter.length);
        outMsg.writeBytes(headerDelimiter);
        outMsg.writeBytes(msg);
        outMsg.writeBytes(tailerDelimiter);
        out.add(outMsg);
    }
}
