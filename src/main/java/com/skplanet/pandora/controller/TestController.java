package com.skplanet.pandora.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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

}
