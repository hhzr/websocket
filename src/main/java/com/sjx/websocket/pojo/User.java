package com.sjx.websocket.pojo;

/**
 * SunJingxuan
 * 2022 \ 07 \ 29
 */

public class User {

	private Integer adminId;
	private String adminPhone;
	private String adminName;
	private String password;
	private String adminMail;
	private Integer adminType;
	private Integer status;

	public User() {}

	public User(Integer adminId, String adminPhone, String adminName, String password, String adminMail, Integer adminType, Integer status) {
		this.adminId = adminId;
		this.adminPhone = adminPhone;
		this.adminName = adminName;
		this.password = password;
		this.adminMail = adminMail;
		this.adminType = adminType;
		this.status = status;
	}

	public Integer getAdminId() {
		return adminId;
	}

	public void setAdminId(Integer adminId) {
		this.adminId = adminId;
	}

	public String getAdminPhone() {
		return adminPhone;
	}

	public void setAdminPhone(String adminPhone) {
		this.adminPhone = adminPhone;
	}

	public String getAdminName() {
		return adminName;
	}

	public void setAdminName(String adminName) {
		this.adminName = adminName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getAdminMail() {
		return adminMail;
	}

	public void setAdminMail(String adminMail) {
		this.adminMail = adminMail;
	}

	public Integer getAdminType() {
		return adminType;
	}

	public void setAdminType(Integer adminType) {
		this.adminType = adminType;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}
}
