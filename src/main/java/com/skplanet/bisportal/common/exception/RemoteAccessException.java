package com.skplanet.bisportal.common.exception;

import org.springframework.core.NestedRuntimeException;

/**
 * Created by pepsi on 15. 3. 10..
 */
public class RemoteAccessException extends NestedRuntimeException {
	private static final long serialVersionUID = 3869033639451378317L;

	/**
	 * Constructor for RemoteAccessException.
	 *
	 * @param msg
	 *            the detail message
	 */
	public RemoteAccessException(String msg) {
		super(msg);
	}

	/**
	 * Constructor for RemoteAccessException.
	 *
	 * @param msg
	 *            the detail message
	 * @param cause
	 *            the root cause (usually from using an underlying remoting API such as RMI)
	 */
	public RemoteAccessException(String msg, Throwable cause) {
		super(msg, cause);
	}
}
