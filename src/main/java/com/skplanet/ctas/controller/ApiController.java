package com.skplanet.ctas.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.skplanet.ctas.repository.querycache.QueryCacheRepository;
import com.skplanet.pandora.model.ApiResponse;

@RestController("ctasApiController")
@RequestMapping("api")
public class ApiController {

	@Autowired
	private QueryCacheRepository querycacheRepository;

	@PostMapping("/requestTransmission")
	public ApiResponse requestTransmission(@RequestParam Map<String, Object> params) {
		querycacheRepository.selectTargeting(params);
		return ApiResponse.builder().message("전송 요청 완료").build();
	}

}
