package com.sjx.websocket.aspect;

import com.sjx.websocket.util.websocket.WebSocket;
import com.sjx.websocket.util.ip2region.Ip2regionUtil;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Map;

/**
 * SunJingxuan
 * 2022 \ 07 \ 28
 */
@Component
@Aspect
@Slf4j
public class IpAddressAspect {

	@Resource
	private Ip2regionUtil ip2regionUtil;

	@Resource
	private WebSocket webSocket;

	@Before("execution(* com.sjx.websocket.controller.TestController.login(..))")
	public void before(JoinPoint joinPoint) {
		RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
		if (requestAttributes != null) {
			HttpServletRequest request = (HttpServletRequest) requestAttributes.resolveReference(RequestAttributes.REFERENCE_REQUEST);
			String ip = request.getHeader("x-forwarded-for");
			if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
				ip = request.getHeader("Proxy-Client-IP");
			}
			if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
				ip = request.getHeader("WL-Proxy-Client-IP");
			}
			if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
				ip = request.getHeader("HTTP_CLIENT_IP");
			}
			if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
				ip = request.getHeader("HTTP_X_FORWARDED_FOR");
			}
			if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
				ip = request.getRemoteAddr();
			}
			// 本机访问
			if ("localhost".equalsIgnoreCase(ip) || "127.0.0.1".equalsIgnoreCase(ip) || "0:0:0:0:0:0:0:1".equalsIgnoreCase(ip)) {
				// 根据网卡取本机配置的IP
				InetAddress inet;
				try {
					inet = InetAddress.getLocalHost();
					ip = inet.getHostAddress();
				} catch (UnknownHostException e) {
					e.printStackTrace();
				}
			}
			// 对于通过多个代理的情况，第一个IP为客户端真实IP,多个IP按照','分割
			if (null != ip && ip.length() > 15) {
				if (ip.indexOf(",") > 15) {
					ip = ip.substring(0, ip.indexOf(","));
				}
			}
			Map<String, Object> ipTerritory = ip2regionUtil.getIPTerritory(ip);
			if (ipTerritory != null) {
				try {
					webSocket.onMessage("<h5><strong>他是来自 " + ipTerritory.get("country") + " " + ipTerritory.get("province") + " " + ipTerritory.get("city") + "的小伙伴</strong></h5>");
				} catch (IOException e) {
					log.error("登录失败", e);
				}
			}
		}
//		// 取mac地址
//		byte[] macAddressBytes = new byte[0];
//		try {
//			macAddressBytes = NetworkInterface.getByInetAddress(InetAddress.getLocalHost()).getHardwareAddress();
//		} catch (SocketException | UnknownHostException e) {
//			e.printStackTrace();
//		}
//		// 下面代码是把mac地址拼装成String
//		StringBuilder sb = new StringBuilder();
//		for (int i = 0; i < macAddressBytes.length; i++) {
//			if (i != 0) {
//				sb.append("-");
//			}
//			// mac[i] & 0xFF 是为了把byte转化为正整数
//			String s = Integer.toHexString(macAddressBytes[i] & 0xFF);
//			sb.append(s.length() == 1 ? 0 + s : s);
//		}
//		System.out.println(sb.toString().trim().toUpperCase());
	}

}
