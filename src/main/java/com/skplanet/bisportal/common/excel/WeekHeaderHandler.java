package com.skplanet.bisportal.common.excel;

import java.util.List;

import lombok.extern.slf4j.Slf4j;

import com.skplanet.bisportal.common.consts.Constants;
import com.skplanet.bisportal.common.model.BasicDateWeekNumber;
import com.skplanet.bisportal.common.model.JqGridRequest;
import com.skplanet.bisportal.common.utils.DateUtil;
import com.skplanet.bisportal.model.bip.BpmWkStrdInfo;
import com.skplanet.bisportal.model.bip.SummaryHeader;

/**
 * The WeekHeaderHandler class.
 * 
 * @author HoJin-Ha (mimul@wiseeco.com)
 * 
 */
@Slf4j
public class WeekHeaderHandler implements HeaderHandler {
	/**
	 * 요약 페이지 주간 엑셀 헤더 저장.
	 * 
	 * @param jqGridRequest
	 *            엘셀 다운로드 요청 정보.
	 * @param summaryHeader
	 *            주간 주기 데이터.
	 * @return void
	 */
	@Override
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public String[][] handleHeader(JqGridRequest jqGridRequest, SummaryHeader summaryHeader) throws Exception {
		String[][] headers;
		try {
			List<BpmWkStrdInfo> bpmWkStrdInfos = (List<BpmWkStrdInfo>) summaryHeader.getPeriods();
			int bpmWkStrdInfosSize = bpmWkStrdInfos.size();
			headers = new String[2][bpmWkStrdInfosSize + 6];
			for (int i = 0; i < bpmWkStrdInfosSize; i++) {
				String wkStcStrdYmw = bpmWkStrdInfos.get(i).getWkStcStrdYmw();
				headers[1][i] = wkStcStrdYmw.substring(0, 4) + "." + wkStcStrdYmw.substring(4, 6) + "."
						+ wkStcStrdYmw.substring(6);
			}
			headers[0][0] = "14주간 추이";
			headers[0][bpmWkStrdInfosSize] = "지표";
			headers[0][bpmWkStrdInfosSize + 1] = getBasicWeek(summaryHeader.getWeekNumber());
			headers[0][bpmWkStrdInfosSize + 2] = getOneWeekAgo(summaryHeader.getWeekNumber());
			headers[0][bpmWkStrdInfosSize + 3] = "1주전 비교증감치";
			headers[0][bpmWkStrdInfosSize + 4] = getOneYearAgo(summaryHeader.getWeekNumber());
			headers[0][bpmWkStrdInfosSize + 5] = "1년전 비교증감치";
		} catch (Exception e) {
			log.error("exec()", e);
			throw new Exception("week header added error!");
		}
		return headers;
	}

	private String getBasicWeek(BasicDateWeekNumber weekNumber) throws Exception {
		StringBuffer basicWeek = new StringBuffer(100);
		basicWeek.append("기준주(").append(
				DateUtil.changeFormatDate(weekNumber.getBasicStartDate(), Constants.DATE_YMD_FORMAT,
						Constants.DATE_YMD_COMMA_FORMAT));
		basicWeek.append("~").append(
				DateUtil.changeFormatDate(weekNumber.getBasicEndDate(), Constants.DATE_YMD_FORMAT,
						Constants.DATE_YMD_COMMA_FORMAT));
		basicWeek.append(")");
		return basicWeek.toString();
	}

	private String getOneWeekAgo(BasicDateWeekNumber weekNumber) throws Exception {
		StringBuffer oneWeekAgo = new StringBuffer(100);
		oneWeekAgo.append("1주전(").append(
				DateUtil.changeFormatDate(weekNumber.getOneWeekAgoStartDate(), Constants.DATE_YMD_FORMAT,
						Constants.DATE_YMD_COMMA_FORMAT));
		oneWeekAgo.append("~").append(
				DateUtil.changeFormatDate(weekNumber.getOneWeekAgoEndDate(), Constants.DATE_YMD_FORMAT,
						Constants.DATE_YMD_COMMA_FORMAT));
		oneWeekAgo.append(")");
		return oneWeekAgo.toString();
	}

	private String getOneYearAgo(BasicDateWeekNumber weekNumber) throws Exception {
		StringBuffer oneYearAgo = new StringBuffer(100);
		oneYearAgo.append("1년전(").append(
				DateUtil.changeFormatDate(weekNumber.getOneYearAgoStartDate(), Constants.DATE_YMD_FORMAT,
						Constants.DATE_YMD_COMMA_FORMAT));
		oneYearAgo.append("~").append(
				DateUtil.changeFormatDate(weekNumber.getOneYearAgoEndDate(), Constants.DATE_YMD_FORMAT,
						Constants.DATE_YMD_COMMA_FORMAT));
		oneYearAgo.append(")");
		return oneYearAgo.toString();
	}
}
