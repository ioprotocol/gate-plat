package com.github.app.utils;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;

import org.apache.commons.lang.StringUtils;

public class SystemUtils {

	/**
	 * 获取服务器IP地址
	 * 
	 * @return
	 */
	public static String getLocalIpAddress() {
		String serverIp = null;
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
					serverIp = addr.getHostAddress();
				}
			}

			if (StringUtils.isEmpty(serverIp)) {
				serverIp = InetAddress.getLocalHost().getHostAddress();
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return serverIp;
	}

	public static void main(String[] args) {
		System.out.println(getLocalIpAddress());
	}
}
