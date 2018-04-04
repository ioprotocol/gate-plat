package com.foton.buffer.protocol.valid;

import io.netty.buffer.ByteBuf;

public interface ValidMethod {
    byte[] validCode(ByteBuf byteBuf, int start, int end);
}
