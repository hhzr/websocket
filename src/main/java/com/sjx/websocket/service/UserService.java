package com.sjx.websocket.service;

import com.sjx.websocket.entity.vo.LoginVO;
import com.sjx.websocket.entity.vo.UserVO;

/**
 * SunJingxuan
 * 2022 \ 07 \ 29
 */

public interface UserService {
	UserVO login(LoginVO loginVO);
}
