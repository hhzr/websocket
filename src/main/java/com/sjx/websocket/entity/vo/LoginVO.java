package com.sjx.websocket.entity.vo;

/**
 * SunJingxuan
 * 2022 \ 08 \ 01
 */

public class LoginVO {

	private String userPhone;
	private String userPassword;
	private String verifyCode;

	public String getUserPhone() {
		return userPhone;
	}

	public void setUserPhone(String userPhone) {
		this.userPhone = userPhone;
	}

	public String getUserPassword() {
		return userPassword;
	}

	public void setUserPassword(String userPassword) {
		this.userPassword = userPassword;
	}

	public String getVerifyCode() {
		return verifyCode;
	}

	public void setVerifyCode(String verifyCode) {
		this.verifyCode = verifyCode;
	}
}
