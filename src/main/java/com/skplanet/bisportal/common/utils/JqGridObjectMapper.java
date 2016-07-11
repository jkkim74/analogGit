package com.skplanet.bisportal.common.utils;

import java.io.Serializable;

import lombok.Data;
import lombok.EqualsAndHashCode;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * The JqGridObjectMapper class.
 * 
 * @author HoJin-Ha (mimul@wiseeco.com)
 * 
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Deprecated
public class JqGridObjectMapper implements Serializable {
	private static final long serialVersionUID = -4733225436428900720L;

	public static JqGridFilter map(String jsonString) {

		if (jsonString != null) {
			ObjectMapper mapper = new ObjectMapper();

			try {
				return mapper.readValue(jsonString, JqGridFilter.class);
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}

		return null;
	}

}
