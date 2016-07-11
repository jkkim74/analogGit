package com.skplanet.bisportal.controller.syrup;

import java.util.List;

import javax.validation.Valid;

import lombok.extern.slf4j.Slf4j;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.skplanet.bisportal.common.consts.Constants;
import com.skplanet.bisportal.common.controller.ReportController;
import com.skplanet.bisportal.common.model.JqGridRequest;
import com.skplanet.bisportal.common.model.JqGridResponse;
import com.skplanet.bisportal.common.model.Pagination;
import com.skplanet.bisportal.common.validator.BleValidator;
import com.skplanet.bisportal.model.ocb.BleNewTech;
import com.skplanet.bisportal.service.ocb.BleService;

/**
 * The BleController class.
 *
 * @author HoJin-Ha (mimul@wiseeco.com)
 *
 */
@Slf4j
@Controller
@RequestMapping("/syrup/ble")
public class BleController extends ReportController {
	@Qualifier("bleValidator")
	@Autowired
	private BleValidator bleValidator;
	@Autowired
	private BleService bleServiceImpl;

	@InitBinder
	protected void initBinder(WebDataBinder binder) {
		binder.setValidator(bleValidator);
	}

	@RequestMapping(value = "/service/grid", method = RequestMethod.POST)
	@ResponseBody
	public JqGridResponse<BleNewTech> getServiceForGrid(@Valid JqGridRequest jqGridRequest, BindingResult bindingResult)
			throws Exception {
		JqGridResponse<BleNewTech> jqGridResponse = new JqGridResponse<>();
		if (bindingResult.hasErrors()) {
			log.error("getServiceForGrid {}", bindingResult.toString());
			jqGridResponse.setCode(Constants.NUMBER_TWO);
			jqGridResponse.setMessage("invalid parameter");
			return jqGridResponse;
		}

		Pagination pagination = new Pagination();
		pagination.setCurrentPageNo(jqGridRequest.getPage());// 현재 페이지
		pagination.setRecordCountPerPage(jqGridRequest.getRows()); // 페이지당 레코드 수
		pagination.setPageSize(10);
		jqGridRequest.setFirstIndex(pagination.getFirstRecordIndex());
		jqGridRequest.setLastIndex(pagination.getLastRecordIndex());

		List<BleNewTech> bleNewTechs = bleServiceImpl.getServiceForGrid(jqGridRequest);
		if (CollectionUtils.isNotEmpty(bleNewTechs)) {
			jqGridResponse.setRows(bleNewTechs);
			pagination.setTotalRecordCount(bleNewTechs.get(0).getTotalRecordCount());
			jqGridResponse.setPage(pagination.getCurrentPageNo());
			jqGridResponse.setRecords(pagination.getTotalRecordCount());
			jqGridResponse.setTotal(pagination.getTotalPageCount());
			jqGridResponse.setCode(Constants.NUMBER_ZERO);
			jqGridResponse.setMessage(Constants.SUCCESS);
		} else {
			jqGridResponse.setCode(Constants.NUMBER_THREE);
			jqGridResponse.setMessage(Constants.DATA_NOT_FOUND);
		}
		return jqGridResponse;
	}

	@RequestMapping(value = "/service/subGrid", method = RequestMethod.GET)
	@ResponseBody
	public JqGridResponse<BleNewTech> getServiceForSubGrid(JqGridRequest jqGridRequest) throws Exception {
		JqGridResponse<BleNewTech> jqGridResponse = new JqGridResponse<>();
		jqGridResponse.setRows(bleServiceImpl.getMerchantDetail(jqGridRequest));
		return jqGridResponse;
	}

	@RequestMapping(value = "/service/downloadExcel", method = RequestMethod.POST)
	public ModelAndView downloadExcelForBleService(JqGridRequest jqGridRequest) throws Exception {
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("bleServiceExcelView");
		modelAndView.addObject("bleServiceData", bleServiceImpl.getServiceForExcel(jqGridRequest));
		return modelAndView;
	}
}
