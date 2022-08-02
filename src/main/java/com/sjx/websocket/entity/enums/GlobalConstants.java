package com.sjx.websocket.entity.enums;

/**
 * SunJingxuan
 * 2022 \ 08 \ 01
 */

public enum GlobalConstants {

	// 全局
	ERROR(-1, "好像出问题了，稍后再试"),
	SUCCESS(200, "成功"),

	// 用户
	USER_NOT_EXIST(14004, "未找到此用户"),
	USER_PASSWORD_ERR(15000, "密码错误"),
	USER_GET_VERIFY_CODE(15001, "获取验证码异常"),
	USER_VERIFY_CODE_WRONG(15002, "验证码错误"),
	USER_LOGIN(15003, "登陆异常"),

	//redis
	REDIS_CONNECTION_ERR(25000, "redis连接异常"),
	REDIS_SET_ERR(25001, "redis存储出错"),
	REDIS_SET_TIME_OUT_ERR(25002, "redis设置超时时间异常"),
	REDIS_GET_TIME_OUT_ERR(25003, "redis获取超时时间异常"),
	REDIS_GET_VALUE_ERR(25004, "redis获取值异常"),
	REDIS_DEL_VALUE_ERR(25005, "redis删除值异常"),
	REDIS_EXISTS_ERR(25006, "redis查询值是否存在异常"),
	REDIS_UPDATE_VALUE_ERR(25007, "redis更新值异常"),

	// ip地址
	IP_ADDRESS_GET_ERR(35000, "ip地址获取异常"),
	MAC_ADDRESS_GET_ERR(35001, "MAC地址获取异常"),

	// websocket
	WEB_SOCKET_ERR(45000, "通讯异常");



	private Integer code;
	private String message;

	GlobalConstants() {}

	GlobalConstants(Integer code) {
		this.code = code;
	}

	GlobalConstants(String message) {
		this.message = message;
	}

	GlobalConstants(Integer code, String message) {
		this.code = code;
		this.message = message;
	}

	public Integer getCode() {
		return code;
	}

	public String getMessage() {
		return message;
	}
}
