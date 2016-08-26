package com.skplanet.pandora.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.skplanet.pandora.model.AutoMappedMap;
import com.skplanet.pandora.repository.oracle.OracleRepository;

@RestController
@RequestMapping("api")
public class ApiController {

	@Autowired
	private OracleRepository oracleRepository;

	@RequestMapping(method = RequestMethod.GET, value = "/memberInfo")
	public List<AutoMappedMap> getUploadedPreview(@RequestParam Map<String, Object> params) {
		return oracleRepository.selectMemberInfo(params);
	}

}
