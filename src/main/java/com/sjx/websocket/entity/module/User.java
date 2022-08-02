package com.sjx.websocket.entity.module;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

/**
 * SunJingxuan
 * 2022 \ 07 \ 29
 */
@TableName(value = "user")
public class User {

	@TableId
	private Integer userId;
	private String userName;
	private String userNickname;
	private String userHeadImg;
	private String userPhone;
	private String userPassword;
	private String userMail;
	private Integer status;
	private String createTime;
	private String modifyTime;

	public User() {}

	public User(Integer userId, String userName, String userNickname, String userHeadImg, String userPhone, String userPassword, String userMail, Integer status, String createTime, String modifyTime) {
		this.userId = userId;
		this.userName = userName;
		this.userNickname = userNickname;
		this.userHeadImg = userHeadImg;
		this.userPhone = userPhone;
		this.userPassword = userPassword;
		this.userMail = userMail;
		this.status = status;
		this.createTime = createTime;
		this.modifyTime = modifyTime;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getUserNickname() {
		return userNickname;
	}

	public void setUserNickname(String userNickname) {
		this.userNickname = userNickname;
	}

	public String getUserHeadImg() {
		return userHeadImg;
	}

	public void setUserHeadImg(String userHeadImg) {
		this.userHeadImg = userHeadImg;
	}

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

	public String getUserMail() {
		return userMail;
	}

	public void setUserMail(String userMail) {
		this.userMail = userMail;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getModifyTime() {
		return modifyTime;
	}

	public void setModifyTime(String modifyTime) {
		this.modifyTime = modifyTime;
	}
}
