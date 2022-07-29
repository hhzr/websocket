package com.sjx.websocket.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.sjx.websocket.mapper.UserMapper;
import com.sjx.websocket.pojo.User;
import com.sjx.websocket.service.UserService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * SunJingxuan
 * 2022 \ 07 \ 29
 */
@Service
public class UserServiceImpl implements UserService {

	@Resource
	private UserMapper userMapper;

	@Override
	public User login(String adminPhone, String password) {
		QueryWrapper<User> queryWrapper = new QueryWrapper<>();
		queryWrapper.select(adminPhone, password);
		User user = userMapper.selectOne(queryWrapper);
		return user;
	}
}
