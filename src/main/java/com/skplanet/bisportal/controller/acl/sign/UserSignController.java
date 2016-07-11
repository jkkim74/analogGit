package com.skplanet.bisportal.controller.acl.sign;

import com.google.common.collect.Maps;
import com.skplanet.bisportal.model.acl.UserSign;
import com.skplanet.bisportal.model.acl.UserSignMngmt;
import com.skplanet.bisportal.service.acl.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;

/**
 * Created by lko on 2014-09-18.
 */
@Slf4j
@Controller
public class UserSignController {
	@Autowired
	private UserService userServiceImpl;

	@RequestMapping(value = "/sign/cnt", method = RequestMethod.GET)
	public @ResponseBody Map<String, Object> countSign(@RequestParam("loginId") String loginId) {
		int count;
		Map<String, Object> queryParam = Maps.newHashMap();
		try {
			count = userServiceImpl.cntUserSign(loginId);
			queryParam.put("cnt", count);
		} catch (Exception e) {
			log.error("countSign error {}", e);
			queryParam.put("cnt", -1);
		}
		return queryParam;
	}

	@RequestMapping(value = "/sign/create", method = RequestMethod.POST)
	public @ResponseBody int sign(@RequestBody UserSign userSign) throws Exception {
		return userServiceImpl.userSign(userSign);
	}

	@RequestMapping(value = "/sign/get", method = RequestMethod.GET)
	public @ResponseBody List<UserSign> getSign(@RequestParam("loginId") String loginId) {
		List<UserSign> userSign;
		try {
			userSign = userServiceImpl.getUserSign(loginId);
		} catch (Exception e) {
			log.error("getSign error {}", e);
			return null;
		}
		return userSign;
	}

	@RequestMapping(value = "/sign/trm/get", method = RequestMethod.POST)
	public @ResponseBody int getCntSignTrm(@RequestBody UserSignMngmt userSignMngmt) {
		int count;
		try {
			count = userServiceImpl.getCntSignTrm(userSignMngmt);
		} catch (Exception e) {
			count = -1;
			log.error("getCntSignTrm error {}", e);
		}
		return count;
	}
}
