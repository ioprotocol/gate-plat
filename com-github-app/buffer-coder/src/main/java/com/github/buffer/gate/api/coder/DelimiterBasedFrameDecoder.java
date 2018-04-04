package com.github.buffer.gate.api.coder;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

public class DelimiterBasedFrameDecoder extends ByteToMessageDecoder {
    private static final int DEFAULT_MAX_BYTES_IN_MESSAGE = 8092;

    private final int maxBytesInMessage;
    private final ByteBuf headerDelimiter;
    private final ByteBuf tailerDelimiter;

    public DelimiterBasedFrameDecoder(ByteBuf headerDelimiter, ByteBuf tailerDelimiter) {
        this(DEFAULT_MAX_BYTES_IN_MESSAGE, headerDelimiter, tailerDelimiter);
    }

    public DelimiterBasedFrameDecoder(int maxBytesInMessage, ByteBuf headerDelimiter, ByteBuf tailerDelimiter) {
        this.maxBytesInMessage = maxBytesInMessage;
        this.headerDelimiter = headerDelimiter;
        this.tailerDelimiter = tailerDelimiter;
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf buffer, java.util.List<Object> out) {
        try {
            if (buffer.readableBytes() < headerDelimiter.readableBytes()) {
                return;
            }

            int readerIndex = buffer.readerIndex();
            int readableBytes = buffer.readableBytes();

            int headerIndex = ByteBufUtil.indexOf(headerDelimiter, buffer);
            if (headerIndex != -1) {
                buffer.skipBytes(headerIndex + headerDelimiter.readableBytes() - readerIndex);
                int tailerIndex = ByteBufUtil.indexOf(tailerDelimiter, buffer);
                if (tailerIndex == -1) {
                    buffer.readerIndex(readerIndex);
                    return;
                } else {
                    int length = tailerIndex - headerIndex + tailerDelimiter.readableBytes();
                    if (length == readableBytes) {
                        out.add(buffer.readerIndex(readerIndex));
                    } else {
                        ByteBuf msg = buffer.retainedSlice(headerIndex, length);
                        out.add(msg);
                        buffer.skipBytes(length - headerDelimiter.readableBytes());
                    }
                }
            }

            if (buffer.readableBytes() > maxBytesInMessage) {
                buffer.skipBytes(buffer.readableBytes());
                return;
            }
        } catch (Exception e) {
            buffer.skipBytes(actualReadableBytes());
        }
    }
}
