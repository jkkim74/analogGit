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
import com.skplanet.bisportal.model.ocb.ObsDPushRespSta;
import com.skplanet.bisportal.model.ocb.ObsPushCnfgSta;
import com.skplanet.bisportal.service.ocb.OcbMktPushSndRsltService;
import com.skplanet.bisportal.service.ocb.ProactiveNotiService;

/**
 * Created by cookatrice on 2014. 5. 19..
 */
@Controller
@RequestMapping("/ocb/proactiveNoti")
public class ProactriveNotiController extends ReportController {
	@Autowired
	private ProactiveNotiService proactiveNotiServiceImpl;

	@Autowired
	private OcbMktPushSndRsltService obsDPushRespStaServiceImpl;

	/**
	 * 알림설정 - pivot(세로형)
	 * 
	 * @param jqGridRequest
	 * @return
	 */
	@RequestMapping(value = "/notificationSet/pivot", method = RequestMethod.GET)
	@ResponseBody
	public JqGridResponse<ObsPushCnfgSta> getNotificationSetForPivot(JqGridRequest jqGridRequest) {
		JqGridResponse<ObsPushCnfgSta> jqGridResponse = new JqGridResponse<>();
		jqGridResponse.setRows(proactiveNotiServiceImpl.getNotificationSetForPivot(jqGridRequest));
		return jqGridResponse;
	}

	/**
	 * 마케팅 push 발송 결과 조회
	 *
	 * @param jqGridRequest
	 * @return jqGridResponse
	 */
	@RequestMapping(value = "/mktPushSndRslt/get", method = RequestMethod.GET)
	@ResponseBody
	public JqGridResponse<ObsDPushRespSta> getMktPushSndRslt(JqGridRequest jqGridRequest) {
		JqGridResponse<ObsDPushRespSta> jqGridResponse = new JqGridResponse<>();
		jqGridResponse.setRows(obsDPushRespStaServiceImpl.getMktPushSndRslt(jqGridRequest));
		return jqGridResponse;
	}

	/**
	 * 마케팅 push 발송 결과 엑셀 출력
	 *
	 * @param jqGridRequest
	 * @return jqGridResponse
	 */
	@RequestMapping(value = "/downloadExcelForMktPushSndRslt", method = RequestMethod.POST)
	public ModelAndView downloadExcelForMktPushSndRslt(JqGridRequest jqGridRequest) throws Exception {
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("mktPushSndRsltExcelView");
		modelAndView.addObject("mktPushSndRsltData", obsDPushRespStaServiceImpl.getMktPushSndRslt(jqGridRequest));
		return modelAndView;
	}
}
