package com.github.gate.coder;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.buffer.UnpooledByteBufAllocator;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageCodec;

import java.util.List;

/**
 * 转义编解码
 */
public class EscapCoder extends MessageToMessageCodec<ByteBuf, ByteBuf> {
    private ByteBuf raw = UnpooledByteBufAllocator.DEFAULT.buffer(2);
    private ByteBuf escap = UnpooledByteBufAllocator.DEFAULT.buffer(4);

    public EscapCoder(byte[] src, byte[] dest) {
        this.raw.writeBytes(src);
        this.escap.writeBytes(dest);
    }

    public EscapCoder(String src, String dest) {
        this.raw.writeBytes(ByteBufUtil.decodeHexDump(src));
        this.escap.writeBytes(ByteBufUtil.decodeHexDump(dest));
    }

    @Override
    protected void encode(ChannelHandlerContext ctx, ByteBuf msg, List<Object> out) throws Exception {
        ByteBuf outMsg = ctx.alloc().buffer(msg.readableBytes() * 2);
        int index = ByteBufUtil.indexOf(raw, msg);
        while (index != -1) {
            outMsg.writeBytes(msg, msg.readerIndex(), index - msg.readerIndex());
            outMsg.writeBytes(escap, escap.readerIndex(), escap.readableBytes());
            msg.skipBytes(index - msg.readerIndex());
            msg.skipBytes(raw.readableBytes());
            index = ByteBufUtil.indexOf(raw, msg);
        }
        outMsg.writeBytes(msg);
        out.add(outMsg);
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf msg, List<Object> out) throws Exception {
        ByteBuf outMsg = ctx.alloc().buffer(msg.readableBytes());
        int index = ByteBufUtil.indexOf(escap, msg);
        while (index != -1) {
            outMsg.writeBytes(msg, msg.readerIndex(), index - msg.readerIndex());
            outMsg.writeBytes(raw, raw.readerIndex(), raw.readableBytes());
            msg.skipBytes(index - msg.readerIndex());
            msg.skipBytes(escap.readableBytes());
            index = ByteBufUtil.indexOf(escap, msg);
        }
        outMsg.writeBytes(msg);
        out.add(outMsg);
    }
}
