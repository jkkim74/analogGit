package com.skplanet.bisportal.controller.syrup;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.skplanet.bisportal.common.controller.ReportController;
import com.skplanet.bisportal.common.model.JqGridRequest;
import com.skplanet.bisportal.common.model.JqGridResponse;
import com.skplanet.bisportal.common.model.OlapDimensionRequest;
import com.skplanet.bisportal.model.syrup.CustomerSituation;
import com.skplanet.bisportal.model.syrup.SmwMbrJoin;
import com.skplanet.bisportal.service.syrup.MbrJoinService;
import com.skplanet.bisportal.service.syrup.SyrupDashboardService;

/**
 * Created by lko on 2014-11-20.
 */
@Controller
@RequestMapping("/syrup/dashboard")
public class SyrupDashboardController extends ReportController {
	@Autowired
	private MbrJoinService mbrJoinServiceImpl;

	@Autowired
	private SyrupDashboardService syrupDashboardService;

	@RequestMapping("/customerSituation")
	@ResponseBody
	public JqGridResponse<CustomerSituation> getCustomerSituationPivot(OlapDimensionRequest olapDimensionRequest) {
		JqGridResponse<CustomerSituation> jqGridResponse = new JqGridResponse<>();
		jqGridResponse.setRows(syrupDashboardService.getCustomerSituationForPivot(olapDimensionRequest));
		return jqGridResponse;
	}

	@RequestMapping(value = "/mbrjoin/grid", method = RequestMethod.GET)
	@ResponseBody
	public JqGridResponse<SmwMbrJoin> getMbrJoinForGrid(JqGridRequest jqGridRequest) {
		// TODO REFACTORING mbrJoinServiceImpl -> syrupDashboardServiceimpl
		JqGridResponse<SmwMbrJoin> jqGridResponse = new JqGridResponse<>();
		jqGridResponse.setRows(mbrJoinServiceImpl.getMbrJoinForGrid(jqGridRequest));
		return jqGridResponse;
	}
}
