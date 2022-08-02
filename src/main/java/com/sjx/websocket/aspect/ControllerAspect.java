package com.sjx.websocket.aspect;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sjx.websocket.entity.http.Response;
import com.sjx.websocket.exception.GlobalException;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

/**
 * SunJingxuan
 * 2022 \ 08 \ 01
 */
@Component
@Aspect
@Slf4j
public class ControllerAspect {

	@Resource
	private ObjectMapper objectMapper;

	@Around("execution(* com.sjx.websocket.controller..*.*(..))")
	public Object logAround(ProceedingJoinPoint pjp) throws Throwable {
		long start = System.currentTimeMillis();
		RequestAttributes ra = RequestContextHolder.getRequestAttributes();
		ServletRequestAttributes sra = (ServletRequestAttributes) ra;
		HttpServletRequest request = sra.getRequest();
		String url = request.getRequestURL().toString();
		String method = request.getMethod();
		Object[] args = pjp.getArgs();
		List<Object> argList = new ArrayList<>();
		if (args != null) {
			for (Object arg : args) {
				if (!(arg instanceof HttpServletRequest) && !(arg instanceof HttpServletResponse)) {
					argList.add(arg);
				}
			}
		}
		log.info("controller [url:{}] [method:{}] [args:{}]", url, method, objectMapper.writeValueAsString(argList));
		int responeCode = 200;
		Object result;
		try {
			Object resultObj;
			result = pjp.proceed();
			if (result instanceof ResponseEntity) {
				ResponseEntity<Response> responseEntity = (ResponseEntity<Response>) result;
				responeCode = responseEntity.getStatusCodeValue();
				resultObj = responseEntity.getBody();
			} else {
				resultObj = result;
			}
			log.info("controller [url:{}] [method:{}] [exec time:{}] [responsecode:{}] [response:{}]", url, method, (System.currentTimeMillis() - start) / 1000 , responeCode, objectMapper.writeValueAsString(resultObj));
		} catch (GlobalException e) {
			log.error("controller [url:{}] [method:{}] [exec time:{}] [error code:{}] [error message:{}]", url, method, (System.currentTimeMillis() - start) / 1000, e.getCode(), e.getMessage());
			throw e;
		} catch (Exception e) {
			log.error("controller [url:{}] [method:{}] [exec time:{}] [error code:exception] [error message:{}]", url, method, (System.currentTimeMillis() - start) / 1000, e.getMessage());
			throw e;
		}
		return result;
	}
}
