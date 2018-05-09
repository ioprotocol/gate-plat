package com.foton.buffer.protocol.valid;

import io.netty.buffer.ByteBuf;

public class CRC16Valid implements ValidMethod {
    @Override
    public byte[] validCode(ByteBuf byteBuf, int start, int end) {
        int crc = 0xFFFF;
        for (int i = start; i < end; i++) {
            crc = (byteBuf.getByte(i) << 8) ^ crc;
            for (int j = 0; j < 8; ++j)
                if ((crc & 0x8000) != 0)
                    crc = (crc << 1) ^ 0x1021;
                else
                    crc <<= 1;
        }

        crc = (crc ^ 0xFFFF) & 0xFFFF;
        return new byte[]{(byte) (crc & 0xFF), (byte) ((crc >>> 8) & 0xFF)};
    }
}
