package com.foton.buffer.protocol.valid;

import io.netty.buffer.ByteBuf;

public class XORValid implements ValidMethod {

    @Override
    public byte[] validCode(ByteBuf byteBuf, int start, int end) {
        int xor = byteBuf.getByte(start) & 0xFF;
        for (int i = start + 1; i < end; i++) {
            xor = xor ^ (byteBuf.getByte(i) & 0xFF);
        }
        return new byte[]{(byte)xor};
    }
}
