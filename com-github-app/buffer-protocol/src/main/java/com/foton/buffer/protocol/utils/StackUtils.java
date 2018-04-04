package com.foton.buffer.protocol.utils;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;

public class StackUtils {
	public static String printStacktrace(Object... args) {
		StringBuilder builder = new StringBuilder();
		int i = 0;
		for (Object o : args) {
			builder.append("args").append(i++).append(":");
			builder.append(JacksonUtils.serializePretty(o));
			builder.append("\n");
		}
		return builder.toString();
	}

	public static String printStacktrace(ByteBuf byteBuf, Object... args) {
		StringBuilder builder = new StringBuilder();
		builder.append("readerIdx:").append(byteBuf.readerIndex()).append("\n");

		StringBuilder subB = new StringBuilder();
		subB.append(ByteBufUtil.hexDump(byteBuf));
		String wholeMsg = ByteBufUtil.hexDump(byteBuf.readerIndex(0));
		while (subB.length() < wholeMsg.length()) {
			subB.insert(0, " ");
		}
		builder.append("unread msg:").append(subB.toString()).append("\n");
		builder.append("whole  msg:").append(wholeMsg).append("\n");
		builder.append(printStacktrace(args));
		return builder.toString();
	}
}
