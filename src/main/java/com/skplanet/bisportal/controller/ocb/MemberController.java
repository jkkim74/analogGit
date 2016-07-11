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
import com.skplanet.bisportal.common.model.Pagination;
import com.skplanet.bisportal.model.ocb.ObsMbrentSta;
import com.skplanet.bisportal.service.ocb.MemberService;

/**
 * Created by cookatrice on 2014. 5. 9..
 */
@Controller
@RequestMapping("/ocb/member")
public class MemberController extends ReportController {
	@Autowired
	private MemberService memberServiceImpl;

	@RequestMapping(value = "/enter/grid", method = RequestMethod.GET)
	@ResponseBody
	public JqGridResponse<ObsMbrentSta> getEnterForGrid(JqGridRequest jqGridRequest) {
		Pagination pagination = new Pagination();
		pagination.setCurrentPageNo(jqGridRequest.getPage());
		pagination.setRecordCountPerPage(jqGridRequest.getRows());
		pagination.setPageSize(10);

		JqGridResponse<ObsMbrentSta> jqGridResponse = new JqGridResponse<>();
		jqGridResponse.setPage(pagination.getCurrentPageNo());
		jqGridResponse.setRecords(pagination.getTotalRecordCount());
		jqGridResponse.setRows(memberServiceImpl.getEnterForGrid(jqGridRequest));
		jqGridResponse.setTotal(pagination.getTotalPageCount());
		return jqGridResponse;
	}

	@RequestMapping(value = "/downloadExcelForEnter", method = RequestMethod.POST)
	public ModelAndView downloadExcelForEnter(JqGridRequest jqGridRequest) {
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("memberEnterExcelView");
		modelAndView.addObject("memberEnterData", memberServiceImpl.getEnterForGrid(jqGridRequest));
		return modelAndView;
	}

	@RequestMapping(value = "/enter/detail/grid", method = RequestMethod.GET)
	@ResponseBody
	public JqGridResponse<ObsMbrentSta> getEnterDetailForGrid(JqGridRequest jqGridRequest) {
		Pagination pagination = new Pagination();
		pagination.setCurrentPageNo(jqGridRequest.getPage());
		pagination.setRecordCountPerPage(jqGridRequest.getRows());
		pagination.setPageSize(10);

		JqGridResponse<ObsMbrentSta> jqGridResponse = new JqGridResponse<>();
		jqGridResponse.setPage(pagination.getCurrentPageNo());
		jqGridResponse.setRecords(pagination.getTotalRecordCount());
		jqGridResponse.setRows(memberServiceImpl.getEnterDetailForGrid(jqGridRequest));
		jqGridResponse.setTotal(pagination.getTotalPageCount());
		return jqGridResponse;
	}

	@RequestMapping(value = "/downloadExcelForDetail", method = RequestMethod.POST)
	public ModelAndView downloadExcelForDetail(JqGridRequest jqGridRequest) {
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("memberEnterDetailExcelView");
		modelAndView.addObject("memberEnterDetailData", memberServiceImpl.getEnterDetailForGrid(jqGridRequest));
		return modelAndView;
	}
}
