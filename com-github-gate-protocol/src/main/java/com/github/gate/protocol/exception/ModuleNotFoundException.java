package com.github.gate.protocol.exception;

import io.netty.buffer.ByteBuf;

public class ModuleNotFoundException extends ProtocolRuntimeException {
    public ModuleNotFoundException(ByteBuf byteBuf, Object... args) {
        super(byteBuf, args);
    }
}
