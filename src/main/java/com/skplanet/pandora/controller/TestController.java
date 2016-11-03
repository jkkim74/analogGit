package com.skplanet.pandora.controller;

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.skplanet.web.service.IdmsLogService;
import com.skplanet.web.util.Helper;

@RestController
@RequestMapping("test")
public class TestController {

	@Autowired
	private IdmsLogService idmsLogService;

	@GetMapping("idms")
	public void idms() {
		idmsLogService.send();
	}

	@GetMapping("ip")
	public HashMap<String, String> ip(HttpServletRequest req) {
		HashMap<String, String> map = new HashMap<>();

		// 프록시에서 remote ip address를 얻기위한 가능한 방법들.
		map.put("X-Forwarded-For}", req.getHeader("X-Forwarded-For"));
		map.put("Proxy-Client-IP", req.getHeader("Proxy-Client-IP"));
		map.put("WL-Proxy-Client-IP", req.getHeader("WL-Proxy-Client-IP"));
		map.put("HTTP_CLIENT_IP", req.getHeader("HTTP_CLIENT_IP"));
		map.put("HTTP_X_FORWARDED_FOR", req.getHeader("HTTP_X_FORWARDED_FOR"));
		map.put("getRemoteAddr", req.getRemoteAddr());
		map.put("serverIp", Helper.serverIp());

		return map;
	}

}
