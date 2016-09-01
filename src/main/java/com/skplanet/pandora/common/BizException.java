package com.skplanet.pandora.common;

import org.springframework.core.NestedRuntimeException;

public class BizException extends NestedRuntimeException {

	private static final long serialVersionUID = 3623560862692438305L;

	public BizException(String msg) {
		super(msg);
	}

	public BizException(String msg, Throwable cause) {
		super(msg, cause);
	}
	
}
