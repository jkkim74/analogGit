package com.skplanet.pandora.controller;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.springframework.batch.core.JobParameters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.skplanet.pandora.model.TransmissionType;
import com.skplanet.pandora.repository.oracle.OracleRepository;
import com.skplanet.pandora.repository.querycache.QueryCacheRepository;
import com.skplanet.pandora.service.TransmissionService;
import com.skplanet.web.exception.BizException;
import com.skplanet.web.model.ApiResponse;
import com.skplanet.web.model.AutoMap;
import com.skplanet.web.model.MenuProgress;
import com.skplanet.web.model.ProgressStatus;
import com.skplanet.web.repository.oracle.UploadTempRepository;
import com.skplanet.web.security.UserInfo;
import com.skplanet.web.service.IdmsLogService;
import com.skplanet.web.service.UploadService;
import com.skplanet.web.util.Helper;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("api")
@Slf4j
public class ApiController {

	@Autowired
	private OracleRepository oracleRepository;

	@Autowired
	private QueryCacheRepository querycacheRepository;

	@Autowired
	private UploadTempRepository uploadTempRepository;

	@Autowired
	private UploadService uploadService;

	@Autowired
	private TransmissionService transmissionService;

	@Autowired
	private IdmsLogService idmsLogService;

	@PostMapping("files")
	public ApiResponse handleUpload(@RequestParam("file") MultipartFile file, @RequestParam String menuId,
			@RequestParam String param) throws IOException {

		String username = Helper.currentUser().getUsername();

		log.info("Uploading file... menuId={}, username={}, param={}", menuId, username, param);

		if (file.isEmpty()) {
			throw new BizException("빈 파일입니다");
		}

		JobParameters jobParameters = uploadService.readyToImport(file, menuId, username, param);

		uploadService.beginImport(jobParameters);

		return ApiResponse.builder().message("업로드 성공").build();
	}

	@GetMapping("files")
	public ApiResponse getUploaded(@RequestParam String menuId,
			@RequestParam(defaultValue = "false") boolean countOnly) {

		String username = Helper.currentUser().getUsername();

		if (countOnly) {
			MenuProgress progress = uploadService.getMenuProgress(menuId, username);

			int count = uploadTempRepository.countUploaded(menuId, username);

			return ApiResponse.builder().message(progress.getStatus().toString()).totalItems(count).build();
		} else {
			List<AutoMap> list = uploadTempRepository.selectUploaded(menuId, username);
			return ApiResponse.builder().value(list).build();
		}
	}

	@GetMapping("/members")
	public ApiResponse getMembers(@RequestParam String menuId, @RequestParam(defaultValue = "0") int offset,
			@RequestParam(defaultValue = "20") int limit) {

		String username = Helper.currentUser().getUsername();

		MenuProgress progress = uploadService.getFinishedMenuProgress(menuId, username);
		List<AutoMap> list = oracleRepository.selectMembers(progress, offset, limit, true);
		int count = oracleRepository.countMembers(progress);

		idmsLogService.memberSearch(Helper.nowDateTimeString(), username, Helper.currentClientIp(), null, null, menuId,
				list.size());

		return ApiResponse.builder().value(list).totalItems(count).build();
	}

	@GetMapping("/memberInfo")
	public List<AutoMap> getMemberInfo(@RequestParam Map<String, Object> params) {

		String mbrId = oracleRepository.selectMbrId(params);
		params.put("searchType", "mbrId");
		params.put("searchKeyword", mbrId);

		List<AutoMap> list = oracleRepository.selectMemberInfo(params);

		String username = Helper.currentUser().getUsername();
		String mbrKorNm = oracleRepository.selectMbrKorNm(mbrId);

		idmsLogService.memberSearch(Helper.nowDateTimeString(), username, Helper.currentClientIp(), mbrId, mbrKorNm,
				(String) params.get("menuId"), 1);

		return list;
	}

	@GetMapping("/agreementInfo")
	public List<AutoMap> getAgreementInfo(@RequestParam Map<String, Object> params) {

		String mbrId = oracleRepository.selectMbrId(params);
		List<AutoMap> list = querycacheRepository.selectAgreementInfo(mbrId);

		String username = Helper.currentUser().getUsername();
		String mbrKorNm = oracleRepository.selectMbrKorNm(mbrId);

		idmsLogService.memberSearch(Helper.nowDateTimeString(), username, Helper.currentClientIp(), mbrId, mbrKorNm,
				(String) params.get("menuId"), 1);

		return list;
	}

	@GetMapping("/joinInfoOcbapp")
	public List<AutoMap> getJoinInfoOcbapp(@RequestParam Map<String, Object> params) {

		String mbrId = oracleRepository.selectMbrId(params);
		List<AutoMap> result = querycacheRepository.selectJoinInfo(mbrId);

		if (result == null || result.size() <= 0 || result.get(0) == null) {
			AutoMap m = new AutoMap();
			m.put("OCB_APP_JOIN_YN", "N");
			result.clear();
			result.add(m);
		} else {
			log.info("{}", result);
			AutoMap m = result.get(0);

			m.put("OCB_APP_JOIN_YN", "Y");
		}

		return result;
	}

	@GetMapping("/joinInfoOcbcom")
	public List<AutoMap> getJoinInfoOcbcom(@RequestParam Map<String, Object> params) {

		return oracleRepository.selectJoinInfo(params);
	}

	@GetMapping("/lastestUsageInfo")
	public List<AutoMap> getLastestUsageInfo(@RequestParam Map<String, Object> params) {

		return oracleRepository.selectLastestUsageInfo(params);
	}

	@GetMapping("/marketingMemberInfo")
	public List<AutoMap> getMarketingMemberInfo(@RequestParam Map<String, Object> params) {

		return oracleRepository.selectMarketingMemberInfo(params);
	}

