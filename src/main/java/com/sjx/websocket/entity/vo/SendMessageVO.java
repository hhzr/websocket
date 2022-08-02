package com.sjx.websocket.entity.vo;

/**
 * SunJingxuan
 * 2022 \ 08 \ 01
 */

public class SendMessageVO {

	private String nickname;
	private String headImg;
	private String message;

	public SendMessageVO() {}

	public SendMessageVO(String nickname, String headImg, String message) {
		this.nickname = nickname;
		this.headImg = headImg;
		this.message = message;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public String getHeadImg() {
		return headImg;
	}

	public void setHeadImg(String headImg) {
		this.headImg = headImg;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
}
