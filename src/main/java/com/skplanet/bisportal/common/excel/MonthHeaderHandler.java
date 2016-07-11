package com.skplanet.bisportal.common.excel;

import java.util.List;

import lombok.extern.slf4j.Slf4j;

import com.skplanet.bisportal.common.model.JqGridRequest;
import com.skplanet.bisportal.common.utils.DateUtil;
import com.skplanet.bisportal.model.bip.BpmMthStrdInfo;
import com.skplanet.bisportal.model.bip.SummaryHeader;

/**
 * The MonthHeaderHandler class.
 * 
 * @author HoJin-Ha (mimul@wiseeco.com)
 * 
 */
@Slf4j
public class MonthHeaderHandler implements HeaderHandler {
	/**
	 * 요약 페이지 월간 엑셀 헤더 저장.
	 * 
	 * @param jqGridRequest
	 *            엘셀 다운로드 요청 정보.
	 * @param summaryHeader
	 *            월간 주기 데이터.
	 * @return void
	 */
	@Override
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public String[][] handleHeader(JqGridRequest jqGridRequest, SummaryHeader summaryHeader) throws Exception {
		String[][] headers;
		try {
			String basicMonth = jqGridRequest.getBasicDate().substring(0, 4) + "."
					+ jqGridRequest.getBasicDate().substring(4, 6);
			List<BpmMthStrdInfo> bpmMthStrdInfos = (List<BpmMthStrdInfo>) summaryHeader.getPeriods();
			int bpmMthStrdInfosSize = bpmMthStrdInfos.size();
			headers = new String[2][bpmMthStrdInfosSize + 6];
			for (int i = 0; i < bpmMthStrdInfosSize; i++) {
				String mthStcStrdYm = bpmMthStrdInfos.get(i).getMthStcStrdYm();
				headers[1][i] = mthStcStrdYm.substring(0, 4) + "." + mthStcStrdYm.substring(4, 6);
			}
			headers[0][0] = "13개월 추이";
			headers[0][bpmMthStrdInfosSize] = "지표";
			headers[0][bpmMthStrdInfosSize + 1] = "기준월(" + basicMonth + ")";
			String oneMonthAgo = DateUtil.addMonths(jqGridRequest.getBasicDate(), -1);
			headers[0][bpmMthStrdInfosSize + 2] = "1달전(" + oneMonthAgo.substring(0, 4) + "."
					+ oneMonthAgo.substring(4, 6) + ")";
			headers[0][bpmMthStrdInfosSize + 3] = "1달전 비교증감치";
			String oneYearAgo = DateUtil.addYears(jqGridRequest.getBasicDate(), -1);
			headers[0][bpmMthStrdInfosSize + 4] = "1년전(" + oneYearAgo.substring(0, 4) + "."
					+ oneYearAgo.substring(4, 6) + ")";
			headers[0][bpmMthStrdInfosSize + 5] = "1년전 비교증감치";
		} catch (Exception e) {
			log.error("exec()", e);
			throw new Exception("month header added error!");
		}
		return headers;
	}
}
