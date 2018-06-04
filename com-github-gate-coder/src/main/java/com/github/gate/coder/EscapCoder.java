package com.github.gate.coder;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.buffer.UnpooledByteBufAllocator;

public class EscapCoder {
    private ByteBuf src = UnpooledByteBufAllocator.DEFAULT.buffer(2);
    private ByteBuf dest = UnpooledByteBufAllocator.DEFAULT.buffer(4);

    public EscapCoder(byte[] src, byte[] dest) {
        this.src.writeBytes(src);
        this.dest.writeBytes(dest);
    }

    public EscapCoder(String src, String dest) {
        this.src.writeBytes(ByteBufUtil.decodeHexDump(src));
        this.dest.writeBytes(ByteBufUtil.decodeHexDump(dest));
    }

    public ByteBuf decode(ByteBuf in, ByteBuf out, int headerSize, int tailerSize) {
        if (headerSize > 0) {
            out.writeBytes(in, in.readerIndex(), headerSize);
            in.skipBytes(headerSize);
        }

        int index = ByteBufUtil.indexOf(dest, in);
        while (index != -1 && in.isReadable() && index != in.writerIndex() - tailerSize) {
            out.writeBytes(in, in.readerIndex(), index - in.readerIndex());
            in.skipBytes(index - in.readerIndex() + dest.readableBytes());
            out.writeBytes(src, src.readerIndex(), src.readableBytes());
            index = ByteBufUtil.indexOf(dest, in);
        }

        out.writeBytes(in, in.readerIndex(), in.readableBytes());

        return out;
    }

    public ByteBuf encode(ByteBuf in, ByteBuf out, int headerSize, int tailerSize) {
        if (headerSize > 0) {
            out.writeBytes(in, in.readerIndex(), headerSize);
            in.skipBytes(headerSize);
        }

        int index = ByteBufUtil.indexOf(src, in);
        while (index != -1 && in.isReadable() && index != in.writerIndex() - tailerSize) {
            out.writeBytes(in, in.readerIndex(), index - in.readerIndex());
            in.skipBytes(index - in.readerIndex() + src.readableBytes());
            out.writeBytes(dest, dest.readerIndex(), dest.readableBytes());
            index = ByteBufUtil.indexOf(src, in);
        }

        out.writeBytes(in, in.readerIndex(), in.readableBytes());

        return out;
    }
}
