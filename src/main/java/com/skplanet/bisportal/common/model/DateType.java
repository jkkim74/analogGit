package com.skplanet.bisportal.common.model;

import com.skplanet.bisportal.common.consts.Constants;

/**
 * Created by pepsi on 2014. 5. 12..
 */
public enum DateType {
	DAY(Constants.DATE_DAY), WEEK(Constants.DATE_WEEK), MONTH(Constants.DATE_MONTH);

	private final String key;

	DateType(String key) {
		this.key = key;
	}

	public String getKey() {
		return key;
	}

}
