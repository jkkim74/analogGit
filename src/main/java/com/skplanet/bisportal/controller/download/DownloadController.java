package com.skplanet.bisportal.controller.download;

import com.skplanet.bisportal.service.bip.DashboardService;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.skplanet.bisportal.common.controller.ReportController;
import com.skplanet.bisportal.common.model.JqGridRequest;
import com.skplanet.bisportal.service.bip.SummaryReportService;

/**
 * The DownloadController class.
 * 
 * @author HoJin-Ha (mimul@wiseeco.com)
 * 
 */
@Slf4j
@Controller
@RequestMapping("/download")
public class DownloadController extends ReportController {
	@Autowired
	private SummaryReportService summaryReportServiceImpl;
	@Autowired
	private DashboardService dashboardServiceImpl;

	/**
	 * PivotTable.js를 활용한 그리드의 엑셀 다운로드용 View
	 * 
	 * @param jqGridRequest
	 * @return modelAndView
	 */
	@RequestMapping(value = "/report/excel", method = RequestMethod.POST)
	public ModelAndView downloadReportExcel(JqGridRequest jqGridRequest) throws Exception {
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("pivotExcelView");
		return modelAndView;
	}

	/**
	 * PivotTable.js를 활용한 그리드의 엑셀 다운로드용 View, header가 동적으로 변하는 목적으로 사용된다.
	 *
	 * @param jqGridRequest
	 * @return modelAndView
	 */
	@RequestMapping(value = "/report/dynamicHeaderPivotExcel", method = RequestMethod.POST)
	public ModelAndView downloadReportDynamicHeaderExcel(JqGridRequest jqGridRequest) throws Exception {
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("dynamicHeaderPivotExcelView");
		return modelAndView;
	}

	/**
	 * 요약 페이지 엑셀 다운로드용 View
	 * 
	 * @param jqGridRequest
	 * @return modelAndView
	 */
	@RequestMapping(value = "/summary/excel", method = RequestMethod.POST)
	public ModelAndView downloadSummaryExcel(JqGridRequest jqGridRequest) throws Exception {
		ModelAndView modelAndView;
		try {
			modelAndView = summaryReportServiceImpl.getSummaryReportView(jqGridRequest);
		} catch (Exception e) {
			log.error("downloadSummaryExcel() {}", e);
			throw new Exception("excel download error!");
		}
		return modelAndView;
	}

	/**
	 * 경영실적 상세조회 엑셀 다운로드용 View
	 * 
	 * @param jqGridRequest
	 * @return modelAndView
	 */
	@RequestMapping(value = "/bpmResultSum/excel", method = RequestMethod.POST)
	public ModelAndView downloadBpmResultSumExcel(JqGridRequest jqGridRequest) throws Exception {
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("bossExcelView");
		return modelAndView;
	}

	/**
	 * 대시보드 리포트 페이지 엑셀 다운로드용 View
	 *
	 * @param jqGridRequest
	 * @return modelAndView
	 */
	@RequestMapping(value = "/dashboard/excel", method = RequestMethod.POST)
	public ModelAndView downloadDashboardExcel(JqGridRequest jqGridRequest) throws Exception {
		ModelAndView modelAndView;
		try {
			modelAndView = dashboardServiceImpl.getDashboardReportView(jqGridRequest);
		} catch (Exception e) {
			log.error("downloadDashboardExcel() {}", e);
			throw new Exception("excel download error!");
		}
		return modelAndView;
	}
}
