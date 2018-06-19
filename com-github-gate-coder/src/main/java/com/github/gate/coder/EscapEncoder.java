package com.github.gate.coder;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.UnpooledByteBufAllocator;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import io.netty.channel.ChannelPromise;
import io.netty.util.ReferenceCountUtil;

public class EscapEncoder extends ChannelOutboundHandlerAdapter {
    private final EscapCoder[] escapCoders;
    private final ByteBuf headerDelimiter = UnpooledByteBufAllocator.DEFAULT.buffer(4);
    private final ByteBuf tailerDelimiter = UnpooledByteBufAllocator.DEFAULT.buffer(4);

    public EscapEncoder(byte[] headerDelimiter, byte[] tailerDelimiter,EscapCoder... escapCoders) {
        this.escapCoders = escapCoders;
        if (headerDelimiter != null) {
            this.headerDelimiter.writeBytes(headerDelimiter);
        }
        if (tailerDelimiter != null) {
            this.tailerDelimiter.writeBytes(tailerDelimiter);
        }
    }

    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
        if (msg instanceof ByteBuf) {
            if (escapCoders != null) {
                for (EscapCoder coder : escapCoders) {
                    ByteBuf outBuf = ctx.alloc().buffer(((ByteBuf)msg).readableBytes());
                    coder.encode((ByteBuf)msg, outBuf, headerDelimiter.readableBytes(), tailerDelimiter.readableBytes());
                    ReferenceCountUtil.safeRelease(msg);
                    msg = outBuf;
                }
            }
        }
        super.write(ctx, msg, promise);
    }
}
