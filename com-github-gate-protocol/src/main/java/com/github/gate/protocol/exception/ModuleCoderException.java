package com.github.gate.protocol.exception;

import io.netty.buffer.ByteBuf;

public class ModuleCoderException extends ProtocolRuntimeException {
    public ModuleCoderException(Throwable cause, ByteBuf byteBuf, Object... args) {
        super(cause, byteBuf, args);
    }
}
