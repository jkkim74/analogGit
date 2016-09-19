package com.skplanet.pandora.configuration;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import skp.bss.msg.rms.front.HttpConnectionManager;
import skp.bss.msg.rms.front.HttpMessageApi;

@Configuration
public class SmsConfig {

	@Bean
	public HttpMessageApi smsApi(@Value("${sms.url}") String url, @Value("${sms.serviceId}") String serviceId,
			@Value("${sms.appKey}") String appKey) throws MalformedURLException, URISyntaxException {

		HttpMessageApi smsApi = new HttpMessageApi();
		smsApi.setConnectionManager(new HttpConnectionManager(new URL(url), serviceId, appKey));
		return smsApi;
	}

}
