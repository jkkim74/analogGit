package com.skplanet.pandora.model;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class ApiResponse {

	public static final String DEFAULT_TYPE = "https://httpstatuses.com/200";
	public static final int DEFAULT_CODE = 200;

	String type;
	int code;
	String message;
	String developerMessage;
	String requestUrl;
	Object value;

}
