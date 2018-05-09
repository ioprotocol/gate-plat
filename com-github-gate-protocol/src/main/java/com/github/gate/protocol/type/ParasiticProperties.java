package com.github.gate.protocol.type;

/**
 * 寄生关系
 */
public class ParasiticProperties {
    private String name;
    private Integer expectValue;
    private Match match = Match.EQ;
    private boolean isAutoEncode = true;

    public ParasiticProperties() {
    }

    public ParasiticProperties(String name) {
        this.name = name;
    }

    public ParasiticProperties(String name, Integer expectValue) {
        this.name = name;
        this.expectValue = expectValue;
    }

    public ParasiticProperties(String name, Integer expectValue, Match match) {
        this.name = name;
        this.expectValue = expectValue;
        this.match = match;
    }

    public ParasiticProperties(String name, Integer expectValue, Match match, boolean isAutoEncode) {
        this.name = name;
        this.expectValue = expectValue;
        this.match = match;
        this.isAutoEncode = isAutoEncode;
    }

    public String getName() {
        return name;
    }

    public boolean isAutoEncode() {
        return isAutoEncode;
    }

    public ParasiticProperties setAutoEncode(boolean autoEncode) {
        isAutoEncode = autoEncode;
        return this;
    }

    public ParasiticProperties setName(String name) {
        this.name = name;
        return this;
    }

    public Integer getExpectValue() {
        return expectValue;
    }

    public ParasiticProperties setExpectValue(Integer expectValue) {
        this.expectValue = expectValue;
        return this;
    }

    public Match getMatch() {
        return match;
    }

    public ParasiticProperties setMatch(Match match) {
        this.match = match;
        return this;
    }

    public boolean isActive(Integer value) {
        if (expectValue == null) {
            return true;
        }

        if (value == null) {
            return false;
        }

        if (match == null) {
            return value == expectValue.intValue();
        }

        switch (match) {
            case EQ:
                return value == expectValue.intValue();
            case GT:
                return value > expectValue.intValue();
            case LT:
                return value < expectValue.intValue();
            case GTE:
                return value >= expectValue.intValue();
            case LTE:
                return value <= expectValue.intValue();
            default:
                return false;
        }
    }

    public enum Match {
        EQ("eq", "等于"),
        GT("gt", "大于"),
        LT("lt", "小于"),
        GTE("gte", "大于等于"),
        LTE("lte", "小于等于");

        private String value;
        private String desc;

        Match(String value, String desc) {
            this.value = value;
            this.desc = desc;
        }

        public static Match parse(String value) {
            if (value.equalsIgnoreCase("eq")) {
                return EQ;
            } else if (value.equalsIgnoreCase("gt")) {
                return GT;
            } else if (value.equalsIgnoreCase("lt")) {
                return LT;
            } else if (value.equalsIgnoreCase("gte")) {
                return GTE;
            } else if (value.equalsIgnoreCase("lte")) {
                return LTE;
            }
            throw new RuntimeException("un support value match type");
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }
    }
}
