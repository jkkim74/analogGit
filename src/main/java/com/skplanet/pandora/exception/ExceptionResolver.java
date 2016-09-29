package com.skplanet.pandora.exception;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.skplanet.pandora.model.ApiResponse;

import lombok.extern.slf4j.Slf4j;

@RestControllerAdvice
@Slf4j
public class ExceptionResolver {

	@ExceptionHandler(Throwable.class)
	@ResponseStatus
	public ApiResponse handleException(Throwable t) {
		log.error("", t);
		t.printStackTrace();
		return ApiResponse.builder().developerMessage(t.toString()).build();
	}

	@ExceptionHandler(AccessDeniedException.class)
	@ResponseStatus(HttpStatus.FORBIDDEN)
	public ApiResponse handleAccessDeniedException(AccessDeniedException ex) {
		log.info("", ex);
		return ApiResponse.builder().developerMessage(ex.toString()).build();
	}

	@ExceptionHandler(BizException.class)
	@ResponseStatus
	public ApiResponse handleBizException(BizException ex) {
		log.warn("", ex);
		return ApiResponse.builder().code(900).message(ex.getMessage()).developerMessage(ex.toString()).build();
	}

}
