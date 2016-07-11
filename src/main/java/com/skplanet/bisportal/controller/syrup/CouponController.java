package com.skplanet.bisportal.controller.syrup;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.skplanet.bisportal.common.consts.Constants;
import com.skplanet.bisportal.common.controller.ReportController;
import com.skplanet.bisportal.common.model.JqGridRequest;
import com.skplanet.bisportal.common.model.JqGridResponse;
import com.skplanet.bisportal.common.model.OlapDimensionRequest;
import com.skplanet.bisportal.common.model.Pagination;
import com.skplanet.bisportal.model.syrup.SmwCponLocStat;
import com.skplanet.bisportal.model.syrup.SmwCponStat;
import com.skplanet.bisportal.model.syrup.SmwCponStatDtl;
import com.skplanet.bisportal.service.syrup.CouponService;

/**
 * Created by lko on 2014-11-19.
 */
@Controller
@RequestMapping("/syrup/coupon")
public class CouponController extends ReportController {
	@Autowired
	private CouponService couponServiceImpl;

	@RequestMapping(value = "/cponstat/grid", method = RequestMethod.GET)
	@ResponseBody
	public JqGridResponse<SmwCponStatDtl> getCponStatForGrid(JqGridRequest jqGridRequest) {
		JqGridResponse<SmwCponStatDtl> jqGridResponse = new JqGridResponse<>();
		jqGridResponse.setRows(couponServiceImpl.getCponStatForGrid(jqGridRequest));
		return jqGridResponse;
	}

	@RequestMapping(value = "/cponstat/grid", method = RequestMethod.POST)
	@ResponseBody
	public JqGridResponse<SmwCponStatDtl> getServiceForGrid(JqGridRequest jqGridRequest)
			throws Exception {
		JqGridResponse<SmwCponStatDtl> jqGridResponse = new JqGridResponse<>();
		Pagination pagination = new Pagination();
		pagination.setCurrentPageNo(jqGridRequest.getPage());// 현재 페이지
		pagination.setRecordCountPerPage(jqGridRequest.getRows()); // 페이지당 레코드 수
		pagination.setPageSize(10);
		jqGridRequest.setFirstIndex(pagination.getFirstRecordIndex());
		jqGridRequest.setLastIndex(pagination.getLastRecordIndex());

		List<SmwCponStatDtl> smwCponStatDtls = couponServiceImpl.getCponStatForGrid(jqGridRequest);
		if (CollectionUtils.isNotEmpty(smwCponStatDtls)) {
			jqGridResponse.setRows(smwCponStatDtls);
			pagination.setTotalRecordCount(smwCponStatDtls.get(0).getTotalRecordCount());
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

	@RequestMapping(value = "/cponlocstat/grid", method = RequestMethod.GET)
	@ResponseBody
	public JqGridResponse<SmwCponLocStat> getCponLocStatForGrid(JqGridRequest jqGridRequest) {
		JqGridResponse<SmwCponLocStat> jqGridResponse = new JqGridResponse<>();
		jqGridResponse.setRows(couponServiceImpl.getCponLocStatForGrid(jqGridRequest));
		return jqGridResponse;
	}

	@RequestMapping(value = "/downloadExcelForCouponStat", method = RequestMethod.POST)
	public ModelAndView downloadExcelForCouponStat(JqGridRequest jqGridRequest) throws Exception {
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("mbrJoinExcelView");
		modelAndView.addObject("couponStatData", couponServiceImpl.getCponStatForGridForExcel(jqGridRequest));
		return modelAndView;
	}

	@RequestMapping(value = "/downloadExcelForCouponLocStat", method = RequestMethod.POST)
	public ModelAndView downloadExcelForCouponLocStat(JqGridRequest jqGridRequest) throws Exception {
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("couponLocStatExcelView");
		modelAndView.addObject("couponLocStatData", couponServiceImpl.getCponLocStatForGrid(jqGridRequest));
		return modelAndView;
	}

	/**
	 * 쿠폰실적
	 *
	 * @param olapDimensionRequest
	 * @return
	 */
	@RequestMapping(value = "/couponAchieve", method = RequestMethod.GET)
	@ResponseBody
	public JqGridResponse<SmwCponStat> getCouponAchieve(OlapDimensionRequest olapDimensionRequest) {
		JqGridResponse<SmwCponStat> jqGridResponse = new JqGridResponse<>();
		jqGridResponse.setRows(couponServiceImpl.getCouponAchieve(olapDimensionRequest));
		return jqGridResponse;
	}
}
