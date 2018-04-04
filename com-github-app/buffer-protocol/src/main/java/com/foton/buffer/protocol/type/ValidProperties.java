package com.foton.buffer.protocol.type;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.foton.buffer.protocol.utils.RangeUtils;
import com.foton.buffer.protocol.valid.ValidMethod;

import io.netty.buffer.ByteBuf;

public class ValidProperties {
	// 校验范围
	private RangeProperties validRangeProperties;
	// 验证值的范围
	private RangeProperties validValueProperties;
	// 验证算法
    @JsonIgnore
	private ValidMethod validMethod;
	@JsonProperty("validMethod")
	private String validMethodClassName;

	public RangeProperties getValidRangeProperties() {
		return validRangeProperties;
	}

	public ValidProperties setValidRangeProperties(RangeProperties validRangeProperties) {
		this.validRangeProperties = validRangeProperties;
		return this;
	}

	public RangeProperties getValidValueProperties() {
		return validValueProperties;
	}

	public ValidProperties setValidValueProperties(RangeProperties validValueProperties) {
		this.validValueProperties = validValueProperties;
		return this;
	}

	public ValidMethod getValidMethod() {
		return validMethod;
	}

	public ValidProperties setValidMethod(ValidMethod validMethod) {
		this.validMethod = validMethod;
		return this;
	}

	public boolean isValid(ByteBuf byteBuf) {
		byte[] checkBuf = validMethod.validCode(byteBuf, RangeUtils.getRangeStart(byteBuf, validRangeProperties), RangeUtils.getRangeEnd(byteBuf, validRangeProperties));
		int start = RangeUtils.getRangeStart(byteBuf, validValueProperties);
		int end = RangeUtils.getRangeEnd(byteBuf, validValueProperties);

		if (end - start != checkBuf.length)
			return false;

		for (int i = start, j = 0; i < end; i++) {
			if (checkBuf[j++] != byteBuf.getByte(i)) {
				return false;
			}
		}

		return true;
	}

	public void setValidValue(ByteBuf byteBuf) {
		byte[] checkBuf = validMethod.validCode(byteBuf, RangeUtils.getRangeStart(byteBuf, validRangeProperties), RangeUtils.getRangeEnd(byteBuf, validRangeProperties));
		int start = RangeUtils.getRangeStart(byteBuf, validValueProperties);
		byteBuf.setBytes(start, checkBuf);
	}

	public String getValidMethodClassName() {
		if (validMethod == null) {
			return null;
		}
		return validMethod.getClass().getName();
	}

	public ValidProperties setValidMethodClassName(String validMethodClassName) {
		this.validMethodClassName = validMethodClassName;
		try {
			validMethod = (ValidMethod) Class.forName(validMethodClassName).getConstructor().newInstance();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return this;
	}
}
