package com.skplanet.ocb.util;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class ApiResponse {

	private int code;
	private String message;
	private String developerMessage;
	private Object value;
	private int totalItems;

}