	@GetMapping("/marketingMemberInfoHistory")
	public List<AutoMap> getMarketingMemberInfoHistory(@RequestParam Map<String, Object> params) {

		return oracleRepository.selectMarketingMemberInfoHistory(params);
	}

	@GetMapping("/thirdPartyProvideHistory")
	public List<AutoMap> getThirdPartyProvideHistory(@RequestParam Map<String, Object> params) {

		return oracleRepository.selectThirdPartyProvideHistory(params);
	}

	@GetMapping("/cardList")
	public List<AutoMap> getCardList(@RequestParam Map<String, Object> params) {

		return oracleRepository.selectCardList(params);
	}

	@GetMapping("/transactionHistory")
	public List<AutoMap> getTransactionHistory(@RequestParam Map<String, Object> params) {

		String mbrId = oracleRepository.selectMbrId(params);
		List<AutoMap> list = querycacheRepository.selectTransactionHistory(mbrId);

		String username = Helper.currentUser().getUsername();
		String mbrKorNm = oracleRepository.selectMbrKorNm(mbrId);

		idmsLogService.memberSearch(Helper.nowDateTimeString(), username, Helper.currentClientIp(), mbrId, mbrKorNm,
				(String) params.get("menuId"), 1);

		return list;
	}

	@GetMapping("/emailSendHistory")
	public List<AutoMap> getEmailSendHistory(@RequestParam Map<String, Object> params) {

		String mbrId = oracleRepository.selectMbrId(params);
		return querycacheRepository.selectEmailSendHistory(mbrId);
	}

	@GetMapping("/appPushHistory")
	public List<AutoMap> getAppPushHistory(@RequestParam Map<String, Object> params) {

		String mbrId = oracleRepository.selectMbrId(params);
		return querycacheRepository.selectAppPushHistory(mbrId);
	}

	@GetMapping("/clphnNoDup")
	public List<AutoMap> getClphnNoDup(@RequestParam Map<String, Object> params) {
		return oracleRepository.selectClphnNoDup(params);
	}

	@GetMapping("/emailAddrDup")
	public List<AutoMap> getEmailAddrDup(@RequestParam Map<String, Object> params) {
		return oracleRepository.selectEmailAddrDup(params);
	}

	@PostMapping("/sendPts")
	public ApiResponse sendPts(@RequestParam boolean ptsMasking, @RequestParam(defaultValue = "") String ptsPrefix,
			@RequestParam String menuId) {

		String username = Helper.currentUser().getUsername();
		String ptsUsername = Helper.currentUser().getPtsUsername();

		MenuProgress progress = uploadService.getFinishedMenuProgress(menuId, username);

		transmissionService.sendToPts(ptsUsername, ptsMasking, ptsPrefix, progress);

		return ApiResponse.builder().message("PTS 전송 성공").build();
	}

	@PostMapping("/extractMemberInfo")
	public ApiResponse extractMemberInfo(@RequestParam String menuId, @RequestParam String inputDataType,
			@RequestParam String periodType, @RequestParam String periodFrom, @RequestParam String periodTo,
			@RequestParam boolean ptsMasking, @RequestParam(defaultValue = "") String ptsPrefix) {

		UserInfo user = Helper.currentUser();
		String username = user.getUsername();
		String ptsUsername = user.getPtsUsername();
		String emailAddr = user.getEmailAddr();

		// 임시로 하드코딩 수정 menuId 새로 생성
		MenuProgress progress = uploadService.getFinishedMenuProgressTmp(menuId, username);
		uploadService.getFinishedMenuProgressTmp("PAN0005", username);
		// 임시로 하드코딩 수정 menuId 새로 생성
		uploadService.markStatus(ProgressStatus.PROCESSING, "PAN0005", username, null, null);

		transmissionService.sendForExtraction(username, inputDataType, periodType, periodFrom, periodTo, ptsUsername,
				ptsMasking, ptsPrefix, emailAddr, progress);

		idmsLogService.memberSearch(Helper.nowDateTimeString(), username, Helper.currentClientIp(), null, null, menuId,
				250); // 다른 화면에서 보통 고객조회 첫페이지로 250건 조회하므로 일관성맞춤.

		return ApiResponse.builder().message("PTS전송되었습니다. 메일로 안내됩니다.").build();
	}

	@GetMapping("/extinctionSummary")
	public List<AutoMap> getExtinctionSummary(@RequestParam Map<String, Object> params) {
		return oracleRepository.selectExtinctionSummary(params);
	}

	@GetMapping("/extinctionTargets")
	public ApiResponse getExtinctionTargets(@RequestParam Map<String, Object> params) {

		List<AutoMap> list = oracleRepository.selectExtinctionTargetsMas(params);
		int count = oracleRepository.countExtinctionTargets(params);
		return ApiResponse.builder().value(list).totalItems(count).build();
	}

	@PostMapping("/noticeExtinction")
	public ApiResponse noticeExtinction(@RequestParam Map<String, Object> params) {
		TransmissionType transmissionType = TransmissionType
				.valueOf(params.get("transmissionType").toString().toUpperCase());

		switch (transmissionType) {
		case OCBCOM:
		case EM:
			transmissionService.sendToFtpForExtinction(params, transmissionType);
			break;
		case SMS:
			transmissionService.sendToSms(params);
			break;
		case TM:
			break;
		case ALL:
			transmissionService.sendToFtpForExtinction(params, TransmissionType.OCBCOM);
			transmissionService.sendToFtpForExtinction(params, TransmissionType.EM);
			transmissionService.sendToSms(params);
			break;
		default:
			throw new BizException("전송 대상이 지정되지 않았습니다");
		}

		return ApiResponse.builder().message("전송 완료").build();
	}

}
