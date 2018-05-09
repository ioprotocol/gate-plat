package com.github.gate.protocol.exception;

import io.netty.buffer.ByteBuf;

public class ColumnCoderDecodeException extends ProtocolRuntimeException {
    public ColumnCoderDecodeException(Throwable cause, ByteBuf byteBuf, Object... args) {
        super(cause, byteBuf, args);
    }
}
