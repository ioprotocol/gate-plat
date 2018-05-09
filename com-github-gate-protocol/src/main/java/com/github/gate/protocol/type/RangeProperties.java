package com.github.gate.protocol.type;

public class RangeProperties {
    // 绝对定位
    private Integer start;
    private Integer end;

    // 长度
    private Integer length;

    // 根据报文总长度来定位
    private Integer startOffset;
    private Integer endOffset;

    // 根据字段值动态定位
    private Integer startIndex;
    private CoderProperties startProperties;

    // 根据长度字段来定位
    private Integer endIndex;
    private CoderProperties endProperties;

    public Integer getStart() {
        return start;
    }

    public RangeProperties setStart(Integer start) {
        this.start = start;
        return this;
    }

    public Integer getEnd() {
        return end;
    }

    public RangeProperties setEnd(Integer end) {
        this.end = end;
        return this;
    }

    public Integer getLength() {
        return length;
    }

    public RangeProperties setLength(Integer length) {
        this.length = length;
        return this;
    }

    public Integer getStartOffset() {
        return startOffset;
    }

    public RangeProperties setStartOffset(Integer startOffset) {
        this.startOffset = startOffset;
        return this;
    }

    public Integer getEndOffset() {
        return endOffset;
    }

    public RangeProperties setEndOffset(Integer endOffset) {
        this.endOffset = endOffset;
        return this;
    }

    public Integer getStartIndex() {
        return startIndex;
    }

    public RangeProperties setStartIndex(Integer startIndex) {
        this.startIndex = startIndex;
        return this;
    }

    public CoderProperties getStartProperties() {
        return startProperties;
    }

    public RangeProperties setStartProperties(CoderProperties startProperties) {
        this.startProperties = startProperties;
        return this;
    }

    public Integer getEndIndex() {
        return endIndex;
    }

    public RangeProperties setEndIndex(Integer endIndex) {
        this.endIndex = endIndex;
        return this;
    }

    public CoderProperties getEndProperties() {
        return endProperties;
    }

    public RangeProperties setEndProperties(CoderProperties endProperties) {
        this.endProperties = endProperties;
        return this;
    }

    @Override
    public String toString() {
        return "RangeProperties{" +
                "start=" + start +
                ", end=" + end +
                ", length=" + length +
                ", startOffset=" + startOffset +
                ", endOffset=" + endOffset +
                ", startIndex=" + startIndex +
                ", startProperties=" + startProperties +
                ", endIndex=" + endIndex +
                ", endProperties=" + endProperties +
                '}';
    }
}
