package com.github.app.api.jmx;

public interface HellowMBean {
	public String getName();

	public void setName(String name);

	public String printHello();

	public String printHello(String whoName);
}
