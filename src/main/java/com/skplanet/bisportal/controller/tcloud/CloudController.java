package com.skplanet.bisportal.controller.tcloud;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.skplanet.bisportal.common.controller.ReportController;
import com.skplanet.bisportal.common.model.JqGridRequest;
import com.skplanet.bisportal.common.model.JqGridResponse;
import com.skplanet.bisportal.model.tcloud.TCloudMenuStat;
import com.skplanet.bisportal.service.tcloud.TcloudService;

/**
 * Created by seoseungho on 2014. 12. 23..
 */
@Controller
@RequestMapping(value = "/tcloud/cloud")
public class CloudController extends ReportController {
	@Autowired
	private TcloudService tcloudServiceImpl;

	@RequestMapping(value = "/getMenuStat", method = RequestMethod.GET)
	@ResponseBody
	public JqGridResponse<TCloudMenuStat> getMenuStat(JqGridRequest jqGridRequest) {
		JqGridResponse<TCloudMenuStat> jqGridResponse = new JqGridResponse<>();
		jqGridResponse.setRows(tcloudServiceImpl.getMenuStat(jqGridRequest));
		return jqGridResponse;
	}
}
