package com.github.gate.protocol.exception;

import com.github.gate.protocol.utils.StackUtils;

import io.netty.buffer.ByteBuf;

public class ProtocolRuntimeException extends RuntimeException {
	public ProtocolRuntimeException() {
	}

	public ProtocolRuntimeException(Object... args) {
		super("\n" + StackUtils.printStacktrace(args));
	}

	public ProtocolRuntimeException(Object message, Throwable cause) {
		super("\n" + message, cause);
	}

	public ProtocolRuntimeException(Throwable cause) {
		super(cause);
	}

	public ProtocolRuntimeException(Throwable cause, Object... args) {
		super("\n" + StackUtils.printStacktrace(args), cause);
	}

	public ProtocolRuntimeException(Throwable cause, ByteBuf byteBuf, Object... args) {
		super("\n" + StackUtils.printStacktrace(byteBuf, args), cause);
	}

	public ProtocolRuntimeException(ByteBuf byteBuf, Object... args) {
		super("\n" + StackUtils.printStacktrace(byteBuf, args));
    }
}
