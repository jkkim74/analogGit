package com.skplanet.pandora.model;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class ApiResponse {

	int code;
	String message;
	String developerMessage;
	Object value;
	int totalRecords;

}
