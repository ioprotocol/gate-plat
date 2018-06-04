package com.github.gate.coder;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.buffer.ByteBufUtil;
import org.junit.Test;

import static org.junit.Assert.*;

public class EscapCoderTest {

    @Test
    public void test() {
        EscapCoder escapCoder = new EscapCoder("5A", "5D01");
        byte[] buf = new byte[]{0x5a, 0x02, 0x5b, 0x5a, 0x01, 0x5a, 0x01, 0x02, 0x03, 0x5a};
        ByteBuf byteBuf = ByteBufAllocator.DEFAULT.buffer();
        byteBuf.writeBytes(buf);

        ByteBuf out = ByteBufAllocator.DEFAULT.buffer();
        escapCoder.encode(byteBuf, out, 1, 1);

        System.out.println(ByteBufUtil.hexDump(out));

        ByteBuf out1 = ByteBufAllocator.DEFAULT.buffer();
        escapCoder.decode(out, out1, 1, 1);

        System.out.println(ByteBufUtil.hexDump(out1));
    }
}
