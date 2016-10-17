package com.skplanet.ctas.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.batch.core.JobExecution;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.skplanet.ctas.repository.oracle.OracleRepository;
import com.skplanet.ctas.repository.querycache.QueryCacheRepository;
import com.skplanet.ocb.exception.BizException;
import com.skplanet.ocb.util.ApiResponse;
import com.skplanet.ocb.util.AutoMappedMap;
import com.skplanet.pandora.controller.AuthController;
import com.skplanet.pandora.service.UploadService;

import lombok.extern.slf4j.Slf4j;

@RestController("ctasApiController")
@RequestMapping("api")
@Slf4j
public class ApiController {

	@Autowired
	private OracleRepository oracleRepository;

	@Autowired
	private QueryCacheRepository querycacheRepository;

	@Autowired
	private UploadService uploadService;

	@GetMapping("/campaigns")
	public ApiResponse getCampaigns(@RequestParam Map<String, Object> params) {
		List<AutoMappedMap> list = oracleRepository.selectCampaigns(params);
		int count = oracleRepository.countCampaigns(params);
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

	@DeleteMapping("/campaigns")
	@Transactional("oracleTxManager")
	public ApiResponse deleteCampaign(@RequestParam Map<String, Object> params) {
		oracleRepository.deleteCampaignTargetingInfo(params);
		oracleRepository.deleteCampaignDetail(params);
		oracleRepository.deleteCampaign(params);
		return ApiResponse.builder().message("삭제 완료").value(params).build();
	}

	@GetMapping("/campaigns/targeting")
	public ApiResponse getCampaignTargetingInfo(@RequestParam Map<String, Object> params) {
		List<AutoMappedMap> list = oracleRepository.selectCampaignTargetingInfo(params);
		return ApiResponse.builder().value(list).build();
	}

	@PostMapping("/campaigns/targeting")
	public ApiResponse saveCampaignTargetingInfo(@RequestParam("file") MultipartFile file,
			@RequestParam Map<String, Object> params) throws IOException {

		String campaignId = (String) params.get("cmpgnId");
		if (campaignId == null) {
			campaignId = oracleRepository.nextCampaignId();
			params.put("cmpgnId", campaignId);
		}
		String username = AuthController.getUserInfo().getUsername();
		params.put("username", username);

		if (file == null || file.isEmpty()) {
			// 타겟팅 추출
			querycacheRepository.createTargetingTable(params);
			querycacheRepository.insertTargeting(params);

			AutoMappedMap campaign = saveCampaignInternal(params, campaignId, username);

			// 타겟팅 조건 저장
			oracleRepository.deleteCampaignTargetingInfo(params);
			oracleRepository.insertCampaignTargetingInfo(username, campaignId, removeUnnecessaryFields(params));

			campaign.put("targetingInfo", params);

			return ApiResponse.builder().message("타게팅 정보 저장 완료").value(campaign).build();
		} else {
			// CSV 업로드
			prepareTargetingCsvTable(params);

			importCsv(file, params, campaignId, username);

			oracleRepository.migrateCampaignTargetingCsv(params);

			int totCnt = oracleRepository.countCampaignTargetingCsvTmp(params);
			int dupDelCnt = oracleRepository.countCampaignTargetingCsv(params);

			params.put("totCnt", String.valueOf(totCnt));
			params.put("dupDelCnt", String.valueOf(dupDelCnt));
			params.put("stsFgCd", "02");
			params.put("objRegFgCd", "CSV");

			oracleRepository.upsertCampaign(params);

			AutoMappedMap campaign = oracleRepository.selectCampaign(params);

			return ApiResponse.builder().message("CSV 타게팅 정보 업로드 완료").value(campaign).build();
		}

	}

	public void importCsv(MultipartFile file, Map<String, Object> params, String campaignId, String username) {
		String columnName = (String) params.get("columnName");
		JobExecution execution = uploadService
				.beginImport(uploadService.readyToImport(file, campaignId, username, columnName));

		while (execution.isRunning()) {
			try {
				Thread.sleep(1000L);
			} catch (InterruptedException e) {
				throw new BizException("", e);
			}
		}
	}

	private void prepareTargetingCsvTable(Map<String, Object> params) {
		if (oracleRepository.countCampaignTargetingCsvTable(params) <= 0) {
			oracleRepository.createCampaignTargetingCsvTable(params);
		}
		oracleRepository.truncateCampaignTargetingCsvTable(params);
	}

	private AutoMappedMap saveCampaignInternal(Map<String, Object> params, String campaignId, String username) {
		int extrctCnt = querycacheRepository.countTargeting(params);

		log.debug("extrctCnt={}", extrctCnt);

		HashMap<String, Object> map = new HashMap<>();
		map.put("cmpgnId", campaignId);
		map.put("username", username);
		map.put("totCnt", String.valueOf(extrctCnt));
		map.put("dupDelCnt", String.valueOf(extrctCnt));
		map.put("stsFgCd", "02");
		map.put("objRegFgCd", "TRGT");

		oracleRepository.upsertCampaign(map);

		return oracleRepository.selectCampaign(map);
	}

	private static Map<String, Object> removeUnnecessaryFields(Map<String, Object> map) {
		map.remove("cmpgnId");
		map.remove("pageId");
		map.remove("username");
		return map;
	}

	@GetMapping("/campaigns/detail")
	public ApiResponse getCampaignDetail(@RequestParam Map<String, Object> params) {
		List<AutoMappedMap> list = oracleRepository.selectCampaignDetails(params);
		return ApiResponse.builder().value(list).build();
	}

	@PostMapping("/campaigns/detail")
	public ApiResponse saveCampaignDetail(@RequestParam Map<String, Object> params) {
		boolean cellAdd = false;
		if (params.get("cellId") == null) {
			String cellId = oracleRepository.nextCellId(params);
			params.put("cellId", cellId);
			cellAdd = true;
		}

		String username = AuthController.getUserInfo().getUsername();
		params.put("username", username);

		oracleRepository.upsertCampaignDetail(params);

		if (cellAdd) {
			oracleRepository.balanceCellExtrctCnt(params);
		}
		return ApiResponse.builder().message("셀 저장 완료").build();
	}

	@DeleteMapping("/campaigns/detail")
	public ApiResponse deleteCampaignDetail(@RequestParam Map<String, Object> params) {
		oracleRepository.deleteCampaignDetail(params);
		oracleRepository.balanceCellExtrctCnt(params);
		return ApiResponse.builder().message("셀 삭제 완료").build();
	}

	@PostMapping("/requestTransmission")
	public ApiResponse requestTransmission(@RequestParam Map<String, Object> params) {
		// querycacheRepository.selectTargeting(params);
		return ApiResponse.builder().message("전송 요청 완료").build();
	}

}
