package com.github.gate.coder;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.buffer.UnpooledByteBufAllocator;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

/**
 * 流式解码器
 * 根据固定的分隔符来拆包
 */
public class DelimiterDecoder extends ByteToMessageDecoder {
    private static final int DEFAULT_MAX_BYTES_IN_MESSAGE = 8092;

    private final int maxBytesInMessage;
    private final ByteBuf headerDelimiter = UnpooledByteBufAllocator.DEFAULT.buffer(4);
    private final ByteBuf tailerDelimiter = UnpooledByteBufAllocator.DEFAULT.buffer(4);

    public DelimiterDecoder(String headerDelimiter, String tailerDelimiter) {
        this(ByteBufUtil.decodeHexDump(headerDelimiter), ByteBufUtil.decodeHexDump(tailerDelimiter));
    }

    public DelimiterDecoder(byte[] headerDelimiter, byte[] tailerDelimiter) {
        this.maxBytesInMessage = DEFAULT_MAX_BYTES_IN_MESSAGE;
        if (headerDelimiter != null) {
            this.headerDelimiter.writeBytes(headerDelimiter);
        }
        if (tailerDelimiter != null) {
            this.tailerDelimiter.writeBytes(tailerDelimiter);
        }
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf buffer, java.util.List<Object> out) {
        if (buffer.readableBytes() < headerDelimiter.readableBytes()) {
            return;
        }

        if (buffer.readableBytes() < tailerDelimiter.readableBytes()) {
            return;
        }

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
            buffer.skipBytes(headerDelimiter.readableBytes());
            ByteBuf msg = buffer.readRetainedSlice(endIndex - startIndex - headerDelimiter.readableBytes());
            buffer.skipBytes(tailerDelimiter.readableBytes());

            out.add(msg);

            if (!buffer.isReadable()) {
                break;
            }

            if (buffer.readableBytes() < headerDelimiter.readableBytes()) {
                return;
            }

            if (buffer.readableBytes() < tailerDelimiter.readableBytes()) {
                return;
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
