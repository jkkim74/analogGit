package com.skplanet.bisportal.controller.ocb;

import com.skplanet.bisportal.common.controller.ReportController;
import com.skplanet.bisportal.common.model.ChartRequest;
import com.skplanet.bisportal.common.model.JqGridRequest;
import com.skplanet.bisportal.common.model.JqGridResponse;
import com.skplanet.bisportal.model.ocb.*;
import com.skplanet.bisportal.service.ocb.VisitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

/**
 * The VisitController class.
 */
@Controller
@RequestMapping("/ocb/visit")
public class VisitController extends ReportController {
	@Autowired
	private VisitService visitServiceImpl;

	/**
	 * 방문개요 - grid(세로형)
	 * 
	 * @param jqGridRequest
	 * @return
	 */
	@RequestMapping(value = "/outline/grid", method = RequestMethod.GET)
	@ResponseBody
	public JqGridResponse<ObsVstSta> getVisitsOutlineForGrid(JqGridRequest jqGridRequest) {
		JqGridResponse<ObsVstSta> jqGridResponse = new JqGridResponse<>();
		jqGridResponse.setRows(visitServiceImpl.getVisitorListForGrid(jqGridRequest));
		return jqGridResponse;
	}

	@RequestMapping(value = "/downloadExcelForOutline", method = RequestMethod.POST)
	public ModelAndView downloadExcelForOutline(JqGridRequest jqGridRequest) {
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("visitOutlineExcelView");
		modelAndView.addObject("visitOutlineData", visitServiceImpl.getVisitorListForGrid(jqGridRequest));
		return modelAndView;
	}

	/**
	 * 방문개요 - grid(가로형)
	 * 
	 * @param jqGridRequest
	 * @return
	 */
	@RequestMapping("/outline/grid2")
	@ResponseBody
	public JqGridResponse<ObsVstSta> getVisitsOutlineForGrid2(JqGridRequest jqGridRequest) {
		JqGridResponse<ObsVstSta> jqGridResponse = new JqGridResponse<>();
		jqGridResponse.setRows(visitServiceImpl.getVisitorListForGrid2(jqGridRequest));
		if (org.springframework.util.StringUtils.isEmpty(jqGridRequest.getSidx())) {
			jqGridRequest.setSidx("stdDt");
			jqGridRequest.setSord("asc");
		}
		return jqGridResponse;
	}

	/**
	 * 방문개요 - chart
	 * 
	 * @param chartRequest
	 * @return
	 */
	@RequestMapping(value = "/outline/chart", method = RequestMethod.GET)
	@ResponseBody
	public List<ObsVstSta> getVisitsOutlineForChart(ChartRequest chartRequest) {
		return visitServiceImpl.getVisitorListForChart(chartRequest);
	}

	/**
	 * 방문자(성별, 연령별)
	 * 
	 * @return Collection of SalesUserCountByType
	 */
	@RequestMapping(value = "/sexage/grid", method = RequestMethod.GET)
	@ResponseBody
	public JqGridResponse<ObsVstSexAgeSta> getVisitSexAgeForGrid(JqGridRequest jqGridRequest) {
		JqGridResponse<ObsVstSexAgeSta> jqGridResponse = new JqGridResponse<>();
		jqGridResponse.setRows(visitServiceImpl.getVisitSexAgeForGrid(jqGridRequest));
		return jqGridResponse;
	}

	@RequestMapping(value = "/downloadExcelForSexAge", method = RequestMethod.POST)
	public ModelAndView downloadExcelForSexAge(JqGridRequest jqGridRequest) {
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("visitSexAgeExcelView");
		modelAndView.addObject("visitSexAgeData", visitServiceImpl.getVisitSexAgeForGrid(jqGridRequest));
		return modelAndView;
	}

