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
import com.skplanet.bisportal.model.ocb.ObsNewOcbSegRpt;
import com.skplanet.bisportal.service.ocb.NewOcbSegService;

/**
 * Created by cookatrice on 2014. 8. 11..
 */
@Controller
@RequestMapping("/ocb/khub/newOcbSeg")
public class NewOcbSegController extends ReportController {
	@Autowired
	private NewOcbSegService newOcbSegServiceImpl;

	/**
	 * New OCB Seg 조회 - grid
	 *
	 * @param jqGridRequest
	 * @return
	 */
	@RequestMapping(value = "/newOcbSeg/grid", method = RequestMethod.GET)
	@ResponseBody
	public JqGridResponse<ObsNewOcbSegRpt> getNewOcbSegForGrid(JqGridRequest jqGridRequest) {
		JqGridResponse<ObsNewOcbSegRpt> jqGridResponse = new JqGridResponse<>();
		jqGridResponse.setRows(newOcbSegServiceImpl.getNewOcbSegForGrid(jqGridRequest));

		return jqGridResponse;
	}

	/**
	 * New OCB Seg 엑셀 다운로드
	 *
	 * @param jqGridRequest
	 * @return
	 */
	@RequestMapping(value = "/downloadExcel", method = RequestMethod.POST)
	public ModelAndView downloadExcelForNewOcbSeg(JqGridRequest jqGridRequest) {
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("newOcbSeqExcelView");
		modelAndView.addObject("newOcbSeqData", newOcbSegServiceImpl.getNewOcbSegForGrid(jqGridRequest));
		return modelAndView;
	}
}
