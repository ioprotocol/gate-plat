package com.github.gate.protocol.exception;

import io.netty.buffer.ByteBuf;

public class ColumnCoderEncodeException extends ProtocolRuntimeException {
    public ColumnCoderEncodeException(Throwable cause, ByteBuf byteBuf, Object... args) {
        super(cause, byteBuf, args);
    }
}
