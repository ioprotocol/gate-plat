package com.github.app.api.jmx;

public class Hellow implements HellowMBean{
    private String name;

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String printHello() {
        return "hellow " + name;
    }

    @Override
    public String printHello(String whoName) {
        return name + " say hellow to " + whoName;
    }
}
