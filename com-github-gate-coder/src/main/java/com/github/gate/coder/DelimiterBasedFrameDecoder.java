package com.github.gate.coder;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.buffer.UnpooledByteBufAllocator;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.util.ReferenceCountUtil;

/**
 * 基于分隔符的流式解码器
 */
public class DelimiterBasedFrameDecoder extends ByteToMessageDecoder {
    private static final int DEFAULT_MAX_BYTES_IN_MESSAGE = 8092;

    private final int maxBytesInMessage;
    private final ByteBuf headerDelimiter;
    private final ByteBuf tailerDelimiter;
    private final EscapCoder[] escapCoders;

    public DelimiterBasedFrameDecoder(String headerDelimiter, String tailerDelimiter, EscapCoder... escapCoders) {
        this(ByteBufUtil.decodeHexDump(headerDelimiter), ByteBufUtil.decodeHexDump(tailerDelimiter), escapCoders);
    }

    public DelimiterBasedFrameDecoder(byte[] headerDelimiter, byte[] tailerDelimiter, EscapCoder... escapCoders) {
        this.maxBytesInMessage = DEFAULT_MAX_BYTES_IN_MESSAGE;
        this.headerDelimiter = UnpooledByteBufAllocator.DEFAULT.buffer(headerDelimiter.length).writeBytes(headerDelimiter);
        this.tailerDelimiter = UnpooledByteBufAllocator.DEFAULT.buffer(headerDelimiter.length).writeBytes(tailerDelimiter);
        this.escapCoders = escapCoders;
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf buffer, java.util.List<Object> out) {
        int startIndex = ByteBufUtil.indexOf(headerDelimiter, buffer);
        if (startIndex == -1) {
            buffer.skipBytes(buffer.readableBytes());
            return;
        }
        buffer.readerIndex(startIndex);

        int endIndex = ByteBufUtil.indexOf(tailerDelimiter, buffer);
        // TCP 半包，继续接收
        if (endIndex == -1) {
            return;
        }

        while (startIndex != -1) {
            ByteBuf msg = buffer.readRetainedSlice(endIndex - startIndex + tailerDelimiter.readableBytes());

            if (escapCoders != null) {
                for (EscapCoder coder : escapCoders) {
                    ByteBuf outBuf = ctx.alloc().buffer(msg.readableBytes());
                    coder.decode(msg, outBuf, headerDelimiter.readableBytes(), tailerDelimiter.readableBytes());
                    ReferenceCountUtil.safeRelease(msg);
                    msg = outBuf;
                }
            }
            out.add(msg);

            if (!buffer.isReadable()) {
                break;
            }

            startIndex = ByteBufUtil.indexOf(headerDelimiter, buffer);
            if (startIndex == -1) {
                buffer.skipBytes(buffer.readableBytes());
                return;
            }
            buffer.readerIndex(startIndex);
            endIndex = ByteBufUtil.indexOf(tailerDelimiter, buffer);
            // TCP 半包，继续接收
            if (endIndex == -1) {
                return;
            }
        }
    }
}
