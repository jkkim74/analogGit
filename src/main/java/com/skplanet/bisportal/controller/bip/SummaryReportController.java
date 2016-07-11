package com.skplanet.bisportal.controller.bip;

import com.skplanet.bisportal.common.model.Condition;
import com.skplanet.bisportal.common.model.SummaryReportRow;
import com.skplanet.bisportal.model.bip.EisSvcComCd;
import com.skplanet.bisportal.service.bip.SummaryReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * The SummaryReportController class.
 * 
 * @author sjune
 */
@Controller
@RequestMapping("/summaryReport")
public class SummaryReportController {
	@Autowired
	private SummaryReportService summaryReportServiceImpl;

	/**
	 * 요약리포트 지표 정보 조회
	 * 
	 * @param serviceId
	 * @return
	 */
	@RequestMapping(value = "/measures/{serviceId}", method = RequestMethod.GET)
	@ResponseBody
	public List<EisSvcComCd> getMeasuresByServiceId(@PathVariable Long serviceId) {
		return summaryReportServiceImpl.getMeasuresByServiceId(serviceId);
	}

	/**
	 * 요약리포트 일별 조회
	 * 
	 * @param condition
	 * @return
	 */
	@RequestMapping(value = "/result/daily", method = RequestMethod.POST)
	@ResponseBody
	public List<SummaryReportRow> getSummaryDailyResult(@RequestBody Condition condition) throws Exception {
		return summaryReportServiceImpl.getSummaryDailyResult(condition);
	}

	/**
	 * 요약리포트 주별 조회
	 * 
	 * @param condition
	 * @return
	 */
	@RequestMapping(value = "/result/weekly", method = RequestMethod.POST)
	@ResponseBody
	public List<SummaryReportRow> getSummaryWeeklyResult(@RequestBody Condition condition) throws Exception {
		return summaryReportServiceImpl.getSummaryWeeklyResult(condition);
	}

	/**
	 * 요약리포트 월별 조회
	 * 
	 * @param condition
	 * @return
	 */
	@RequestMapping(value = "/result/monthly", method = RequestMethod.POST)
	@ResponseBody
	public List<SummaryReportRow> getSummaryMonthlyResult(@RequestBody Condition condition) throws Exception {
		return summaryReportServiceImpl.getSummaryMonthlyResult(condition);
	}
}
