package com.github.gate.protocol.exception;

public class ReferenceEncodeMissingArgsException extends ProtocolRuntimeException {
    public ReferenceEncodeMissingArgsException(Object... args) {
        super(args);
    }
}
