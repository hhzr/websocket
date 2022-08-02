package com.sjx.websocket.service;

import com.sjx.websocket.entity.module.User;
import com.sjx.websocket.entity.vo.LoginVO;

/**
 * SunJingxuan
 * 2022 \ 07 \ 29
 */

public interface UserService {
	User login(LoginVO loginVO);
}
