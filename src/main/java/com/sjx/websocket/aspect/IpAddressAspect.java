package com.sjx.websocket.aspect;

import com.sjx.websocket.exception.GlobalException;
import com.sjx.websocket.util.ip2region.Ip2regionUtil;
import com.sjx.websocket.util.random.RandomUtil;
import com.sjx.websocket.util.redis.RedisUtil;
import com.sjx.websocket.util.websocket.WebSocket;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;

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

	@Resource
	private RedisUtil redisUtil;

	@Before("execution(* com.sjx.websocket.controller.UserController.getVerifyCode(..))")
	public void before(JoinPoint joinPoint) {
		RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
		String requestUrl = "";
		StringBuilder enumName = new StringBuilder();
		if (requestAttributes != null) {
			HttpServletRequest request = (HttpServletRequest) requestAttributes.resolveReference(RequestAttributes.REFERENCE_REQUEST);
			assert request != null;
			requestUrl = request.getRequestURL().toString();
			String[] requestUrlArr = requestUrl.split("/");
			enumName.append(requestUrlArr[3].toUpperCase()).append("_").append(requestUrlArr[4].replace("[A-Z]", "_$0").toUpperCase());
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
					log.error("未获取到ip地址", e);
				}
			}
			// 对于通过多个代理的情况，第一个IP为客户端真实IP,多个IP按照','分割
			if (null != ip && ip.length() > 15) {
				if (ip.indexOf(",") > 15) {
					ip = ip.substring(0, ip.indexOf(","));
				}
			}
		}
		// 取mac地址
		byte[] macAddressBytes = new byte[0];
		try {
			macAddressBytes = NetworkInterface.getByInetAddress(InetAddress.getLocalHost()).getHardwareAddress();
		} catch (SocketException | UnknownHostException e) {
			log.error("未获取到ip地址", e);
		}
		// 下面代码是把mac地址拼装成String
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < macAddressBytes.length; i++) {
			if (i != 0) {
				sb.append("-");
			}
			// mac[i] & 0xFF 是为了把byte转化为正整数
			String s = Integer.toHexString(macAddressBytes[i] & 0xFF);
			sb.append(s.length() == 1 ? 0 + s : s);
		}
		String macAddress = sb.toString().trim().toUpperCase();
		try {
			boolean exists = redisUtil.exists(macAddress);
			if (!exists) {
				redisUtil.set(macAddress, RandomUtil.getRandomNum(4, true));
			}
		} catch (GlobalException e) {
			log.error("获取IP地址异常", e);
		}
	}
}
