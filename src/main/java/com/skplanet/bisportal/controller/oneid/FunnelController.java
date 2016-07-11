package com.skplanet.bisportal.controller.oneid;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.skplanet.bisportal.common.controller.ReportController;
import com.skplanet.bisportal.model.oneid.FunnelRequest;
import com.skplanet.bisportal.model.oneid.OidFunnel;
import com.skplanet.bisportal.service.oneid.FunnelService;

/**
 * The FunnelController class.
 *
 * @author HoJin-Ha (mimul@wiseeco.com)
 *
 */
@Controller
@RequestMapping("/oneid")
public class FunnelController extends ReportController {
	@Autowired
	private FunnelService funnelServiceImpl;

	@RequestMapping(value = "/funnel/chart", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, List<OidFunnel>> getFunnelDatas(FunnelRequest funnelRequest) throws Exception {
		return funnelServiceImpl.getFunnelDatas(funnelRequest);
	}
}
