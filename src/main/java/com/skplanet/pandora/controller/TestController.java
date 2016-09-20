package com.skplanet.pandora.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.skplanet.pandora.model.AutoMappedMap;
import com.skplanet.pandora.model.UploadStatus;
import com.skplanet.pandora.repository.mysql.MysqlRepository;
import com.skplanet.pandora.repository.oracle.OracleRepository;
import com.skplanet.pandora.repository.querycache.QueryCacheRepository;
import com.skplanet.pandora.service.MailService;
import com.skplanet.pandora.service.SmsService;

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

	@Transactional("mysqlTxManager")
	@GetMapping("/testError")
	public void testError(@RequestParam Map<String, Object> params, Model model) {
		mysqlRepository.upsertUploadProgress("pan0101", "test", "MY_COL", UploadStatus.RUNNING);
		throw new RuntimeException("custom error");
	}

	@Autowired
	private MailService mailService;

	@GetMapping("/testMail")
	public void testMail(@RequestParam Map<String, Object> params) {
		Map<String, Object> model = new HashMap<>();
		model.put("msg", new Date());
		mailService.send((String) params.get("to"), "TEST", "pan0104.vm", model);
	}

	@Autowired
	private SmsService smsService;

	@GetMapping("/testSms")
	public void testSms(@RequestParam Map<String, Object> params) {
		Map<String, Object> model = new HashMap<>();
		model.put("msg", new Date());
		smsService.send(new String[] { (String) params.get("to") }, "pan0104.vm", model);
	}

	@GetMapping("/testClientIp")
	public Map<String, Object> testClientIp() {
		HttpServletRequest req = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes())
				.getRequest();
		
		String ip = req.getHeader("X-Forwarded-For");
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = req.getHeader("Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = req.getHeader("WL-Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = req.getHeader("HTTP_CLIENT_IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = req.getHeader("HTTP_X_FORWARDED_FOR");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = req.getRemoteAddr();
		}

		// 여러 프록시를 경유했을 경우 콤마로 여러 IP가 누적됨. 맨 왼쪽 IP가 end-user's IP.
		ip = ip.split(",")[0];

		Map<String, Object> map = new HashMap<>();
		map.put("clientIp", ip);
		map.put("getRemoteAddr()", req.getRemoteAddr());

		return map;
	}

}
