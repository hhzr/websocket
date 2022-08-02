package com.sjx.websocket.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.sjx.websocket.entity.enums.GlobalConstants;
import com.sjx.websocket.entity.vo.LoginVO;
import com.sjx.websocket.exception.UserException;
import com.sjx.websocket.mapper.UserMapper;
import com.sjx.websocket.entity.module.User;
import com.sjx.websocket.service.UserService;
import com.sjx.websocket.util.ip2region.IpAddressUtil;
import com.sjx.websocket.util.redis.RedisUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Optional;

/**
 * SunJingxuan
 * 2022 \ 07 \ 29
 */
@Service
public class UserServiceImpl implements UserService {

	@Resource
	private UserMapper userMapper;

	@Resource
	private RedisUtil redisUtil;

	@Override
	public User login(LoginVO loginVO) {
		String userPhone = loginVO.getUserPhone();
		String userPassword = loginVO.getUserPassword();
		String verifyCode = loginVO.getVerifyCode();
		String macAddress = IpAddressUtil.getMacAddress();
		String verifyCodeRed = redisUtil.get(macAddress).toString();
		if (verifyCode.equalsIgnoreCase(verifyCodeRed)) {
			QueryWrapper<User> queryWrapper = new QueryWrapper<>();
			queryWrapper.eq("user_phone", userPhone);
			boolean exists = userMapper.exists(queryWrapper);
			redisUtil.delete(macAddress);
			if (exists) {
				queryWrapper.eq("user_password", userPassword);
				User user = userMapper.selectOne(queryWrapper);
				try {
					Optional<User> optionalUser = Optional.of(user);
					return optionalUser.get();
				} catch (NullPointerException e) {
					throw new UserException(GlobalConstants.USER_PASSWORD_ERR.getCode(), GlobalConstants.USER_PASSWORD_ERR.getMessage());
				}
			}
			throw new UserException(GlobalConstants.USER_NOT_EXIST.getCode(), GlobalConstants.USER_NOT_EXIST.getMessage());
		}
		throw new UserException(GlobalConstants.USER_VERIFY_CODE_WRONG.getCode(), GlobalConstants.USER_VERIFY_CODE_WRONG.getMessage());
	}
}
