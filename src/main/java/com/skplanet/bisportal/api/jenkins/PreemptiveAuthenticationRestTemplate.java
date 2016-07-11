package com.skplanet.bisportal.api.jenkins;

import org.apache.http.HttpHost;
import org.springframework.web.client.RestOperations;
import org.springframework.web.client.RestTemplate;

/**
 * Created by mimul on 2015. 7. 13..
 */
public class PreemptiveAuthenticationRestTemplate extends RestTemplate implements RestOperations {
	public PreemptiveAuthenticationRestTemplate() {

		super();

		setRequestFactory(new HttpComponentsClientHttpRequestFactoryPreemptiveAuthentication());
	}

	public void setCredentials(HttpHost host,
			PreemptiveAuthenticationScheme authenticationScheme,
			String username,
			String password) {

		HttpComponentsClientHttpRequestFactoryPreemptiveAuthentication requestFactory = (HttpComponentsClientHttpRequestFactoryPreemptiveAuthentication) getRequestFactory();
		requestFactory.setCredentials(host, authenticationScheme, username, password);
	}
}
