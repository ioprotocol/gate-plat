package com.github.gate.protocol.type;

import java.util.HashMap;
import java.util.Map;

import com.github.gate.protocol.exception.ColumnTypeNotFoundException;

public enum ColumnType {
	BIT("bit"), INTEGER("integer"), DECIMAL("decimal"), STRING("string"), BUFFER("buffer"), DATETIME("datetime"), DATE("date"), TIME("time"), REFERENCE("reference"), ARRAY("array");

	ColumnType(String value) {
		this.value = value;
	}

	private String value;

	public String getValue() {
		return value;
	}

	public ColumnType setValue(String value) {
		this.value = value;
		return this;
	}

	private static Map<String, ColumnType> mapper = new HashMap<>();

	static {
		mapper.put(BIT.getValue(), BIT);
		mapper.put(INTEGER.getValue(), INTEGER);
		mapper.put(DECIMAL.getValue(), DECIMAL);
		mapper.put(STRING.getValue(), STRING);
		mapper.put(BUFFER.getValue(), BUFFER);
		mapper.put(DATETIME.getValue(), DATETIME);
		mapper.put(DATE.getValue(), DATE);
		mapper.put(TIME.getValue(), TIME);
		mapper.put(REFERENCE.getValue(), REFERENCE);
		mapper.put(ARRAY.getValue(), ARRAY);
	}

	public static ColumnType parse(String value) {
		if (mapper.containsKey(value.toLowerCase())) {
			return mapper.get(value);
		}
		throw new ColumnTypeNotFoundException(value);
	}
}
