package com.skplanet.bisportal.controller.tmap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.skplanet.bisportal.common.controller.ReportController;
import com.skplanet.bisportal.common.model.JqGridRequest;
import com.skplanet.bisportal.common.model.JqGridResponse;
import com.skplanet.bisportal.model.tmap.TmapDayKpi;
import com.skplanet.bisportal.model.tmap.TmapMonKpiStc;
import com.skplanet.bisportal.service.tmap.KpiService;

/**
 * The VisitController class.
 */
@Controller
@RequestMapping("/tmap/kpi")
public class KpiController extends ReportController {
	@Autowired
	private KpiService kpiServiceImpl;

	/**
	 * 일별 KPI - Chart Data
	 *
	 * @param jqGridRequest
	 * @return
	 */
	@RequestMapping(value = "/day/chart", method = RequestMethod.GET)
	@ResponseBody
	public JqGridResponse<TmapDayKpi> getDayKpiForChart(JqGridRequest jqGridRequest) {
		JqGridResponse<TmapDayKpi> jqGridResponse = new JqGridResponse<>();
		jqGridResponse.setRows(kpiServiceImpl.getDayKpiForChart(jqGridRequest));
		return jqGridResponse;
	}

	/**
	 * 일별 KPI - Grid Data
	 *
	 * @param jqGridRequest
	 * @return
	 */
	@RequestMapping(value = "/day/grid", method = RequestMethod.GET)
	@ResponseBody
	public JqGridResponse<TmapDayKpi> getDayKpiForGrid(JqGridRequest jqGridRequest) {
		JqGridResponse<TmapDayKpi> jqGridResponse = new JqGridResponse<>();
		jqGridResponse.setRows(kpiServiceImpl.getDayKpiForGrid(jqGridRequest));
		return jqGridResponse;
	}

	/**
	 * 일별 KPI - Excel Data
	 *
	 * @param jqGridRequest
	 * @return
	 */
	@RequestMapping(value = "/downloadExcelForDayKpi", method = RequestMethod.POST)
	public ModelAndView downloadExcelForDayKpi(JqGridRequest jqGridRequest) {
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("dayKpiExcelView");
		modelAndView.addObject("dayKpiData", kpiServiceImpl.getDayKpiForGrid(jqGridRequest));
		return modelAndView;
	}

	/**
	 * 월별KPI관리 조회
	 * @param jqGridRequest
	 * @return
	 */
	@RequestMapping(value = "/mon/grid", method = RequestMethod.GET)
	@ResponseBody
	public JqGridResponse<TmapMonKpiStc> getMonKpiForGrid(JqGridRequest jqGridRequest){
		JqGridResponse<TmapMonKpiStc> jqGridResponse = new JqGridResponse<>();
		jqGridResponse.setRows(kpiServiceImpl.getMonthKpiForGrid(jqGridRequest));
		return jqGridResponse;
	}

	/**
	 * 월별KPI관리 엑셀 다운로드
	 *
	 * @param jqGridRequest
	 * @return
	 */
	@RequestMapping(value = "/mon/downloadExcel", method = RequestMethod.POST)
	public ModelAndView downloadExcelForMon(JqGridRequest jqGridRequest) {
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("monKpiView");
		modelAndView.addObject("monKpiData", kpiServiceImpl.getMonthKpiForGrid(jqGridRequest));
		return modelAndView;
	}
}
