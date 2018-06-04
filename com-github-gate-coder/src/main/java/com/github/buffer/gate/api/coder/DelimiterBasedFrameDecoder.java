package com.github.buffer.gate.api.coder;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.buffer.UnpooledByteBufAllocator;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

/**
 * 基于分隔符的解码器 TCP流时处理字节流的粘包问题
 *
 */
public class DelimiterBasedFrameDecoder extends ByteToMessageDecoder {
    private static final int DEFAULT_MAX_BYTES_IN_MESSAGE = 8092;

    private final int maxBytesInMessage;
    private final ByteBuf headerDelimiter;
    private final ByteBuf tailerDelimiter;

    public DelimiterBasedFrameDecoder(ByteBuf headerDelimiter, ByteBuf tailerDelimiter) {
        this(DEFAULT_MAX_BYTES_IN_MESSAGE, headerDelimiter, tailerDelimiter);
    }

    public DelimiterBasedFrameDecoder(String headerDelimiter, String tailerDelimiter) {
        this(ByteBufUtil.decodeHexDump(headerDelimiter), ByteBufUtil.decodeHexDump(tailerDelimiter));
    }

    public DelimiterBasedFrameDecoder(byte[] headerDelimiter, byte[] tailerDelimiter) {
        this.maxBytesInMessage = DEFAULT_MAX_BYTES_IN_MESSAGE;
        this.headerDelimiter = UnpooledByteBufAllocator.DEFAULT.buffer(headerDelimiter.length).writeBytes(headerDelimiter);
        this.tailerDelimiter = UnpooledByteBufAllocator.DEFAULT.buffer(headerDelimiter.length).writeBytes(tailerDelimiter);
    }

    public DelimiterBasedFrameDecoder(int maxBytesInMessage, ByteBuf headerDelimiter, ByteBuf tailerDelimiter) {
        this.maxBytesInMessage = maxBytesInMessage;
        this.headerDelimiter = headerDelimiter;
        this.tailerDelimiter = tailerDelimiter;
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
            out.add(msg);

            buffer.skipBytes(endIndex - startIndex + tailerDelimiter.readableBytes());
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
