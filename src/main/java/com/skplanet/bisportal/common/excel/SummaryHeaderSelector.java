package com.skplanet.bisportal.common.excel;

import com.google.common.collect.Maps;
import com.google.inject.Singleton;
import com.skplanet.bisportal.common.consts.Constants;
import com.skplanet.bisportal.common.model.JqGridRequest;
import com.skplanet.bisportal.model.bip.SummaryHeader;

import java.util.Map;

/**
 * The SummaryHeaderSelector(요액페이지 헤더 생성 처리) class.
 *
 * @author HoJin-Ha (mimul@wiseeco.com)
 *
 */
@Singleton
public class SummaryHeaderSelector {
	private Map<String, HeaderHandler> headerMap;

	public SummaryHeaderSelector() {
		this.headerMap = Maps.newHashMap();
		this.headerMap.put(Constants.DATE_DAY, new DayHeaderHandler());
		this.headerMap.put(Constants.DATE_WEEK, new WeekHeaderHandler());
		this.headerMap.put(Constants.DATE_MONTH, new MonthHeaderHandler());
	}

	@SuppressWarnings("rawtypes")
	public String[][] getHandleHeader(JqGridRequest jqGridRequest, SummaryHeader summaryHeader) throws Exception {
		return this.headerMap.get(jqGridRequest.getDateType().value()).handleHeader(jqGridRequest, summaryHeader);
	}
}
