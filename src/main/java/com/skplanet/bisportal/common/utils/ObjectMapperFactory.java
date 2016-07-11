package com.skplanet.bisportal.common.utils;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.FactoryBean;

/**
 * Created by mimul on 2015. 6. 18..
 */
public class ObjectMapperFactory implements FactoryBean<ObjectMapper> {
	// ObjectMapper 생성비용이 높아서 싱글톤방식으로 처리.
	private static final ObjectMapper objectMapper;
	static {
		objectMapper = new ObjectMapper();
		// Model에 없는 필드 무시.
		objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
	}

	public static ObjectMapper get() {
		return objectMapper;
	}

	@Override
	public ObjectMapper getObject() throws Exception {
		return objectMapper;
	}

	@Override
	public Class<?> getObjectType() {
		return ObjectMapper.class;
	}

	@Override public boolean isSingleton() {
		return true;
	}
}
