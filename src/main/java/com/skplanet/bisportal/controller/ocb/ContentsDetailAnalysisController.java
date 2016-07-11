package com.skplanet.bisportal.controller.ocb;

import com.skplanet.bisportal.common.controller.ReportController;
import com.skplanet.bisportal.common.model.JqGridRequest;
import com.skplanet.bisportal.common.model.JqGridResponse;
import com.skplanet.bisportal.common.model.Pagination;
import com.skplanet.bisportal.model.ocb.*;
import com.skplanet.bisportal.service.ocb.ContentsDetailAnalysisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

/**
 * Created by cookatrice on 2014. 5. 15..
 */
@Controller
@RequestMapping("/ocb/contentsDetailAnalisys")
public class ContentsDetailAnalysisController extends ReportController {
	@Autowired
	private ContentsDetailAnalysisService contentsDetailAnalysisServiceImpl;

	/**
	 * Feed 노출 - grid(세로형)
	 * 
	 * @param jqGridRequest
	 * @return
	 */
	@RequestMapping(value = "/feedExposure/grid", method = RequestMethod.GET)
	@ResponseBody
	public JqGridResponse<ObsFeedSta> getFeedsExposureForGrid(JqGridRequest jqGridRequest) {
		JqGridResponse<ObsFeedSta> jqGridResponse = new JqGridResponse<>();
		jqGridResponse.setRows(contentsDetailAnalysisServiceImpl.getFeedsExposureForGrid(jqGridRequest));
		return jqGridResponse;
	}

	@RequestMapping(value = "/downloadExcelForFeedExposure", method = RequestMethod.POST)
	public ModelAndView downloadExcelForFeedExposure(JqGridRequest jqGridRequest) {
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("feedExposureExcelView");
		modelAndView.addObject("feedExposureData",
				contentsDetailAnalysisServiceImpl.getFeedsExposureForGrid(jqGridRequest));
		return modelAndView;
	}

	/**
	 * Feed 클릭 - pivot(세로형)
	 * 
	 * @param jqGridRequest
	 * @return
	 */
	@RequestMapping(value = "/feedExposureOrder/pivot", method = RequestMethod.GET)
	@ResponseBody
	public JqGridResponse<ObsFeedSta> getFeedsExposureOrderForPivot(JqGridRequest jqGridRequest) {
		JqGridResponse<ObsFeedSta> jqGridResponse = new JqGridResponse<>();
		jqGridResponse.setRows(contentsDetailAnalysisServiceImpl.getFeedsExposureOrderForPivot(jqGridRequest));
		return jqGridResponse;
	}

	@RequestMapping(value = "/downloadExcelForFeedExposureOrder", method = RequestMethod.POST)
	public ModelAndView downloadExcelForfeedExposureOrder(JqGridRequest jqGridRequest) {
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("feedExposureOrderExcelView");
		modelAndView.addObject("feedExposureOrderData",
				contentsDetailAnalysisServiceImpl.getFeedsExposureOrderForGrid(jqGridRequest));
		return modelAndView;
	}

	/**
	 * Feed 클릭 - grid(세로형)
	 * 
	 * feed-order grid test version... service2>category2>feed-order grid tests
	 * 
	 * @param jqGridRequest
	 * @return
	 */
	@RequestMapping(value = "/feedExposureOrder/grid", method = RequestMethod.GET)
	@ResponseBody
	public JqGridResponse<ObsFeedSta> getFeedsExposureOrderForGrid(JqGridRequest jqGridRequest) {
		JqGridResponse<ObsFeedSta> jqGridResponse = new JqGridResponse<>();
		jqGridResponse.setRows(contentsDetailAnalysisServiceImpl.getFeedsExposureOrderForGrid(jqGridRequest));
		return jqGridResponse;
	}

	/**
	 * 매장-전체 - pivot(세로형)
	 * 
	 * @param jqGridRequest
	 * @return
	 */
	@RequestMapping(value = "/storeTotal/pivot", method = RequestMethod.GET)
	@ResponseBody
	public JqGridResponse<ObsStoreOvrlSta> getStoreTotalForPivot(JqGridRequest jqGridRequest) {
		JqGridResponse<ObsStoreOvrlSta> jqGridResponse = new JqGridResponse<>();
		jqGridResponse.setRows(contentsDetailAnalysisServiceImpl.getStoreTotalForPivot(jqGridRequest));
		return jqGridResponse;
	}

