package com.github.gate.protocol.utils;

import com.github.gate.protocol.type.RangeProperties;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;

public class RangeUtils {
    public static ByteBuf getRange(ByteBuf byteBuf, RangeProperties properties) {
        int start = getRangeStart(byteBuf, properties);
        int end = getRangeEnd(byteBuf, properties);

        if (end - start > byteBuf.readableBytes()) {
            throw new RuntimeException("out of index in getRange:[" + ByteBufUtil.hexDump(byteBuf) + "]-->" + properties.toString());
        }
        return byteBuf.slice(start, end - start);
    }

    public static int getRangeStart(ByteBuf byteBuf, RangeProperties properties) {
        try {
            if (properties.getStart() != null)
                return properties.getStart().intValue();

            if (properties.getEnd() != null && properties.getLength() != null) {
                return properties.getEnd() - properties.getLength();
            }

            if (properties.getStartOffset() != null) {
                return byteBuf.readableBytes() + properties.getStartOffset();
            }

            if (properties.getStartIndex() != null) {
                return CoderUtils.getUnsignedInteger(byteBuf, properties.getStartIndex(), properties.getStartProperties());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        throw new RuntimeException("getRangeStart:[" + ByteBufUtil.hexDump(byteBuf) + "]-->" + properties.toString());
    }

    public static int getRangeEnd(ByteBuf byteBuf, RangeProperties properties) {
        try {
            if (properties.getEnd() != null) {
                return properties.getEnd().intValue();
            }

            if (properties.getStart() != null && properties.getLength() != null) {
                return properties.getStart() + properties.getLength();
            }

            if (properties.getEndOffset() != null) {
                return byteBuf.readableBytes() + properties.getEndOffset();
            }

            if (properties.getEndIndex() != null) {
                return CoderUtils.getUnsignedInteger(byteBuf, properties.getEndIndex(), properties.getEndProperties());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        throw new RuntimeException("getRangeEnd:[" + ByteBufUtil.hexDump(byteBuf) + "]-->" + properties.toString());
    }
}
