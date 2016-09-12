package com.skplanet.pandora.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.skplanet.pandora.model.AutoMappedMap;
import com.skplanet.pandora.model.UploadStatus;
import com.skplanet.pandora.repository.mysql.MysqlRepository;
import com.skplanet.pandora.repository.oracle.OracleRepository;
import com.skplanet.pandora.repository.querycache.QueryCacheRepository;

@RestController
public class TestController {

	@Autowired
	private MysqlRepository mysqlRepository;

	@RequestMapping(value = "/testMysql", method = RequestMethod.GET)
	public AutoMappedMap testMysql(@RequestParam Map<String, Object> params, Model model) {
		return mysqlRepository.selectTest(params);
	}

	@Autowired
	private OracleRepository oracleRepository;

	@RequestMapping(value = "/testOracle", method = RequestMethod.GET)
	public AutoMappedMap testOracle(@RequestParam Map<String, Object> params, Model model) {
		return oracleRepository.selectTest(params);
	}

	@Autowired
	private QueryCacheRepository queryCacheRepository;

	@RequestMapping(value = "/testQC", method = RequestMethod.GET)
	public List<AutoMappedMap> testQC(@RequestParam Map<String, Object> params, Model model) {
		return queryCacheRepository.selectTest(params);
	}

	@Transactional("mysqlTxManager")
	@RequestMapping(value = "/testError", method = RequestMethod.GET)
	public void testError(@RequestParam Map<String, Object> params, Model model) {
		mysqlRepository.upsertUploadProgress("pan0101", "test", "MY_COL", UploadStatus.RUNNING);
		throw new RuntimeException("custom error");
	}

}
