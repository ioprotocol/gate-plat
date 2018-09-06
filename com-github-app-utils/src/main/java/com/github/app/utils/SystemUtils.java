package com.github.app.utils;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;
import java.util.List;

import com.google.common.collect.Lists;

public class SystemUtils {

	/**
	 * 获取服务器IP地址
	 * 
	 * @return
	 */
	public static List<String> getLocalIpAddress() {
	    List<String> ips = Lists.newArrayList();
		try {
			Enumeration netInterfaces = NetworkInterface.getNetworkInterfaces();
			while (netInterfaces.hasMoreElements()) {
				NetworkInterface ni = (NetworkInterface) netInterfaces.nextElement();
				if (!ni.isUp() || ni.isLoopback() || ni.isVirtual())
					continue;
				Enumeration<InetAddress> addresses = ni.getInetAddresses();
				while (addresses.hasMoreElements()) {
					InetAddress addr = addresses.nextElement();
					if (addr.isLoopbackAddress())
						continue;
					if (!addr.isSiteLocalAddress())
						continue;
					ips.add(addr.getHostAddress());
				}
			}

			if (ips.isEmpty()) {
				ips.add(InetAddress.getLocalHost().getHostAddress());
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return ips;
	}

	/**
	 * 获取计算机名称
	 *
	 * @return
	 */
	public static String getHostName() {
		try {
			InetAddress addr = InetAddress.getLocalHost();
			String hostName = addr.getHostName();
			return hostName;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static void main(String[] args) {
		System.out.println(JacksonUtils.serializePretty(getLocalIpAddress()));
		System.out.println(getHostName());
	}
}
