package com.skplanet.bisportal.common.model;

import com.fasterxml.jackson.annotation.JsonValue;
import com.skplanet.bisportal.common.consts.Constants;

/**
 * The ReportDateType enum
 *
 * @author sjune
 */
public enum ReportDateType {
	DAY(Constants.DATE_DAY), WEEK(Constants.DATE_WEEK), MONTH(Constants.DATE_MONTH), SUM("sum");

	private String value;

	ReportDateType(String value) {
		this.value = value;
	}

	@JsonValue
	public String value() {
		return this.value;
	}

	@Override
	public String toString() {
		return name().toLowerCase();
	}
}
