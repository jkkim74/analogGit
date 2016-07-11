package com.skplanet.bisportal.controller.ocb;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.skplanet.bisportal.common.controller.ReportController;
import com.skplanet.bisportal.common.model.JqGridRequest;
import com.skplanet.bisportal.common.model.JqGridResponse;
import com.skplanet.bisportal.model.ocb.ObsPresntSumRpt;
import com.skplanet.bisportal.service.ocb.PointPresentSumService;

/**
 * Created by cookatrice on 2014. 8. 11..
 */
@Controller
@RequestMapping("/ocb/khub/pointPresentSum")
public class PointPresentSumController extends ReportController {
	@Autowired
	private PointPresentSumService pointPresentSumServiceImpl;

	/**
	 * 포인트 선물/합산하기 조회 - grid
	 *
	 * @param jqGridRequest
	 * @return
	 */
	@RequestMapping(value = "/pointPresentSum/grid", method = RequestMethod.GET)
	@ResponseBody
	public JqGridResponse<ObsPresntSumRpt> getPointPresentSumForGrid(JqGridRequest jqGridRequest) {
		if (StringUtils.equals(jqGridRequest.getDateType().value(), "month")) {
			jqGridRequest.setStartDate(jqGridRequest.getStartDate() + "01");
			jqGridRequest.setEndDate(jqGridRequest.getEndDate() + "31");
		}
		JqGridResponse<ObsPresntSumRpt> jqGridResponse = new JqGridResponse<>();
		jqGridResponse.setRows(pointPresentSumServiceImpl.getPointPresentSumForGrid(jqGridRequest));
		return jqGridResponse;
	}

	/**
	 * 포인트 선물/합산하기 엑셀 다운로드
	 *
	 * @param jqGridRequest
	 * @return
	 */
	@RequestMapping(value = "/downloadExcel", method = RequestMethod.POST)
	public ModelAndView downloadExcelForPointPresentSum(JqGridRequest jqGridRequest) {
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("pointPresentSumExcelView");
		modelAndView.addObject("pointPresentSumData",
				pointPresentSumServiceImpl.getPointPresentSumForGrid(jqGridRequest));
		return modelAndView;
	}
}
