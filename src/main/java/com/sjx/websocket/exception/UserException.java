package com.sjx.websocket.exception;

import com.sjx.websocket.entity.enums.GlobalConstants;

/**
 * SunJingxuan
 * 2022 \ 08 \ 01
 */

public class UserException extends RuntimeException {

	private Integer code = GlobalConstants.ERROR.getCode();
	private String message = GlobalConstants.ERROR.getMessage();

	public UserException() {}

	public UserException(Integer code, String message) {
		this.code = code;
		this.message = message;
	}

	public UserException(String message, Exception e) {
		super(e);
		this.message = message;
	}

	public UserException(int code) {
		super();
		this.code = code;
	}

	public UserException(String message) {
		super(message);
		this.message = message;
	}

	public Integer getCode() {
		return code;
	}

	public void setCode(Integer code) {
		this.code = code;
	}

	@Override
	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
}
