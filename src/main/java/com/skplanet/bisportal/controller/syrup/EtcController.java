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
import com.skplanet.bisportal.model.syrup.SmwPushTickerStatDtl;
import com.skplanet.bisportal.service.syrup.EtcService;

/**
 * Created by lko on 2014-12-05.
 */
@Controller
@RequestMapping("/syrup/etc")
public class EtcController extends ReportController {
	@Autowired
	private EtcService etcServiceImpl;

	@RequestMapping(value = "/pushTickerStatDtl/grid", method = RequestMethod.GET)
	@ResponseBody
	public JqGridResponse<SmwPushTickerStatDtl> getSmwPushTickerStatDtlGrid(JqGridRequest jqGridRequest) {
		JqGridResponse<SmwPushTickerStatDtl> jqGridResponse = new JqGridResponse<>();
		jqGridResponse.setRows(etcServiceImpl.getSmwPushTickerStatDtlGrid(jqGridRequest));
		return jqGridResponse;
	}

	@RequestMapping(value = "/downloadExcelForPushTickerStat", method = RequestMethod.POST)
	public ModelAndView downloadExcelForPushTickerStat(JqGridRequest jqgridRequest) throws Exception {
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("pushTickerStatExcelView");
		modelAndView.addObject("jqgridRequest", jqgridRequest);
		modelAndView
				.addObject("pushTickerStatExcelViewData", etcServiceImpl.getSmwPushTickerStatDtlGrid(jqgridRequest));
		return modelAndView;
	}
}
