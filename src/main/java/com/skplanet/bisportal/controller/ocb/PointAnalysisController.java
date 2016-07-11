package com.skplanet.bisportal.controller.ocb;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.skplanet.bisportal.common.controller.ReportController;
import com.skplanet.bisportal.common.model.JqGridRequest;
import com.skplanet.bisportal.common.model.JqGridResponse;
import com.skplanet.bisportal.model.ocb.ObsMbilAchvRpt;
import com.skplanet.bisportal.model.ocb.ObsRsrvPntRpt;
import com.skplanet.bisportal.model.ocb.ObsTotPntRpt;
import com.skplanet.bisportal.model.ocb.ObsUsePntRpt;
import com.skplanet.bisportal.service.ocb.PointAnalysisService;

/**
 * Created by cookatrice on 2014. 8. 11..
 */
@Controller
@RequestMapping("/ocb/khub/pointAnalysis")
public class PointAnalysisController extends ReportController {
	@Autowired
	private PointAnalysisService pointAnalysisServiceImpl;

	/**
	 * 전체포인트보고서 조회 - pivot
	 *
	 * @param jqGridRequest
	 * @return
	 */
	@RequestMapping(value = "/totalPointReport/pivot", method = RequestMethod.GET)
	@ResponseBody
	public JqGridResponse<ObsTotPntRpt> getTotalPointReportForPivot(JqGridRequest jqGridRequest) {
		if (jqGridRequest.getDateType().value() == "month") {
			jqGridRequest.setStartDate(jqGridRequest.getStartDate() + "01");
			jqGridRequest.setEndDate(jqGridRequest.getEndDate() + "31");
		}
		JqGridResponse<ObsTotPntRpt> jqGridResponse = new JqGridResponse<>();
		jqGridResponse.setRows(pointAnalysisServiceImpl.getTotalPointReportForPivot(jqGridRequest));
		return jqGridResponse;
	}

	/**
	 * 적립포인트보고서 조회 - pivot
	 *
	 * @param jqGridRequest
	 * @return
	 */
	@RequestMapping(value = "/reservingPointReport/pivot", method = RequestMethod.GET)
	@ResponseBody
	public JqGridResponse<ObsRsrvPntRpt> getReservingPointReportForPivot(JqGridRequest jqGridRequest) {
		if (jqGridRequest.getDateType().value() == "month") {
			jqGridRequest.setStartDate(jqGridRequest.getStartDate() + "01");
			jqGridRequest.setEndDate(jqGridRequest.getEndDate() + "31");
		}
		JqGridResponse<ObsRsrvPntRpt> jqGridResponse = new JqGridResponse<>();
		jqGridResponse.setRows(pointAnalysisServiceImpl.getReservingPointReportForPivot(jqGridRequest));
		return jqGridResponse;
	}

	/**
	 * 사용포인트보고서 조회 - pivot
	 *
	 * @param jqGridRequest
	 * @return
	 */
	@RequestMapping(value = "/usePointReport/pivot", method = RequestMethod.GET)
	@ResponseBody
	public JqGridResponse<ObsUsePntRpt> getUsePointReportForPivot(JqGridRequest jqGridRequest) {
		if (jqGridRequest.getDateType().value() == "month") {
			jqGridRequest.setStartDate(jqGridRequest.getStartDate() + "01");
			jqGridRequest.setEndDate(jqGridRequest.getEndDate() + "31");
		}
		JqGridResponse<ObsUsePntRpt> jqGridResponse = new JqGridResponse<>();
		jqGridResponse.setRows(pointAnalysisServiceImpl.getUsePointReportForPivot(jqGridRequest));
		return jqGridResponse;
	}

	/**
	 * 모바일실적보고서 조회 - pivot
	 *
	 * @param jqGridRequest
	 * @return
	 */
	@RequestMapping(value = "/mobileAchieveReport/pivot", method = RequestMethod.GET)
	@ResponseBody
	public JqGridResponse<ObsMbilAchvRpt> getMobileAchieveReportForPivot(JqGridRequest jqGridRequest) {
		if (jqGridRequest.getDateType().value() == "month") {
			jqGridRequest.setStartDate(jqGridRequest.getStartDate() + "01");
			jqGridRequest.setEndDate(jqGridRequest.getEndDate() + "31");
		}
		JqGridResponse<ObsMbilAchvRpt> jqGridResponse = new JqGridResponse<>();
		jqGridResponse.setRows(pointAnalysisServiceImpl.getMobileAchieveReportForPivot(jqGridRequest));
		return jqGridResponse;
	}
}
