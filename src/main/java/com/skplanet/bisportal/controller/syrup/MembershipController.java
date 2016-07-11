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
import com.skplanet.bisportal.model.syrup.SmwCardIssue;
import com.skplanet.bisportal.model.syrup.SmwCardIssueDtl;
import com.skplanet.bisportal.model.syrup.SmwRcmdStatDtl;
import com.skplanet.bisportal.service.syrup.MembershipService;

/**
 * Created by lko on 2014-11-27.
 */
@Controller
@RequestMapping("/syrup/membership")
public class MembershipController extends ReportController {
	@Autowired
	private MembershipService membershipServiceImpl;

	@RequestMapping(value = "/cardissuemem/grid", method = RequestMethod.GET)
	@ResponseBody
	public JqGridResponse<SmwCardIssueDtl> getCardIssueMemGrid(JqGridRequest jqGridRequest) {
		JqGridResponse<SmwCardIssueDtl> jqGridResponse = new JqGridResponse<>();
		jqGridResponse.setRows(membershipServiceImpl.getCardIssueMemGrid(jqGridRequest));
		return jqGridResponse;
	}

	@RequestMapping(value = "/cardissuePaVou/grid", method = RequestMethod.GET)
	@ResponseBody
	public JqGridResponse<SmwCardIssueDtl> getCardIssuePaVouGrid(JqGridRequest jqGridRequest) {
		JqGridResponse<SmwCardIssueDtl> jqGridResponse = new JqGridResponse<>();
		jqGridResponse.setRows(membershipServiceImpl.getCardIssuePaVouGrid(jqGridRequest));
		return jqGridResponse;
	}

	@RequestMapping(value = "/rcmdStat/grid", method = RequestMethod.GET)
	@ResponseBody
	public JqGridResponse<SmwRcmdStatDtl> getSmwRcmdStatDtlGrid(JqGridRequest jqGridRequest) {
		JqGridResponse<SmwRcmdStatDtl> jqGridResponse = new JqGridResponse<>();
		jqGridResponse.setRows(membershipServiceImpl.getSmwRcmdStatDtlGrid(jqGridRequest));
		return jqGridResponse;
	}

	@RequestMapping(value = "/downloadExcelForMebershipIssueMem", method = RequestMethod.POST)
	public ModelAndView downloadExcelForMebershipIssueMem(JqGridRequest jqGridRequest) throws Exception {
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("mebershipIssueMemExcelView");
		modelAndView.addObject("mebershipIssueMemExcelViewData",
				membershipServiceImpl.getCardIssueMemGrid(jqGridRequest));
		return modelAndView;
	}

	@RequestMapping(value = "/downloadExcelForIssuePaVou", method = RequestMethod.POST)
	public ModelAndView downloadExcelForIssuePaVou(JqGridRequest jqGridRequest) throws Exception {
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("issuePaVouExcelView");
		modelAndView.addObject("issuePaVouExcelViewData", membershipServiceImpl.getCardIssuePaVouGrid(jqGridRequest));
		return modelAndView;
	}

	@RequestMapping(value = "/downloadExcelForRcmdStat", method = RequestMethod.POST)
	public ModelAndView downloadExcelForRcmdStat(JqGridRequest jqgridRequest) throws Exception {
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("rcmdStatExcelView");
		modelAndView.addObject("rcmdStatExcelViewData", membershipServiceImpl.getSmwRcmdStatDtlGrid(jqgridRequest));
		return modelAndView;
	}

	/**
	 * 멤버십발급
	 *
	 * @param olapDimensionRequest
	 * @return
	 */
	@RequestMapping(value = "/membershipIssue", method = RequestMethod.GET)
	@ResponseBody
	public JqGridResponse<SmwCardIssue> getMembershipIssue(OlapDimensionRequest olapDimensionRequest) {
		JqGridResponse<SmwCardIssue> jqGridResponse = new JqGridResponse<>();
		jqGridResponse.setRows(membershipServiceImpl.getMembershipIssue(olapDimensionRequest));
		return jqGridResponse;
	}
}
