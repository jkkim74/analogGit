package com.skplanet.bisportal.common.model;

import org.apache.commons.lang.builder.ToStringBuilder;

import java.io.Serializable;
import java.util.List;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * The JqGridRequest class.
 * 
 * @author HoJin-Ha (mimul@wiseeco.com)
 * 
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class JqGridResponse<T extends Serializable> {
	private static final long serialVersionUID = 0l;
	/**
	 * Current page
	 */
	private int page;

	/**
	 * Total pages
	 */
	private int total;

	/**
	 * Total number of records
	 */
	private int records;

	/**
	 * Contains the actual data
	 */
	private List<T> rows;

	private int code; // 0: 성공, 1 - Missing argument, 2 - Invalid parameter, 3 - Data not found, 4 - Error fetching data

	private String message;//실패시 메세지 지정

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}

}