	/**
	 * 방문자(언어)
	 * 
	 * @param jqGridRequest
	 * @return
	 */
	@RequestMapping(value = "/lang/grid", method = RequestMethod.GET)
	@ResponseBody
	public JqGridResponse<ObsVstLngSta> getVisitsLangForGrid(JqGridRequest jqGridRequest) {
		JqGridResponse<ObsVstLngSta> jqGridResponse = new JqGridResponse<>();
		jqGridResponse.setRows(visitServiceImpl.getVisitsLangForGrid(jqGridRequest));
		return jqGridResponse;
	}

	/**
	 * 방문자(해상도)
	 * 
	 * @param jqGridRequest
	 * @return
	 */
	@RequestMapping(value = "/rsltn/grid", method = RequestMethod.GET)
	@ResponseBody
	public JqGridResponse<ObsVstRsltnSta> getVisitsRsltnForGrid(JqGridRequest jqGridRequest) {
		JqGridResponse<ObsVstRsltnSta> jqGridResponse = new JqGridResponse<>();
		jqGridResponse.setRows(visitServiceImpl.getVisitsRsltnForGrid(jqGridRequest));
		return jqGridResponse;
	}

	/**
	 * 방문자(단말 모델)
	 *
	 * @param jqGridRequest
	 * @return
	 */
	@RequestMapping(value = "/dvcmdl/grid", method = RequestMethod.GET)
	@ResponseBody
	public JqGridResponse<ObsVstDvcMdlSta> getVisitorDvcMdlForGrid(JqGridRequest jqGridRequest) {
		JqGridResponse<ObsVstDvcMdlSta> jqGridResponse = new JqGridResponse<>();
		jqGridResponse.setRows(visitServiceImpl.getVisitorDvcMdlForGrid(jqGridRequest));
		return jqGridResponse;
	}

	/**
	 * 방문자(시간대)
	 * 
	 * @param jqGridRequest
	 * @return
	 */
	@RequestMapping(value = "/timezone/grid")
	@ResponseBody
	public JqGridResponse<ObsVstTimeSta> getVisitTimeZoneForGrid(JqGridRequest jqGridRequest) {
		JqGridResponse<ObsVstTimeSta> jqGridResponse = new JqGridResponse<>();
		jqGridResponse.setRows(visitServiceImpl.getVisitTimeZoneForGrid(jqGridRequest));
		return jqGridResponse;
	}

	/**
	 * 방문자(Os)
	 * 
	 * @param jqGridRequest
	 * @return
	 */
	@RequestMapping(value = "/os/grid")
	@ResponseBody
	public JqGridResponse<ObsVstOsSta> getVisitOsForGrid(JqGridRequest jqGridRequest) {
		JqGridResponse<ObsVstOsSta> jqGridResponse = new JqGridResponse<>();
		jqGridResponse.setRows(visitServiceImpl.getVisitOsForGrid(jqGridRequest));
		return jqGridResponse;
	}

	/**
	 * 방문페이지
	 * 
	 * @param jqGridRequest
	 * @return
	 */
	@RequestMapping(value = "/page/grid")
	@ResponseBody
	public JqGridResponse<ObsVstPageSta> getVisitorsPageForGrid(JqGridRequest jqGridRequest) {
		JqGridResponse<ObsVstPageSta> jqGridResponse = new JqGridResponse<>();
		jqGridResponse.setRows(visitServiceImpl.getVisitorsPageForGrid(jqGridRequest));
		return jqGridResponse;
	}

	/**
	 * 방문페이지
	 *
	 * @param jqGridRequest
	 * @return
	 */
	@RequestMapping(value = "/tracking/chart")
	@ResponseBody
	public JqGridResponse<SankeyAccessLog> getVisitorTrackingForChart(JqGridRequest jqGridRequest) {
		JqGridResponse<SankeyAccessLog> jqGridResponse = new JqGridResponse<>();
		jqGridResponse.setRows(visitServiceImpl.getVisitorTrackingForChart(jqGridRequest));
		return jqGridResponse;
	}
}
