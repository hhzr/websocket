package com.sjx.websocket.util.redis;

/**
 * @author 孙敬轩
 * @version V1.0
 * @Description: redis工具类
 * @date 2020/1/3 09:58 星期五
 */

import com.sjx.websocket.entity.enums.GlobalConstants;
import com.sjx.websocket.exception.GlobalException;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.redis.RedisConnectionFailureException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
@Slf4j
public class RedisUtil {

	/**
	 * 获取redisTemplate对象
	 */
	@Resource
	private RedisTemplate<String, Object> redisTemplate;

	/**
	 * redis中添加数据
	 *
	 * @param key   键
	 * @param value 值
	 * @return whether succeed
	 */
	public boolean set(String key, String value) {
		try {
			// 将键值序列化
			redisTemplate.setKeySerializer(new StringRedisSerializer());
			redisTemplate.setValueSerializer(new StringRedisSerializer());
			// 实例化操作redis的ValueOperations对象
			ValueOperations<String, Object> vo = redisTemplate.opsForValue();
			// 调用set方法将键值传入
			vo.set(key, value);
			return true;
		} catch (Exception e) {
			log.error("redis存储无超时时间出错", e);
			throw new GlobalException(GlobalConstants.REDIS_SET_ERR.getCode(), GlobalConstants.REDIS_SET_ERR.getMessage());
		}
	}

	/**
	 * 添加数据时设置超时时间
	 *
	 * @param key     键
	 * @param value   值
	 * @param seconds 有效时长
	 * @return whether succeed
	 */
	public boolean set(String key, String value, long seconds) {
		try {
			// 键值序列化
			redisTemplate.setKeySerializer(new StringRedisSerializer());
			redisTemplate.setValueSerializer(new StringRedisSerializer());
			// 实例化操作redis的ValueOperations对象
			ValueOperations<String, Object> vo = redisTemplate.opsForValue();
			// 将键值存入redis
			vo.set(key, value);
			// 根据key设置超时时间
			return expire(key, seconds);
		} catch (RedisConnectionFailureException e) {
			log.error("redis连接失败", e);
			throw new GlobalException(GlobalConstants.REDIS_CONNECTION_ERR.getCode(), GlobalConstants.REDIS_CONNECTION_ERR.getMessage());
		} catch (Exception e) {
			log.error("redis存储并设置超时时间出错", e);
			throw new GlobalException(GlobalConstants.REDIS_SET_ERR.getCode(), GlobalConstants.REDIS_SET_ERR.getMessage());
		}
	}

	/**
	 * 根据key设置超时时间
	 *
	 * @param key        键
	 * @param expireTime 有效时长
	 */
	public Boolean expire(final String key, final long expireTime) {
		// 重写内部类
		// 序列化键值对
		// 将键转化成byte数组
		// 根据键设置超时时间
		return redisTemplate.execute(new RedisCallback<Boolean>() {
			// 重写内部类
			@Override
			public Boolean doInRedis(@NotNull RedisConnection redisConnection) {
				boolean flag = false;
				try {
					// 序列化键值对
					redisTemplate.setKeySerializer(new StringRedisSerializer());
					redisTemplate.setValueSerializer(new StringRedisSerializer());
					// 将键转化成byte数组
					byte[] keys = new StringRedisSerializer().serialize(key);
					// 根据键设置超时时间
					if (keys != null && keys.length > 0) {
						flag = Boolean.TRUE.equals(redisConnection.expire(keys, expireTime));
					}
				} catch (RedisConnectionFailureException e) {
					log.error("redis连接失败", e);
					throw new GlobalException(GlobalConstants.REDIS_CONNECTION_ERR.getCode(), GlobalConstants.REDIS_CONNECTION_ERR.getMessage());
				} catch (Exception e) {
					log.error("redis给现有key设置超时时间出错", e);
					throw new GlobalException(GlobalConstants.REDIS_SET_TIME_OUT_ERR.getCode(), GlobalConstants.REDIS_SET_TIME_OUT_ERR.getMessage());
				}
				return flag;
			}
		});
	}

	/**
	 * 获取到期剩余时间
	 *
	 * @param key 键
	 * @return date
	 */
	public Long getExpire(String key) {
		try {
			// 将键序列化给redis
			redisTemplate.setKeySerializer(new StringRedisSerializer());
			// 将查询的值进行序列化
			redisTemplate.setValueSerializer(new StringRedisSerializer());
			ValueOperations<String, Object> vo = redisTemplate.opsForValue();
			return redisTemplate.getExpire(key);
		} catch (RedisConnectionFailureException e) {
			log.error("redis连接失败", e);
			throw new GlobalException(GlobalConstants.REDIS_CONNECTION_ERR.getCode(), GlobalConstants.REDIS_CONNECTION_ERR.getMessage());
		} catch (Exception e) {
			log.error("redis获取剩余时间出错", e);
			throw new GlobalException(GlobalConstants.REDIS_GET_TIME_OUT_ERR.getCode(), GlobalConstants.REDIS_GET_TIME_OUT_ERR.getMessage());
		}
	}

