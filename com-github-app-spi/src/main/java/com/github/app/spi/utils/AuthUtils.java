package com.github.app.spi.utils;

import io.vertx.core.MultiMap;
import io.vertx.ext.web.RoutingContext;

import java.util.List;
import java.util.Map;

public class AuthUtils {

	/**
	 * 取操作验证码，粗粒度权限控制，如果想实现更细粒度的权限控制，在handler里来实现
	 * 
	 * @param routingContext
	 * @return
	 */
	public static String authCode(RoutingContext routingContext) {
		String code = routingContext.normalisedPath();

		int idx1 = code.indexOf("/");
		int idx2 = code.indexOf("/", idx1 + 1);
		int idx3 = code.indexOf("/", idx2 + 1);

		if(idx3 != -1) {
			code = code.substring(0, idx3);
		}

		code = String.format("[%s][%s]", routingContext.request().method().name(), code);
		return code;
	}

	public static String buildRequestInfo(RoutingContext routingContext) {
		StringBuilder builder = new StringBuilder();
		builder.append(routingContext.request().remoteAddress().toString());
		builder.append("||");
		builder.append(routingContext.normalisedPath());
		builder.append("||");
		builder.append(routingContext.request().query());
		builder.append("||");
		MultiMap map = routingContext.request().headers();
		List<Map.Entry<String, String>> list = map.entries();
		for (Map.Entry<String, String> en : list) {
			builder.append(en.getKey()).append("=").append(en.getValue()).append(";");
		}
		String msg = builder.toString();
		if(msg.length() > 500)
			msg = msg.substring(0, 490);
		return msg;
	}
}
