package com.skplanet.web.configuration;

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

	@Value("${sms.url}")
	private String url;

	@Value("${sms.serviceId}")
	private String serviceId;

	@Value("${sms.appKey}")
	private String appKey;

	@Bean
	public HttpMessageApi smsApi() throws MalformedURLException, URISyntaxException {
		HttpMessageApi smsApi = new HttpMessageApi();
		smsApi.setConnectionManager(new HttpConnectionManager(new URL(url), serviceId, appKey));
		return smsApi;
	}

}
