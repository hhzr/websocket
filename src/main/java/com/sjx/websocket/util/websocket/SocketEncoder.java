package com.sjx.websocket.util.websocket;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sjx.websocket.entity.vo.SendMessageVO;
import lombok.extern.slf4j.Slf4j;

import javax.websocket.EncodeException;
import javax.websocket.Encoder;
import javax.websocket.EndpointConfig;

/**
 * SunJingxuan
 * 2022 \ 08 \ 01
 */
@Slf4j
public class SocketEncoder implements Encoder.Text<SendMessageVO> {

	@Override
	public String encode(SendMessageVO sendMessageVO) throws EncodeException {
		try {
			ObjectMapper objectMapper = new ObjectMapper();
			return objectMapper.writeValueAsString(sendMessageVO);
		} catch (JsonProcessingException e) {
			log.error("websocket编码器json解析异常", e);
		} catch (Exception e) {
			log.error("websocket编码器异常", e);
		}
		return null;
	}

	@Override
	public void init(EndpointConfig endpointConfig) {

	}

	@Override
	public void destroy() {

	}
}
