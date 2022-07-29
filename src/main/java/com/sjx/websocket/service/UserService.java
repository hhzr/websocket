package com.sjx.websocket.service;

import com.sjx.websocket.pojo.User;

/**
 * SunJingxuan
 * 2022 \ 07 \ 29
 */

public interface UserService {
	User login(String adminPhone, String password);
}
