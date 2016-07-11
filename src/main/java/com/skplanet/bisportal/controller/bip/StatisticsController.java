package com.skplanet.bisportal.controller.bip;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.skplanet.bisportal.common.controller.ReportController;
import com.skplanet.bisportal.common.model.JqGridRequest;
import com.skplanet.bisportal.common.model.JqGridResponse;
import com.skplanet.bisportal.model.bip.DayVisitor;
import com.skplanet.bisportal.service.bip.StatisticsService;

/**
 * Created by lko on 2014-09-30.
 */
@Controller
@RequestMapping("/statistics")
public class StatisticsController extends ReportController {
	@Autowired
	private StatisticsService statisticsServiceImpl;

	@RequestMapping(value = "/dayVisitor", method = RequestMethod.GET)
	public @ResponseBody JqGridResponse<DayVisitor> getDayVisitor(JqGridRequest jqGridRequest) throws Exception {
		JqGridResponse<DayVisitor> jqGridResponse = new JqGridResponse<>();
		jqGridResponse.setRows(statisticsServiceImpl.getDayVisitor(jqGridRequest));
		return jqGridResponse;
	}

	@RequestMapping(value = "/downloadExcelForDayVisitor", method = RequestMethod.POST)
	public ModelAndView downloadExcelForDayVisitor(JqGridRequest jqGridRequest) throws Exception {
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("dayVisitorExcelView");
		modelAndView.addObject("dayVisitorData", statisticsServiceImpl.getDayVisitor(jqGridRequest));
		return modelAndView;
	}
}
