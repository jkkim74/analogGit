package com.skplanet.bisportal.service.bip;

import com.skplanet.bisportal.common.model.Condition;
import com.skplanet.bisportal.common.model.JqGridRequest;
import com.skplanet.bisportal.common.model.SummaryReportRow;
import com.skplanet.bisportal.model.bip.EisSvcComCd;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

/**
 * The SummaryReportService interface.
 * 
 * @author sjune
 */
public interface SummaryReportService {
	/**
	 * serviceId로 요약 리포트 지표정보를 조회한다.
	 * 
	 * @param serviceId
	 * @return
	 */
	List<EisSvcComCd> getMeasuresByServiceId(Long serviceId);

	/**
	 * 일별 지표 실적 리스트
	 * 
	 * @param condition
	 * @return
	 */
	List<SummaryReportRow> getSummaryDailyResult(Condition condition) throws Exception;

	/**
	 * 주별 지표 실적 리스트
	 *
	 * @param condition
	 * @return
	 */
	List<SummaryReportRow> getSummaryWeeklyResult(Condition condition) throws Exception;

	/**
	 * 월별 지표 실적 리스트
	 *
	 * @param condition
	 * @return
	 */
	List<SummaryReportRow> getSummaryMonthlyResult(Condition condition) throws Exception;

	/**
	 * 월별 지표 실적 리스트
	 *
	 * @param jqGridRequest 요청 파라미터.
	 * @return
	 */
	ModelAndView getSummaryReportView(JqGridRequest jqGridRequest) throws Exception;
}
