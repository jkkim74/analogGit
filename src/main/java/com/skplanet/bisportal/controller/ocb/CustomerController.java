package com.skplanet.bisportal.controller.ocb;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.skplanet.bisportal.common.controller.ReportController;
import com.skplanet.bisportal.common.model.JqGridRequest;
import com.skplanet.bisportal.common.model.JqGridResponse;
import com.skplanet.bisportal.model.ocb.DauResponse;
import com.skplanet.bisportal.model.ocb.ObsActnSta;
import com.skplanet.bisportal.model.ocb.ObsSosBleSta;
import com.skplanet.bisportal.model.ocb.ObsVstFstUvSta;
import com.skplanet.bisportal.model.ocb.ObsVstInflowUvSta;
import com.skplanet.bisportal.model.ocb.RtdDau;
import com.skplanet.bisportal.service.ocb.CustomerService;

/**
 * The CustomerController class.
 *
 * @author HoJin-Ha (mimul@wiseeco.com)
 *
 */
@Slf4j
@Controller
@RequestMapping("/ocb/customer")
public class CustomerController extends ReportController {
	@Autowired
	private CustomerService customerDetailServiceImpl;

	/**
	 * DAU 처리.
	 *
	 * @param
	 * @return List<Dau>
	 */
	@RequestMapping(value = "/dau", method = RequestMethod.GET)
	@ResponseBody
	public DauResponse getDauForGrid() {
		DauResponse dauResponse = null;
		try {
			dauResponse = customerDetailServiceImpl.getDau();
		} catch (Exception e) {
			log.error("getDauForGrid {}", e);
		}
		return dauResponse;
	}

	/**
	 * 고객별 액션
	 *
	 * @param jqGridRequest
	 * @return JqGridResponse<ObsActnSta>
	 */
	@RequestMapping(value = "/customerAction/grid", method = RequestMethod.GET)
	@ResponseBody
	public JqGridResponse<ObsActnSta> getCustomersActionForGrid(JqGridRequest jqGridRequest) {
		JqGridResponse<ObsActnSta> jqGridResponse = new JqGridResponse<>();
		jqGridResponse.setRows(customerDetailServiceImpl.getCustomersActionForGrid(jqGridRequest));
		return jqGridResponse;
	}

	/**
	 * BLE
	 *
	 * @param jqGridRequest
	 * @return JqGridResponse<ObsSosBleSta>
	 */
	@RequestMapping(value = "/sosBle/grid", method = RequestMethod.GET)
	@ResponseBody
	public JqGridResponse<ObsSosBleSta> getBleDiffData(JqGridRequest jqGridRequest) {
		JqGridResponse<ObsSosBleSta> jqGridResponse = new JqGridResponse<>();
		jqGridResponse.setRows(customerDetailServiceImpl.getBleDiffData(jqGridRequest));
		return jqGridResponse;
	}

	/**
	 * BLE 엑셀 다운로드
	 *
	 * @param jqGridRequest
	 * @return
	 */
	@RequestMapping(value = "/sosBle/downloadExcel", method = RequestMethod.POST)
	public ModelAndView downloadExcelForSosBle(JqGridRequest jqGridRequest) {
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("sosBleView");
		modelAndView.addObject("sosBleData", customerDetailServiceImpl.getBleDiffData(jqGridRequest));
		return modelAndView;
	}

	/**
	 * OCB DAU(KPI 기준) - pivot
	 *
	 * @param jqGridRequest
	 * @return
	 */
	@RequestMapping(value = "/ocbDau/pivot", method = RequestMethod.GET)
	@ResponseBody
	public JqGridResponse<RtdDau> getOcbDauForPivot(JqGridRequest jqGridRequest) {
		JqGridResponse<RtdDau> jqGridResponse = new JqGridResponse<>();
		jqGridResponse.setRows(customerDetailServiceImpl.getOcbDauForPivot(jqGridRequest));
		return jqGridResponse;
	}

	/**
	 * OCB DAU(유입경로별) - pivot
	 *
	 * @param jqGridRequest
	 * @return
	 */
	@RequestMapping(value = "/visitInflRt/pivot", method = RequestMethod.GET)
	@ResponseBody
	public JqGridResponse<ObsVstInflowUvSta> getVisitInflRtForPivot(JqGridRequest jqGridRequest) {
		JqGridResponse<ObsVstInflowUvSta> jqGridResponse = new JqGridResponse<>();
		jqGridResponse.setRows(customerDetailServiceImpl.getVisitInflRtForPivot(jqGridRequest));
		return jqGridResponse;
	}

	/**
	 * 월 최초방문자(고객타입별) - pivot
	 *
	 * @param jqGridRequest
	 * @return
	 */
	@RequestMapping(value = "/visitFst/pivot", method = RequestMethod.GET)
	@ResponseBody
	public JqGridResponse<ObsVstFstUvSta> getVisitFstForPivot(JqGridRequest jqGridRequest) {
		JqGridResponse<ObsVstFstUvSta> jqGridResponse = new JqGridResponse<>();
		jqGridResponse.setRows(customerDetailServiceImpl.getVisitFstForPivot(jqGridRequest));
		return jqGridResponse;
	}

	/**
	 * Syrup DAU - pivot
	 *
	 * @param jqGridRequest
	 * @return
	 */
	@RequestMapping(value = "/syrupDau/pivot", method = RequestMethod.GET)
	@ResponseBody
	public JqGridResponse<RtdDau> getSyrupDauForPivot(JqGridRequest jqGridRequest) {
		JqGridResponse<RtdDau> jqGridResponse = new JqGridResponse<>();
		jqGridResponse.setRows(customerDetailServiceImpl.getSyrupDauForPivot(jqGridRequest));
		return jqGridResponse;
	}

	/**
	 * App Stickness - pivot
	 *
	 * @param jqGridRequest
	 * @return
	 */
	@RequestMapping(value = "/appStickness/pivot", method = RequestMethod.GET)
	@ResponseBody
	public JqGridResponse<RtdDau> getAppSticknessForPivot(JqGridRequest jqGridRequest) {
		JqGridResponse<RtdDau> jqGridResponse = new JqGridResponse<>();
		jqGridResponse.setRows(customerDetailServiceImpl.getAppSticknessForPivot(jqGridRequest));
		return jqGridResponse;
	}
}
