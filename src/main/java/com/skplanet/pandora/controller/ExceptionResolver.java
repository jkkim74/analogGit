package com.skplanet.pandora.controller;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.skplanet.pandora.model.ApiResponse;

@RestControllerAdvice
public class ExceptionResolver {

	@ExceptionHandler(Throwable.class)
	@ResponseStatus
	public ApiResponse handleException(Throwable t) {
		return ApiResponse.builder().code(500).message(t.getMessage()).developerMessage(t.toString()).build();
	}

}
