package com.skplanet.bisportal.common.utils;

import java.io.IOException;
import java.util.Properties;

/**
 * The PropertiesUtil class.
 *
 * @author HoJin-Ha (mimul@wiseeco.com)
 *
 */
public final class PropertiesUtil {
	private static Properties props = new Properties();
	static {
		try {
			props.load(PropertiesUtil.class.getClassLoader().getResourceAsStream("config/common.properties"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private PropertiesUtil() {
	}

	public static String getProperty(String key) {
		return props.getProperty(key);
	}
}
