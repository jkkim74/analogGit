package com.skplanet.pandora.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.skplanet.pandora.repository.mysql.MysqlRepository;
import com.skplanet.pandora.repository.oracle.OracleRepository;
import com.skplanet.pandora.repository.querycache.QueryCacheRepository;

@RestController
public class TestController {

	@Autowired
	private MysqlRepository mysqlRepository;

	@RequestMapping(value = "/testMysql", method = RequestMethod.GET)
	public Map<String, Object> testMysql(Map<String, Object> params, Model model) {
		return mysqlRepository.selectTest(params);
	}

	@Autowired
	private OracleRepository oracleRepository;

	@RequestMapping(value = "/testOracle", method = RequestMethod.GET)
	public Map<String, Object> testOracle(Map<String, Object> params, Model model) {
		return oracleRepository.selectTest(params);
	}

	@Autowired
	private QueryCacheRepository queryCacheRepository;

	@RequestMapping(value = "/testQC", method = RequestMethod.GET)
	public Map<String, Object> testQC(Map<String, Object> params, Model model) {
		return queryCacheRepository.selectTest(params);
	}

}
