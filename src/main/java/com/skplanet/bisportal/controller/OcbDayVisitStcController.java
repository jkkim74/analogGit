package com.skplanet.bisportal.controller;

import com.google.common.collect.Maps;
import com.skplanet.bisportal.common.model.JqGridRequest;
import com.skplanet.bisportal.common.model.JqGridResponse;
import com.skplanet.bisportal.common.model.Pagination;
import com.skplanet.bisportal.model.ocb.OcbDayVisitStc;
import com.skplanet.bisportal.service.OcbDayVisitStcService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;

/**
 * Created by cookatrice on 2014. 5. 2..
 */

@Controller
@RequestMapping("/ocbDayVisitStc")
public class OcbDayVisitStcController {
	@Autowired
	private OcbDayVisitStcService ocbDayVisitStcServiceImpl;

	@RequestMapping(value = "/visitorList/pagination", method = RequestMethod.GET)
	@ResponseBody
	public JqGridResponse<OcbDayVisitStc> getVisitorListPerDayPagination(JqGridRequest jqGridRequest,
			@RequestParam(required = true) String startDate, @RequestParam(required = true) String endDate) {
		Pagination pagination = new Pagination();
		pagination.setCurrentPageNo(jqGridRequest.getPage());
		pagination.setRecordCountPerPage(jqGridRequest.getRows());
		pagination.setPageSize(10);

		Map<String, Object> queryParam = Maps.newHashMap();
		queryParam.put("firstIndex", pagination.getFirstRecordIndex());
		queryParam.put("lastIndex", pagination.getLastRecordIndex());
		queryParam.put("recordCountPerPage", pagination.getRecordCountPerPage());
		queryParam.put("startDate", startDate);
		queryParam.put("endDate", endDate);

		List<OcbDayVisitStc> obcDayVisitStc = ocbDayVisitStcServiceImpl.getVisitorListPerDay(queryParam);
		pagination.setTotalRecordCount(obcDayVisitStc.get(0).getTotalRecordCount());

		JqGridResponse<OcbDayVisitStc> jqGridResponse = new JqGridResponse<>();
		jqGridResponse.setPage(pagination.getCurrentPageNo());
		jqGridResponse.setRecords(pagination.getTotalRecordCount());
		jqGridResponse.setRows(obcDayVisitStc);
		jqGridResponse.setTotal(pagination.getTotalPageCount());

		return jqGridResponse;
	}

	@RequestMapping(value = "/visitorList", method = RequestMethod.GET)
	@ResponseBody
	public List<OcbDayVisitStc> getVisitorListPerDay(@RequestParam(required = true) String startDate, @RequestParam(required = true) String endDate) {
        Map<String, Object> queryParam = Maps.newHashMap();
        queryParam.put("startDate", startDate);
        queryParam.put("endDate", endDate);
		return ocbDayVisitStcServiceImpl.getVisitorList(queryParam);
	}

}
