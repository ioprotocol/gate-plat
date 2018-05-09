package com.github.gate.protocol.exception;

public class ReferenceTypeNotFoundException extends ProtocolRuntimeException {
    public ReferenceTypeNotFoundException(Object... message) {
        super(message);
    }
}
