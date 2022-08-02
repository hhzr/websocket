package com.sjx.websocket.entity.http;

import com.sjx.websocket.entity.enums.GlobalConstants;

/**
 * SunJingxuan
 * 2022 \ 07 \ 29
 */

public class Response<T> {
	private Integer code;
	private String message;
	private T data;


	public static <T> Response<T> success() {
		return new Response<>(GlobalConstants.ERROR.getCode(), GlobalConstants.ERROR.getMessage());
	}

	public static <T> Response<T> success(T data) {
		return new Response<>(GlobalConstants.SUCCESS.getCode(), GlobalConstants.SUCCESS.getMessage(), data);
	}

	public static <T> Response<T> success(String message, T data) {
		return new Response<>(GlobalConstants.SUCCESS.getCode(), message, data);
	}

	public static <T> Response<T> success(Integer code, String message, T data) {
		return new Response<>(code, message, data);
	}

	public static <T> Response<T> error() {
		return new Response<>(GlobalConstants.ERROR.getCode(), GlobalConstants.ERROR.getMessage());
	}

	public static <T> Response<T> error(String message) {
		return new Response<>(GlobalConstants.ERROR.getCode(), message);
	}

	public static <T> Response<T> error(Integer code, String message) {
		return new Response<>(code, message);
	}

	public static <T> Response<T> error(Integer code, String message, T data) {
		return new Response<>(code, message, data);
	}

	public Response() {}

	public Response(Integer code, String message) {
		this.code = code;
		this.message = message;
	}

	public Response(Integer code, String message, T data) {
		this.code = code;
		this.message = message;
		this.data = data;
	}

	public Integer getCode() {
		return code;
	}

	public void setCode(Integer code) {
		this.code = code;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}
}