	/**
	 * 获取值
	 *
	 * @param key 键
	 * @return data
	 */
	public Object get(String key) {
		try {
			// 将键序列化给redis
			redisTemplate.setKeySerializer(new StringRedisSerializer());
			// 将查询的值进行序列化
			redisTemplate.setValueSerializer(new StringRedisSerializer());
			// 实例化redis操作对象
			ValueOperations<String, Object> vo = redisTemplate.opsForValue();
			// 返回查询结果
			return vo.get(key);
		} catch (RedisConnectionFailureException e) {
			log.error("redis连接失败", e);
			throw new GlobalException(GlobalConstants.REDIS_CONNECTION_ERR.getCode(), GlobalConstants.REDIS_CONNECTION_ERR.getMessage());
		} catch (Exception e) {
			log.error("redis获取值出错", e);
			throw new GlobalException(GlobalConstants.REDIS_GET_VALUE_ERR.getCode(), GlobalConstants.REDIS_GET_VALUE_ERR.getMessage());
		}
	}

	/**
	 * 删除值
	 *
	 * @param key 键
	 */
	public boolean delete(String key) {
		try {
			// 实例化键
			redisTemplate.setKeySerializer(new StringRedisSerializer());
			// 通过键删除值
			return Boolean.TRUE.equals(redisTemplate.delete(key));
		} catch (RedisConnectionFailureException e) {
			log.error("redis连接失败", e);
			throw new GlobalException(GlobalConstants.REDIS_CONNECTION_ERR.getCode(), GlobalConstants.REDIS_CONNECTION_ERR.getMessage());
		} catch (Exception e) {
			log.error("redis删除key出错", e);
			throw new GlobalException(GlobalConstants.REDIS_DEL_VALUE_ERR.getCode(), GlobalConstants.REDIS_DEL_VALUE_ERR.getMessage());
		}
	}

	/**
	 * 是否存在
	 *
	 * @param key 键
	 * @return key does it exist
	 */
	public boolean exists(String key) {
		try {
			// 序列化键
			redisTemplate.setKeySerializer(new StringRedisSerializer());
			// 将查询的值进行序列化
			redisTemplate.setValueSerializer(new StringRedisSerializer());
			// 实例化reids操作对象
			ValueOperations<String, Object> vo = redisTemplate.opsForValue();
			// 获取值
			Object value = vo.get(key);
			if (value == null || value == "") {
				return false;
			} else {
				return true;
			}
		} catch (RedisConnectionFailureException e) {
			log.error("redis连接失败", e);
			throw new GlobalException(GlobalConstants.REDIS_CONNECTION_ERR.getCode(), GlobalConstants.REDIS_CONNECTION_ERR.getMessage());
		} catch (Exception e) {
			log.error("redis查询值是否存在出错", e);
			throw new GlobalException(GlobalConstants.REDIS_EXISTS_ERR.getCode(), GlobalConstants.REDIS_EXISTS_ERR.getMessage());
		}
	}

	/**
	 * 修改值
	 *
	 * @param key   原键
	 * @param value 新值
	 * @return update true or false
	 */
	public boolean update(String key, String value) {
		try {
			// 键值序列化
			redisTemplate.setKeySerializer(new StringRedisSerializer());
			redisTemplate.setValueSerializer(new StringRedisSerializer());
			// 实例化reids操作对象
			ValueOperations<String, Object> vo = redisTemplate.opsForValue();
			// 获取当前key的超时时间
			Long expireTime = redisTemplate.getExpire(key);
			// 如果为空、零或者-2则已过期或不存在
			if (expireTime == null) {
				return false;
			} else if (expireTime == 0 || expireTime == -2) {
				return false;
			}
			// 将新值设置给原键
			vo.set(key, value);
			if (expireTime > 0) {
				// 将大于零的超时时间重新设置给原键
				return expire(key, expireTime);
			}
			return false;
		} catch (RedisConnectionFailureException e) {
			log.error("redis连接失败", e);
			throw new GlobalException(GlobalConstants.REDIS_CONNECTION_ERR.getCode(), GlobalConstants.REDIS_CONNECTION_ERR.getMessage());
		} catch (Exception e) {
			log.error("redis修改值时出错", e);
			throw new GlobalException(GlobalConstants.REDIS_UPDATE_VALUE_ERR.getCode(), GlobalConstants.REDIS_UPDATE_VALUE_ERR.getMessage());
		}
	}
}