	/**
	 * 매장-개별 - pivot(세로형)
	 * 
	 * @param jqGridRequest
	 * @return
	 */
	@RequestMapping(value = "/storeSingle/pivot", method = RequestMethod.GET)
	@ResponseBody
	public JqGridResponse<ObsStoreDetlSta> getStoreSingleForPivot(JqGridRequest jqGridRequest) {
		JqGridResponse<ObsStoreDetlSta> jqGridResponse = new JqGridResponse<>();
		jqGridResponse.setRows(contentsDetailAnalysisServiceImpl.getStoreSingleForPivot(jqGridRequest));
		return jqGridResponse;
	}

	/**
	 * 매장-개별 - grid(세로형)
	 * 
	 * @param jqGridRequest
	 * @return
	 */
	@RequestMapping(value = "/storeSingle/grid", method = RequestMethod.GET)
	@ResponseBody
	public JqGridResponse<ObsStoreDetlSta> getStoreSingleForGrid(JqGridRequest jqGridRequest) {
		JqGridResponse<ObsStoreDetlSta> jqGridResponse = new JqGridResponse<>();
		jqGridResponse.setRows(contentsDetailAnalysisServiceImpl.getStoreSingleForGrid(jqGridRequest));
		return jqGridResponse;
	}

	@RequestMapping(value = "/storeSingle/grid/pagination", method = RequestMethod.GET)
	@ResponseBody
	public JqGridResponse<ObsStoreDetlSta> getStoreSingleForGridPagination(JqGridRequest jqGridRequest) {
		Pagination pagination = new Pagination();
		pagination.setCurrentPageNo(jqGridRequest.getPage());
		pagination.setRecordCountPerPage(jqGridRequest.getRows());
		pagination.setPageSize(10);

		jqGridRequest.setFirstIndex(pagination.getFirstRecordIndex());
		jqGridRequest.setLastIndex(pagination.getLastRecordIndex());
		jqGridRequest.setRecordCountPerPage(pagination.getRecordCountPerPage());
		List<ObsStoreDetlSta> result = contentsDetailAnalysisServiceImpl.getStoreSingleForGridPagination(jqGridRequest);
		pagination.setTotalRecordCount(result.get(0).getTotalRecordCount());

		JqGridResponse<ObsStoreDetlSta> jqGridResponse = new JqGridResponse<>();
		jqGridResponse.setPage(pagination.getCurrentPageNo());
		jqGridResponse.setRecords(pagination.getTotalRecordCount());
		jqGridResponse.setRows(result);
		jqGridResponse.setTotal(pagination.getTotalPageCount());
		return jqGridResponse;
	}

	@RequestMapping(value = "/downloadExcelForStoreSingle", method = RequestMethod.POST)
	public ModelAndView downloadExcelForStoreSingle(JqGridRequest jqGridRequest) {
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("storeSingleExcelView");
		modelAndView.addObject("storeSingleData",
				contentsDetailAnalysisServiceImpl.getStoreSingleForGrid(jqGridRequest));
		return modelAndView;
	}

	/**
	 * 포인트 - pivot
	 * 
	 * @param jqGridRequest
	 * @return
	 */
	@RequestMapping(value = "/point/pivot", method = RequestMethod.GET)
	@ResponseBody
	public JqGridResponse<ObsPntSta> getPntsForPivot(JqGridRequest jqGridRequest) {
		JqGridResponse<ObsPntSta> jqGridResponse = new JqGridResponse<>();
		jqGridResponse.setRows(contentsDetailAnalysisServiceImpl.getPntsForPivot(jqGridRequest));
		return jqGridResponse;
	}

	/**
	 * discover - pivot
	 * 
	 * @param jqGridRequest
	 * @return
	 */
	@RequestMapping(value = "/discover/pivot", method = RequestMethod.GET)
	@ResponseBody
	public JqGridResponse<ObsCntntDscvSta> getDiscoverForPivot(JqGridRequest jqGridRequest) {
		JqGridResponse<ObsCntntDscvSta> jqGridResponse = new JqGridResponse<>();
		// TODO after table create... coding
		jqGridResponse.setRows(contentsDetailAnalysisServiceImpl.getDiscoverForPivot(jqGridRequest));

		return jqGridResponse;
	}

