package com.sjx.websocket.util.ip2region;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;

/**
 * SunJingxuan
 * 2022 \ 08 \ 01
 */

public class IpAddressUtil {

	public static String getMacAddress() {
		// 取mac地址
		byte[] macAddressBytes = new byte[0];
		try {
			macAddressBytes = NetworkInterface.getByInetAddress(InetAddress.getLocalHost()).getHardwareAddress();
		} catch (SocketException | UnknownHostException e) {
			e.printStackTrace();
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
		return sb.toString().trim().toUpperCase();
	}
}
