package com.github.app.spi.jmx;

public interface HellowMBean {
	public String getName();

	public void setName(String name);

	public String printHello();

	public String printHello(String whoName);
}
