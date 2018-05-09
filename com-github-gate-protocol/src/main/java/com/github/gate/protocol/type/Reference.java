package com.github.gate.protocol.type;

import com.github.gate.protocol.exception.ReferenceTypeNotFoundException;

public enum Reference {
    HARD("hard"), // 通过module group 和 moduleId 直接引用
    SOFT("soft"), // 通过module group 和 动态计算出的moduleId 引用
    LOOP("loop"), // 通过module group 和 动态计算出的moduleId 循环引用
    UNTIL_LOOP("until_loop"); // 通过结束位置来判定停止的循环模块引用

    Reference(String name) {
        this.name = name;
    }

    private String name;

    public String getName() {
        return name;
    }

    public Reference setName(String name) {
        this.name = name;
        return this;
    }

    public static Reference parse(String name) {
        if (name.equalsIgnoreCase(HARD.getName())) {
            return HARD;
        } else if (name.equalsIgnoreCase(SOFT.getName())) {
            return SOFT;
        } else if (name.equalsIgnoreCase(LOOP.getName())) {
            return LOOP;
        } else if (name.equalsIgnoreCase(UNTIL_LOOP.getName())) {
            return UNTIL_LOOP;
        }
        throw new ReferenceTypeNotFoundException(name);
    }
}
