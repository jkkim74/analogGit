package com.skplanet.pandora.controller;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.skplanet.pandora.common.BizException;
import com.skplanet.pandora.model.ApiResponse;

@RestControllerAdvice
public class ExceptionResolver {

	@ExceptionHandler(Throwable.class)
	@ResponseStatus
	public ApiResponse handleException(Throwable t) {
		return ApiResponse.builder().developerMessage(t.toString()).build();
	}

	@ExceptionHandler(BizException.class)
	@ResponseStatus
	public ApiResponse handleBizException(BizException ex) {
		return ApiResponse.builder().code(900).message(ex.getMessage()).developerMessage(ex.toString()).build();
	}

}
