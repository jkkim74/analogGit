package com.skplanet.bisportal.controller.ocb;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.skplanet.bisportal.common.controller.ReportController;
import com.skplanet.bisportal.common.model.JqGridRequest;
import com.skplanet.bisportal.common.model.JqGridResponse;
import com.skplanet.bisportal.model.ocb.ObsSrchClickAlliSta;
import com.skplanet.bisportal.model.ocb.ObsSrchClickBnftSta;
import com.skplanet.bisportal.model.ocb.ObsSrchClickStoreSta;
import com.skplanet.bisportal.service.ocb.SearchAnalysisService;

/**
 * Created by cookatrice on 2014. 5. 19..
 */
@Controller
@RequestMapping("/ocb/searchAnalysis")
public class SearchAnalysisController extends ReportController {
	@Autowired
	private SearchAnalysisService searchAnalysisServiceImpl;

	/**
	 * 검색결과클릭_매장 - pivot(세로형)
	 * 
	 * @param jqGridRequest
	 * @return
	 */
	@RequestMapping(value = "/searchResultClickStore/pivot", method = RequestMethod.GET)
	@ResponseBody
	public JqGridResponse<ObsSrchClickStoreSta> getSearchResultClickStoreForPivot(JqGridRequest jqGridRequest) {
		JqGridResponse<ObsSrchClickStoreSta> jqGridResponse = new JqGridResponse<>();
		jqGridResponse.setRows(searchAnalysisServiceImpl.getSearchResultClickStoreForPivot(jqGridRequest));
		return jqGridResponse;
	}

	/**
	 * 검색결과클릭_매장 - grid(세로형)
	 *
	 * @param jqGridRequest
	 * @return
	 */
	@RequestMapping(value = "/searchResultClickStore/grid", method = RequestMethod.GET)
	@ResponseBody
	public JqGridResponse<ObsSrchClickStoreSta> getSearchResultClickStoreForGrid(JqGridRequest jqGridRequest) {
		JqGridResponse<ObsSrchClickStoreSta> jqGridResponse = new JqGridResponse<>();
		jqGridResponse.setRows(searchAnalysisServiceImpl.getSearchResultClickStoreForGrid(jqGridRequest));
		return jqGridResponse;
	}
	/**
	 * 검색결과클릭_제휴사 - pivot(세로형)
	 * 
	 * @param jqGridRequest
	 * @return
	 */
	@RequestMapping(value = "/searchResultClickAlli/pivot", method = RequestMethod.GET)
	@ResponseBody
	public JqGridResponse<ObsSrchClickAlliSta> getSearchResultClickAlliForPivot(JqGridRequest jqGridRequest) {
		JqGridResponse<ObsSrchClickAlliSta> jqGridResponse = new JqGridResponse<>();
		jqGridResponse.setRows(searchAnalysisServiceImpl.getSearchResultClickAlliForPivot(jqGridRequest));
		return jqGridResponse;
	}

	/**
	 * 검색결과클릭_제휴사 - grid(세로형)
	 *
	 * @param jqGridRequest
	 * @return
	 */
	@RequestMapping(value = "/searchResultClickAlli/grid", method = RequestMethod.GET)
	@ResponseBody
	public JqGridResponse<ObsSrchClickAlliSta> getSearchResultClickAlliForGrid(JqGridRequest jqGridRequest) {
		JqGridResponse<ObsSrchClickAlliSta> jqGridResponse = new JqGridResponse<>();
		jqGridResponse.setRows(searchAnalysisServiceImpl.getSearchResultClickAlliForGrid(jqGridRequest));
 		return jqGridResponse;
	}

	/**
	 * 검색결과클릭_혜택 - grid(세로형)
	 *
	 * @param jqGridRequest
	 * @return
	 */
	@RequestMapping(value = "/searchResultClickBnft/grid", method = RequestMethod.GET)
	@ResponseBody
	public JqGridResponse<ObsSrchClickBnftSta> getSearchResultClickBnftForGrid(JqGridRequest jqGridRequest) {
		JqGridResponse<ObsSrchClickBnftSta> jqGridResponse = new JqGridResponse<>();
		jqGridResponse.setRows(searchAnalysisServiceImpl.getSearchResultClickBnftForGrid(jqGridRequest));
		return jqGridResponse;
	}
}
