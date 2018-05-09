package com.foton.buffer.protocol.exception;

public class ReferenceEncodeMissingArgsException extends ProtocolRuntimeException {
    public ReferenceEncodeMissingArgsException(Object... args) {
        super(args);
    }
}
