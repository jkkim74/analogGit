package com.skplanet.pandora.controller;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.skplanet.ocb.service.MailService;
import com.skplanet.ocb.service.SmsService;
import com.skplanet.ocb.util.AutoMappedMap;
import com.skplanet.pandora.repository.mysql.MysqlRepository;
import com.skplanet.pandora.repository.oracle.OracleRepository;
import com.skplanet.pandora.repository.querycache.QueryCacheRepository;

@RestController
public class TestController {

	@Autowired
	private MysqlRepository mysqlRepository;

	@GetMapping("/testMysql")
	public AutoMappedMap testMysql(@RequestParam Map<String, Object> params, Model model) {
		return mysqlRepository.selectTest(params);
	}

	@Autowired
	private OracleRepository oracleRepository;

	@GetMapping("/testOracle")
	public AutoMappedMap testOracle(@RequestParam Map<String, Object> params, Model model) {
		return oracleRepository.selectTest(params);
	}

	@Autowired
	private QueryCacheRepository queryCacheRepository;

	@GetMapping("/testQC")
	public List<AutoMappedMap> testQC(@RequestParam Map<String, Object> params, Model model) {
		return queryCacheRepository.selectTest(params);
	}

	@Autowired
	private MailService mailService;

	@GetMapping("/testMail")
	public void testMail(@RequestParam Map<String, Object> params) {
		Map<String, Object> model = new HashMap<>();
		model.put("mbrKorNm", "테스트");
		model.put("mm", "00");
		model.put("dd", "00");
		mailService.send((String) params.get("to"), "TEST BOSS", "pan0104.vm", model);
	}

	@Autowired
	private SmsService smsService;

	@GetMapping("/testSms")
	public void testSms(@RequestParam Map<String, Object> params) {
		Map<String, Object> model = new HashMap<>();
		model.put("mbrKorNm", "테스트");
		model.put("mm", "00");
		model.put("dd", "00");
		smsService.send(Arrays.asList((String) params.get("to")), "pan0104.vm", model);
	}

	@GetMapping("/testClientIp")
	public Map<String, Object> testClientIp() {
		HttpServletRequest req = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes())
				.getRequest();

		Map<String, Object> map = new HashMap<>();
		map.put("X-Forwarded-For", req.getHeader("X-Forwarded-For"));
		map.put("Proxy-Client-IP", req.getHeader("Proxy-Client-IP"));
		map.put("WL-Proxy-Client-IP", req.getHeader("WL-Proxy-Client-IP"));
		map.put("HTTP_CLIENT_IP", req.getHeader("HTTP_CLIENT_IP"));
		map.put("HTTP_X_FORWARDED_FOR", req.getHeader("HTTP_X_FORWARDED_FOR"));
		map.put("getRemoteAddr()", req.getRemoteAddr());

		return map;
	}

}
