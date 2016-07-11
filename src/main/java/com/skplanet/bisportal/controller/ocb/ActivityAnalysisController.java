package com.skplanet.bisportal.controller.ocb;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.skplanet.bisportal.common.controller.ReportController;
import com.skplanet.bisportal.common.model.JqGridRequest;
import com.skplanet.bisportal.common.model.JqGridResponse;
import com.skplanet.bisportal.model.ocb.ObsActvCustRpt;
import com.skplanet.bisportal.model.ocb.ObsActvJoinsRpt;
import com.skplanet.bisportal.service.ocb.ActivityAnalysisService;

/**
 * Created by cookatrice on 2014. 8. 11..
 */
@Controller
@RequestMapping("/ocb/khub/activityAnalysis")
public class ActivityAnalysisController extends ReportController {
    @Autowired
    private ActivityAnalysisService activityAnalysisServiceImpl;

	/**
	 * 활성화고객보고서 조회 - pivot
	 *
	 * @param jqGridRequest
	 * @return
	 */
	@RequestMapping(value = "/activityCustomerReport/pivot", method = RequestMethod.GET)
	@ResponseBody
	public JqGridResponse<ObsActvCustRpt> getActivityCustomerReportForPivot(JqGridRequest jqGridRequest) {
		JqGridResponse<ObsActvCustRpt> jqGridResponse = new JqGridResponse<>();
		jqGridResponse.setRows(activityAnalysisServiceImpl.getActivityCustomerReportForPivot(jqGridRequest));
		return jqGridResponse;
	}

	/**
	 * 활성화가맹점보고서 조회 - pivot
	 *
	 * @param jqGridRequest
	 * @return
	 */
	@RequestMapping(value = "/activityJoinsReport/pivot", method = RequestMethod.GET)
	@ResponseBody
	public JqGridResponse<ObsActvJoinsRpt> getActivityJoinsReportForPivot(JqGridRequest jqGridRequest) {
		JqGridResponse<ObsActvJoinsRpt> jqGridResponse = new JqGridResponse<>();
		jqGridResponse.setRows(activityAnalysisServiceImpl.getActivityJoinsReportForPivot(jqGridRequest));
		return jqGridResponse;
	}

}
