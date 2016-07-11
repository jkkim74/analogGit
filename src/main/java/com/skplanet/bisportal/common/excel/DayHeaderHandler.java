package com.skplanet.bisportal.common.excel;

import java.util.List;

import lombok.extern.slf4j.Slf4j;

import com.skplanet.bisportal.common.model.JqGridRequest;
import com.skplanet.bisportal.common.utils.DateUtil;
import com.skplanet.bisportal.model.bip.BpmDayStrdInfo;
import com.skplanet.bisportal.model.bip.SummaryHeader;

/**
 * The DayHeaderHandler class.
 * 
 * @author HoJin-Ha (mimul@wiseeco.com)
 * 
 */
@Slf4j
public class DayHeaderHandler implements HeaderHandler {
	/**
	 * 요약 페이지 일간 엑셀 헤더 저장.
	 * 
	 * @param jqGridRequest
	 *            엘셀 다운로드 요청 정보.
	 * @param summaryHeader
	 *            일간 주기 데이터.
	 * @return void
	 */
	@Override
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public String[][] handleHeader(JqGridRequest jqGridRequest, SummaryHeader summaryHeader) throws Exception {
		String[][] headers;
		try {
			String changeFormat = jqGridRequest.getBasicDate().substring(0, 4) + "."
					+ jqGridRequest.getBasicDate().substring(4, 6) + "." + jqGridRequest.getBasicDate().substring(6);
			List<BpmDayStrdInfo> bpmDayStrdInfos = (List<BpmDayStrdInfo>) summaryHeader.getPeriods();
			int bpmDayStrdInfosSize = bpmDayStrdInfos.size();
			headers = new String[2][bpmDayStrdInfosSize + 8];
			for (int i = 0; i < bpmDayStrdInfosSize; i++) {
				String dlyStrdDt = bpmDayStrdInfos.get(i).getDlyStrdDt();
				headers[1][i] = dlyStrdDt.substring(0, 4) + "." + dlyStrdDt.substring(4, 6) + "."
						+ dlyStrdDt.substring(6);
			}
			headers[0][0] = "8일간 추이";
			headers[0][bpmDayStrdInfosSize] = "지표";
			headers[0][bpmDayStrdInfosSize + 1] = "기준일(" + changeFormat + ")";
			String oneDayAgo = DateUtil.addDays(jqGridRequest.getBasicDate(), -1);
			headers[0][bpmDayStrdInfosSize + 2] = "1일전(" + oneDayAgo.substring(0, 4) + "." + oneDayAgo.substring(4, 6)
					+ "." + oneDayAgo.substring(6) + ")";
			headers[0][bpmDayStrdInfosSize + 3] = "1일전 비교증감치";
			String oneWeekAgo = DateUtil.addDays(jqGridRequest.getBasicDate(), -7);
			headers[0][bpmDayStrdInfosSize + 4] = "1주전(" + oneWeekAgo.substring(0, 4) + "."
					+ oneWeekAgo.substring(4, 6) + "." + oneWeekAgo.substring(6) + ")";
			headers[0][bpmDayStrdInfosSize + 5] = "1주전 비교증감치";
			String oneYearAgo = DateUtil.addMonths(jqGridRequest.getBasicDate(), -1);
			headers[0][bpmDayStrdInfosSize + 6] = "1달전(" + oneYearAgo.substring(0, 4) + "."
					+ oneYearAgo.substring(4, 6) + "." + oneYearAgo.substring(6) + ")";
			headers[0][bpmDayStrdInfosSize + 7] = "1달전 비교증감치";
		} catch (Exception e) {
			log.error("handleHeader()", e);
			throw new Exception("day header added error!");
		}
		return headers;
	}
}
