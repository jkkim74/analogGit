package com.skplanet.bisportal.controller.ocb;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.skplanet.bisportal.common.controller.ReportController;
import com.skplanet.bisportal.common.model.JqGridRequest;
import com.skplanet.bisportal.common.model.JqGridResponse;
import com.skplanet.bisportal.model.ocb.ObsVstExcptPushSta;
import com.skplanet.bisportal.service.ocb.VisitExceptPushService;

/**
 * cookatrice
 */
@Controller
@RequestMapping("/ocb/visitExceptPush")
public class VisitExceptPushController extends ReportController {
	@Autowired
	private VisitExceptPushService visitExceptPushServiceImpl;

	/**
	 * 방문개요 - grid(세로형)
	 * 
	 * @param jqGridRequest
	 * @return
	 */
	@RequestMapping(value = "/outline/grid", method = RequestMethod.GET)
	@ResponseBody
	public JqGridResponse<ObsVstExcptPushSta> getVisitsExceptPushOutlineForGrid(JqGridRequest jqGridRequest) {
		JqGridResponse<ObsVstExcptPushSta> jqGridResponse = new JqGridResponse<>();
		jqGridResponse.setRows(visitExceptPushServiceImpl.getVisitsExceptPushOutlineForGrid(jqGridRequest));
		return jqGridResponse;
	}

	@RequestMapping(value = "/downloadExcelForOutline", method = RequestMethod.POST)
	public ModelAndView downloadExcelForOutline(JqGridRequest jqGridRequest) {
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("visitExceptPushOutlineExcelView");
		modelAndView.addObject("visitExceptPushOutlineData",
				visitExceptPushServiceImpl.getVisitsExceptPushOutlineForGrid(jqGridRequest));
		return modelAndView;
	}
}
