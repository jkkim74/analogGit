package com.skplanet.web.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

@Configuration
public class IdmsConfig {

	@Bean
	public RestTemplate restTemplate() {
		RestTemplate template = new RestTemplate(httpRequestFactory());
		return template;
	}

	@Bean
	public ClientHttpRequestFactory httpRequestFactory() {
		// 로그인 처리가 동시에 몰리지 않을 것으로 예상하므로 기본 HttpClient 사용으로 충분. (최대 5개 연결 풀)
		HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory();

		/*
		 * IDMS 로그인 연동 연동 예제 기준으로 타임아웃은 6초이나 연동규약에서 응답이 3초를 넘어가면 로그인 통과로 처리하는
		 * 부분이 있기 때문에 (E99) 근사치로 4초 잡음.
		 */
		factory.setConnectTimeout(4000);
		factory.setReadTimeout(4000);

		return factory;
	}

}
