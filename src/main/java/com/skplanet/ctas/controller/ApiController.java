package com.skplanet.ctas.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.skplanet.ctas.repository.oracle.OracleRepository;
import com.skplanet.ctas.repository.querycache.QueryCacheRepository;
import com.skplanet.ocb.util.ApiResponse;
import com.skplanet.ocb.util.AutoMappedMap;
import com.skplanet.pandora.controller.AuthController;

@RestController("ctasApiController")
@RequestMapping("api")
public class ApiController {

	@Autowired
	private OracleRepository oracleRepository;

	@Autowired
	private QueryCacheRepository querycacheRepository;

	@GetMapping("/campaigns")
	public ApiResponse getCampaigns(@RequestParam Map<String, Object> params) {
		List<AutoMappedMap> list = oracleRepository.selectCampaign(params);
		int count = oracleRepository.countCampaign(params);
		return ApiResponse.builder().value(list).totalItems(count).build();
	}

	@PostMapping("/campaigns")
	@Transactional("oracleTxManager")
	public ApiResponse saveCampaign(@RequestParam Map<String, Object> params) {
		if (params.get("cmpgnId") == null) {
			String campaignId = oracleRepository.nextCampaignId();
			params.put("cmpgnId", campaignId);
		}

		String username = AuthController.getUserInfo().getUsername();
		params.put("username", username);

		oracleRepository.upsertCampaign(params);

		return ApiResponse.builder().message("저장 완료").value(params).build();
	}

	@GetMapping("/campaigns/targeting")
	public ApiResponse getCampaignTargetingInfo(@RequestParam Map<String, Object> params) {
		List<AutoMappedMap> list = oracleRepository.selectCampaignTargetingInfo(params);
		return ApiResponse.builder().value(list).build();
	}

	@PostMapping("/campaigns/targeting")
	@Transactional("oracleTxManager")
	public ApiResponse saveCampaignTargetingInfo(@RequestParam Map<String, Object> params) {
		String username = AuthController.getUserInfo().getUsername();
		String campaignId = (String) params.get("cmpgnId");
		params.remove("cmpgnId");

		oracleRepository.deleteCampaignTargetingInfo(campaignId);
		oracleRepository.insertCampaignTargetingInfo(username, campaignId, params);

		return ApiResponse.builder().message("타게팅 정보 저장 완료").build();
	}

	@PostMapping("/requestTransmission")
	public ApiResponse requestTransmission(@RequestParam Map<String, Object> params) {
		querycacheRepository.selectTargeting(params);
		return ApiResponse.builder().message("전송 요청 완료").build();
	}

}
