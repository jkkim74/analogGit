package com.skplanet.bisportal.common.utils;

/**
 * Created by pepsi on 15. 1. 14..
 */
public class Objects {
	/**
	 * Helps to avoid using {@code @SuppressWarnings({"unchecked"})} when casting to a generic type.
	 */
	@SuppressWarnings({"unchecked"})
	public static <T> T uncheckedCast(Object obj) {
		return (T) obj;
	}
}
