package com.skplanet.bisportal.controller.ocb;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.skplanet.bisportal.model.sankey.SankeyRequest;
import com.skplanet.bisportal.model.sankey.SankeyResponse;
import com.skplanet.bisportal.service.ocb.SankeyService;

/**
 * Created by cookatrice on 2015. 8. 11..
 */
@Controller
@RequestMapping("/sankey")
public class SankeyController {

	@Autowired
	private SankeyService sankeyServiceImpl;

	/**
	 * load sankey chart data set
	 * @param sankeyRequest
	 * @return
	 */
	@RequestMapping(value = "/getDataSet", method = RequestMethod.GET)
	@ResponseBody
	public List<SankeyResponse> getSankeyDataSet(SankeyRequest sankeyRequest) {
		return sankeyServiceImpl.getSankeyDataSet(sankeyRequest);
	}
}
