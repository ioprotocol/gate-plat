package com.github.gate.coder;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.buffer.UnpooledByteBufAllocator;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.Arrays;

/**
 * 流式解码器
 * 根据固定的分隔符来拆包
 */
public class DelimiterDecoder extends ByteToMessageDecoder {
    private static final int DEFAULT_MAX_BYTES_IN_MESSAGE = 8092;

    private final int maxBytesInMessage;
    private final ByteBuf headerDelimiter = UnpooledByteBufAllocator.DEFAULT.buffer(4);
    private final ByteBuf tailerDelimiter = UnpooledByteBufAllocator.DEFAULT.buffer(4);
    private final boolean isSame;

    public DelimiterDecoder(DelimiterCoderConfig config) {
        this(ByteBufUtil.decodeHexDump(config.getHeader()), ByteBufUtil.decodeHexDump(config.getTailer()));
    }

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
        if (Arrays.equals(headerDelimiter, tailerDelimiter)) {
            isSame = true;
        } else {
            isSame = false;
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
        if (isSame) {
            buffer.markReaderIndex();
            buffer.skipBytes(headerDelimiter.readableBytes());
        }

        int endIndex = ByteBufUtil.indexOf(tailerDelimiter, buffer);

        if (isSame) {
            buffer.resetReaderIndex();
        }

        // TCP 半包，继续接收
        if (endIndex == -1) {
            return;
        }

        while (startIndex != -1) {
            buffer.readerIndex(startIndex + headerDelimiter.readableBytes());
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
            if (isSame) {
                buffer.markReaderIndex();
                buffer.skipBytes(headerDelimiter.readableBytes());
            }

            endIndex = ByteBufUtil.indexOf(tailerDelimiter, buffer);

            if (isSame) {
                buffer.resetReaderIndex();
            }

            // TCP 半包，继续接收
            if (endIndex == -1) {
                return;
            }
        }
    }
}