	/**
	 * 상권전단 내 매장전단 - pivot
	 * 
	 * @param jqGridRequest
	 * @return
	 */
	@RequestMapping(value = "/storeFlyerInTradeAreaFlyer/pivot", method = RequestMethod.GET)
	@ResponseBody
	public JqGridResponse<ObsCntntFlyrDetlSta> getstoreFlyerInTradeAreaFlyerPivot(JqGridRequest jqGridRequest) {
		JqGridResponse<ObsCntntFlyrDetlSta> jqGridResponse = new JqGridResponse<>();
		jqGridResponse.setRows(contentsDetailAnalysisServiceImpl.getStoreFlyerInTradeAreaFlyerForPivot(jqGridRequest));
		return jqGridResponse;
	}

	/**
	 * 상권전단 - pivot
	 * 
	 * @param jqGridRequest
	 * @return
	 */
	@RequestMapping(value = "/storeFlyer/pivot", method = RequestMethod.GET)
	@ResponseBody
	public JqGridResponse<ObsCntntTraraFlyrSta> getStoreFlyerForPivot(JqGridRequest jqGridRequest) {
		JqGridResponse<ObsCntntTraraFlyrSta> jqGridResponse = new JqGridResponse<>();
		jqGridResponse.setRows(contentsDetailAnalysisServiceImpl.getStoreFlyerForPivot(jqGridRequest));
		return jqGridResponse;
	}

	/**
	 * 모바일전단 쿠폰다운 - pivot
	 *
	 * @param jqGridRequest
	 * @return
	 */
	@RequestMapping(value = "/mobileCoupon/pivot", method = RequestMethod.GET)
	@ResponseBody
	public JqGridResponse<ObsCntntMbilFlyrCopnSta> getMobileFrontCouponForPivot(JqGridRequest jqGridRequest) {
		JqGridResponse<ObsCntntMbilFlyrCopnSta> jqGridResponse = new JqGridResponse<>();
		jqGridResponse.setRows(contentsDetailAnalysisServiceImpl.getMobileFrontCouponForPivot(jqGridRequest));
		return jqGridResponse;
	}

	/**
	 * 모바일전단 페이지조회 - pivot
	 *
	 * @param jqGridRequest
	 * @return
	 */
	@RequestMapping(value = "/mobileFlyerPageInquiry/pivot", method = RequestMethod.GET)
	@ResponseBody
	public JqGridResponse<ObsCntntMbilFlyrPageSta> getMobileFlyerPageInquiryForPivot(JqGridRequest jqGridRequest) {
		JqGridResponse<ObsCntntMbilFlyrPageSta> jqGridResponse = new JqGridResponse<>();
		jqGridResponse.setRows(contentsDetailAnalysisServiceImpl.getMobileFlyerPageInquiryForPivot(jqGridRequest));
		return jqGridResponse;
	}

	/**
	 * 마케팅 PUSH
	 *
	 * @param jqGridRequest
	 * @return
	 */
	@RequestMapping(value = "/marketingPush/get", method = RequestMethod.GET)
	@ResponseBody
	public JqGridResponse<ObsMktPushSend> getMarketingPush(JqGridRequest jqGridRequest) {
		JqGridResponse<ObsMktPushSend> jqGridResponse = new JqGridResponse<>();
		jqGridResponse.setRows(contentsDetailAnalysisServiceImpl.getMarketingPush(jqGridRequest));
		return jqGridResponse;
	}

	/**
	 * 마케팅 PUSH 엑셀 출력
	 *
	 * @param jqGridRequest
	 * @return
	 */
	@RequestMapping(value = "/downloadExcelForMarketingPush", method = RequestMethod.POST)
	public ModelAndView downloadExcelForMarketingPush(JqGridRequest jqGridRequest) throws Exception {
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("marketingPushExcelView");
		modelAndView.addObject("marketingPushData", contentsDetailAnalysisServiceImpl.getMarketingPush(jqGridRequest));
		return modelAndView;
	}
}
