package com.skplanet.pandora.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.skplanet.web.service.IdmsLogService;

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
	public Model ip(HttpServletRequest req, Model model) {
		// 프록시에서 remote ip address를 얻기위한 가능한 방법들.
		model.addAttribute("X-Forwarded-For: {}", req.getHeader("X-Forwarded-For"));
		model.addAttribute("Proxy-Client-IP: {}", req.getHeader("Proxy-Client-IP"));
		model.addAttribute("WL-Proxy-Client-IP: {}", req.getHeader("WL-Proxy-Client-IP"));
		model.addAttribute("HTTP_CLIENT_IP: {}", req.getHeader("HTTP_CLIENT_IP"));
		model.addAttribute("HTTP_X_FORWARDED_FOR: {}", req.getHeader("HTTP_X_FORWARDED_FOR"));
		model.addAttribute("getRemoteAddr(): {}", req.getRemoteAddr());

		return model;
	}

}
