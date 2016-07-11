package com.skplanet.bisportal.controller.syrup;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.skplanet.bisportal.common.controller.ReportController;
import com.skplanet.bisportal.common.model.JqGridRequest;
import com.skplanet.bisportal.common.model.JqGridResponse;
import com.skplanet.bisportal.common.model.OlapDimensionRequest;
import com.skplanet.bisportal.model.syrup.SmwAppClickN;
import com.skplanet.bisportal.model.syrup.SmwAppExec;
import com.skplanet.bisportal.model.syrup.SmwSyrupDauFunnels;
import com.skplanet.bisportal.service.syrup.VisitSituationService;

/**
 * Created by cookatrice on 15. 1. 8..
 */
@Controller
@RequestMapping("/syrup/visitSituation")
public class VisitSituationController extends ReportController {
	@Autowired
	private VisitSituationService visitSituationServiceImpl;

	/**
	 * App 방문현황
	 * 
	 * @param olapDimensionRequest
	 * @return
	 */
	@RequestMapping(value = "/app", method = RequestMethod.GET)
	@ResponseBody
	public JqGridResponse<SmwAppExec> getAppVisitSituation(OlapDimensionRequest olapDimensionRequest) {
		JqGridResponse<SmwAppExec> jqGridResponse = new JqGridResponse<>();
		jqGridResponse.setRows(visitSituationServiceImpl.getAppVisitSituation(olapDimensionRequest));

		return jqGridResponse;
	}

	/**
	 * 메뉴별 방문현황
	 * 
	 * @param olapDimensionRequest
	 * @return
	 */
	@RequestMapping(value = "/menu", method = RequestMethod.GET)
	@ResponseBody
	public JqGridResponse<SmwAppClickN> getMenuVisitSituation(OlapDimensionRequest olapDimensionRequest) {
		JqGridResponse<SmwAppClickN> jqGridResponse = new JqGridResponse<>();
		jqGridResponse.setRows(visitSituationServiceImpl.getMenuVisitSituation(olapDimensionRequest));

		return jqGridResponse;
	}

	/**
	 * Syrup DAU 기준 유입경로
	 *
	 * @param jqGridRequest
	 * @return
	 */
	@RequestMapping(value = "/inflRt", method = RequestMethod.GET)
	@ResponseBody
	public JqGridResponse<SmwSyrupDauFunnels> getInflRtVisitSituation(JqGridRequest jqGridRequest) {
		JqGridResponse<SmwSyrupDauFunnels> jqGridResponse = new JqGridResponse<>();
		jqGridResponse.setRows(visitSituationServiceImpl.getInflRtVisitSituation(jqGridRequest));
		return jqGridResponse;
	}

	/**
	 * Syrup DAU - pivot(세로형)
	 *
	 * @param jqGridRequest
	 * @return
	 */
	@RequestMapping(value = "/inflRt/pivot", method = RequestMethod.GET)
	@ResponseBody
	public JqGridResponse<SmwSyrupDauFunnels> getSyrupDauForPivot(JqGridRequest jqGridRequest) {
		JqGridResponse<SmwSyrupDauFunnels> jqGridResponse = new JqGridResponse<>();
		jqGridResponse.setRows(visitSituationServiceImpl.getInflRtVisitSituation(jqGridRequest));

		return jqGridResponse;
	}

	/**
	 * Syrup DAU 기준 유입경로 - Excel Data
	 *
	 * @param jqGridRequest
	 * @return
	 */
	@RequestMapping(value = "/downloadExcelForInflRtVisitSituation", method = RequestMethod.POST)
	public ModelAndView downloadExcelForInflRtVisitSituation(JqGridRequest jqGridRequest) {
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("inflRtVisitSituationExcelView");
		modelAndView.addObject("inflRtVisitSituationData", visitSituationServiceImpl.getInflRtVisitSituation(jqGridRequest));
		return modelAndView;
	}
}
