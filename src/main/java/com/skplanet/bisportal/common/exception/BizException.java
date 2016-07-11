package com.skplanet.bisportal.common.exception;

import org.springframework.core.NestedRuntimeException;

/**
 * The BizException class.
 * 
 * @author sjune
 */
public class BizException extends NestedRuntimeException {

	private static final long serialVersionUID = 1L;

	/**
	 * Constructor for BizException.
	 * 
	 * @param msg
	 *            the detail message
	 */
	public BizException(String msg) {
		super(msg);
	}

	/**
	 * Constructor for BizException.
	 * 
	 * @param msg
	 *            the detail message
	 * @param cause
	 *            the root cause (usually from using an underlying business logic)
	 */
	public BizException(String msg, Throwable cause) {
		super(msg, cause);
	}
}
