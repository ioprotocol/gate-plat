package com.foton.buffer.protocol.type;

public class LengthProperties {
    public static final LengthProperties ONE = new LengthProperties(1, ByteOrder.BIG_ENDIAN);
    public static final LengthProperties TWO = new LengthProperties(2, ByteOrder.BIG_ENDIAN);
    public static final LengthProperties THREE = new LengthProperties(3, ByteOrder.BIG_ENDIAN);
    public static final LengthProperties FOUR = new LengthProperties(4, ByteOrder.BIG_ENDIAN);

    public static final LengthProperties ONE_LE = new LengthProperties(1, ByteOrder.LITTLE_ENDIAN);
    public static final LengthProperties TWO_LE = new LengthProperties(2, ByteOrder.LITTLE_ENDIAN);
    public static final LengthProperties THREE_LE = new LengthProperties(3, ByteOrder.LITTLE_ENDIAN);
    public static final LengthProperties FOUR_LE = new LengthProperties(4, ByteOrder.LITTLE_ENDIAN);
    // 字节长度
    private Integer length;
    private ByteOrder byteOrder;

    public LengthProperties() {
    }

    public LengthProperties(Integer length, ByteOrder byteOrder) {
        this.length = length;
        this.byteOrder = byteOrder;
    }

    public Integer getLength() {
        return length;
    }

    public LengthProperties setLength(Integer length) {
        this.length = length;
        return this;
    }

    public ByteOrder getByteOrder() {
        return byteOrder;
    }

    public LengthProperties setByteOrder(ByteOrder byteOrder) {
        this.byteOrder = byteOrder;
        return this;
    }
}
