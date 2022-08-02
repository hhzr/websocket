package com.sjx.websocket.exception;

import com.sjx.websocket.entity.enums.GlobalConstants;

/**
 * SunJingxuan
 * 2022 \ 08 \ 01
 */

public class GlobalException extends RuntimeException {
	private int code = GlobalConstants.ERROR.getCode();

	private String message = GlobalConstants.ERROR.getMessage();

	public GlobalException(Exception e) {
		super(e);
	}

	public GlobalException() {

	}

	public GlobalException(int code, String message) {
		this.code = code;
		this.message = message;
	}

	public GlobalException(String message, Exception e) {
		super(e);
		this.message = message;
	}

	public GlobalException(int code) {
		super();
		this.code = code;
	}

	public GlobalException(String message) {
		super(message);
		this.message = message;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
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
