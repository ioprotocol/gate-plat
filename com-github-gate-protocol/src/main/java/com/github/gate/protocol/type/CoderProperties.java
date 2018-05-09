package com.github.gate.protocol.type;

import com.github.gate.protocol.plugin.CoderPlugin;

import java.nio.charset.Charset;

public class CoderProperties {
    // 字段名称
    private String name;
    // 占用字节长度
    private Integer length;
    // 如果长度为空，需要动态计算长度
    private LengthProperties lengthProperties;
    // 位操作定义集合
    private BitProperties[] bitProperties;

    // 偏移量
    private Number offset;
    // 是否采用BCD编码格式
    private boolean isBCD = false;
    // 是否是无符号数
    private boolean isUnsigned = true;
    // 默认值
    private Object defaultValue;
    // 非法值
    private Object invalidValue;
    // 字节序
    private ByteOrder byteOrder = ByteOrder.BIG_ENDIAN;

    // 位代表的精度
    private Number precision;
    // 浮点数小数保留位数
    private Integer digit;

    // 是否代表某个引用的长度
    private boolean isModuleLengthField = false;
    // 是否在解码时屏蔽输出
    private boolean isMask = false;

    // 字符串编码格式
    private Charset charset = Charset.forName("utf-8");
    // 字符串空值填充
    private Byte nullValue;
    // 时间格式
    private TimeFormat timeFormat;
    // 寄生关系
    private ParasiticProperties parasiticProperties;
    // 自定义插件
    private CoderPlugin coderPlugin;
    // 字段类型
    private ColumnType columnType;
    // 引用
    // 模块编组名称
    private String groupName;
    // 模块名称
    private String moduleName;
    // 引用方式
    private Reference reference;
    // 数组需要嵌套一个CoderProperties
    private CoderProperties itemProperties;

    public String getName() {
        return name;
    }

    public CoderProperties setName(String name) {
        this.name = name;
        return this;
    }

    public Integer getLength() {
        return length;
    }

    public CoderProperties setLength(Integer length) {
        this.length = length;
        return this;
    }

    public LengthProperties getLengthProperties() {
        return lengthProperties;
    }

    public CoderProperties setLengthProperties(LengthProperties lengthProperties) {
        this.lengthProperties = lengthProperties;
        return this;
    }

    public BitProperties[] getBitProperties() {
        return bitProperties;
    }

    public CoderProperties setBitProperties(BitProperties[] bitProperties) {
        this.bitProperties = bitProperties;
        return this;
    }

    public Number getOffset() {
        return offset;
    }

    public CoderProperties setOffset(Number offset) {
        this.offset = offset;
        return this;
    }

    public boolean isBCD() {
        return isBCD;
    }

    public CoderProperties setBCD(boolean BCD) {
        isBCD = BCD;
        return this;
    }

    public boolean isUnsigned() {
        return isUnsigned;
    }

    public CoderProperties setUnsigned(boolean unsigned) {
        isUnsigned = unsigned;
        return this;
    }

    public Object getInvalidValue() {
        return invalidValue;
    }

    public CoderProperties setInvalidValue(Object invalidValue) {
        this.invalidValue = invalidValue;
        return this;
    }

    public ByteOrder getByteOrder() {
        return byteOrder;
    }

    public CoderProperties setByteOrder(ByteOrder byteOrder) {
        this.byteOrder = byteOrder;
        return this;
    }

    public Number getPrecision() {
        return precision;
    }

    public CoderProperties setPrecision(Number precision) {
        this.precision = precision;
        return this;
    }

    public Integer getDigit() {
        return digit;
    }

    public CoderProperties setDigit(Integer digit) {
        this.digit = digit;
        return this;
    }

    public boolean isMask() {
        return isMask;
    }

    public CoderProperties setMask(boolean mask) {
        isMask = mask;
        return this;
    }

    public Charset getCharset() {
        return charset;
    }

    public CoderProperties setCharset(Charset charset) {
        this.charset = charset;
        return this;
    }

    public Byte getNullValue() {
        return nullValue;
    }

    public CoderProperties setNullValue(Byte nullValue) {
        this.nullValue = nullValue;
        return this;
    }

    public TimeFormat getTimeFormat() {
        return timeFormat;
    }

    public CoderProperties setTimeFormat(TimeFormat timeFormat) {
        this.timeFormat = timeFormat;
        return this;
    }

    public ParasiticProperties getParasiticProperties() {
        return parasiticProperties;
    }

    public CoderProperties setParasiticProperties(ParasiticProperties parasiticProperties) {
        this.parasiticProperties = parasiticProperties;
        return this;
    }

    public CoderPlugin getCoderPlugin() {
        return coderPlugin;
    }

    public CoderProperties setCoderPlugin(CoderPlugin coderPlugin) {
        this.coderPlugin = coderPlugin;
        return this;
    }

    public ColumnType getColumnType() {
        return columnType;
    }

    public CoderProperties setColumnType(ColumnType columnType) {
        this.columnType = columnType;
        return this;
    }

    public String getGroupName() {
        return groupName;
    }

    public CoderProperties setGroupName(String groupName) {
        this.groupName = groupName;
        return this;
    }

    public Reference getReference() {
        return reference;
    }

    public CoderProperties setReference(Reference reference) {
        this.reference = reference;
        return this;
    }

    public CoderProperties getItemProperties() {
        return itemProperties;
    }

    public CoderProperties setItemProperties(CoderProperties itemProperties) {
        this.itemProperties = itemProperties;
        return this;
    }

    public Object getDefaultValue() {
        return defaultValue;
    }

    public CoderProperties setDefaultValue(Object defaultValue) {
        this.defaultValue = defaultValue;
        return this;
    }

    public String getModuleName() {
        return moduleName;
    }

    public CoderProperties setModuleName(String moduleName) {
        this.moduleName = moduleName;
        return this;
    }

    public boolean isModuleLengthField() {
        return isModuleLengthField;
    }

    public CoderProperties setModuleLengthField(boolean moduleLengthField) {
        isModuleLengthField = moduleLengthField;
        return this;
    }
}
