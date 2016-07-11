package com.skplanet.bisportal.controller.acl.access;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.skplanet.bisportal.common.acl.CookieUtils;
import com.skplanet.bisportal.common.model.JqGridRequest;
import com.skplanet.bisportal.common.utils.Utils;
import com.skplanet.bisportal.model.acl.AccessLog;
import com.skplanet.bisportal.service.acl.AccessService;

/**
 * Created by pepsi on 2014. 5. 28..
 */
@Slf4j
@Controller
@RequestMapping("/log")
public class AccessController {
	@Autowired
	private AccessService accessServiceImpl;

	/**
	 * 화면 접근 로그 처리
	 *
	 * @param accessLog
	 *            로그 정보
	 * @return 로그 정보.
	 */
	@RequestMapping(value = "/access", method = RequestMethod.GET)
	public @ResponseBody String accessLog(AccessLog accessLog, HttpServletRequest request) throws Exception {
		String result = "0";
		try {
			accessLog.setIp(CookieUtils.getIpAddress(request));
			accessLog.setAgent(Utils.getUserAgent(request));
			accessLog.setLastUpdate(Utils.getCreateDate());
			accessServiceImpl.createAccessLog(accessLog);
		} catch (Exception e) {
			log.error("AccessLog insert Failed. {}", e);
		}
		return result;
	}

    /**
     * retport 사용현황
     * @param jqGridRequest
     * @return
     * @throws Exception
     */
	@RequestMapping(value = "/reportUse", method = RequestMethod.GET)
	public @ResponseBody List<AccessLog> reportUseCount(JqGridRequest jqGridRequest) throws Exception {
		return accessServiceImpl.getReportUseCount(jqGridRequest);
	}
}
