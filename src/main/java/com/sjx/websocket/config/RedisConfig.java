package com.sjx.websocket.config;

import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;

/**
 * @author 孙敬轩
 * @version V1.0
 * @Project: redis-demo
 * @Package com.sjx.redisdemo.util.redis
 * @Description: redisTemplate：spring 封装了 RedisTemplate 对象来进行对redis的各种操作，
 *                  它支持所有的 redis 原生的 api。
 * @date 2020/1/3 09:10 星期五
 */
@EnableCaching
@Configuration
public class RedisConfig extends CachingConfigurerSupport {

    @Bean
    public RedisTemplate<String, String> redisTemplate(RedisConnectionFactory factory){
        RedisTemplate<String, String> redisTemplate = new RedisTemplate<String, String>();
        redisTemplate.setConnectionFactory(factory);
        return redisTemplate;
    }
}