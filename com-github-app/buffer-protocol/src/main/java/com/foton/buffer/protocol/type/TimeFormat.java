package com.foton.buffer.protocol.type;

import com.foton.buffer.protocol.exception.TimeFormatNotFoundException;

public enum TimeFormat {
    GBT32960("gbt32960"),
    JTT808("jtt808"),
    UTC("utc");

    TimeFormat(String name) {
        this.name = name;
    }

    private String name;

    public String getName() {
        return name;
    }

    public TimeFormat setName(String name) {
        this.name = name;
        return this;
    }

    public static TimeFormat parse(String name) {
        if (name.equalsIgnoreCase(GBT32960.getName())) {
            return GBT32960;
        } else if (name.equalsIgnoreCase(JTT808.getName())) {
            return JTT808;
        } else if (name.equalsIgnoreCase(UTC.getName())) {
            return UTC;
        }
        throw new TimeFormatNotFoundException(name);
    }

}
