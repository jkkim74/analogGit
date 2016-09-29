package com.skplanet.pandora.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.skplanet.pandora.exception.BizException;
import com.skplanet.pandora.model.ApiResponse;
import com.skplanet.pandora.model.AutoMappedMap;
import com.skplanet.pandora.model.NotificationType;
import com.skplanet.pandora.model.UploadProgress;
import com.skplanet.pandora.repository.oracle.OracleRepository;
import com.skplanet.pandora.repository.querycache.QueryCacheRepository;
import com.skplanet.pandora.service.NoticeService;
import com.skplanet.pandora.service.PtsService;
import com.skplanet.pandora.service.SshService;
import com.skplanet.pandora.service.UploadService;

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
	private UploadService uploadService;

	@Autowired
	private PtsService ptsService;

	@Autowired
	private SshService sshService;

	@Autowired
	private NoticeService noticeService;

	@GetMapping("/members")
	public ApiResponse getMembers(@RequestParam String pageId, @RequestParam(defaultValue = "0") int offset,
			@RequestParam(defaultValue = "20") int limit) {

		String username = AuthController.getUserInfo().getUsername();

		UploadProgress uploadProgress = uploadService.getFinishedUploadProgress(pageId, username);
		List<AutoMappedMap> list = oracleRepository.selectMembers(uploadProgress, offset, limit, true);
		int count = oracleRepository.countMembers(uploadProgress);
		return ApiResponse.builder().value(list).totalRecords(count).build();
	}

	@GetMapping("/memberInfo")
	public List<AutoMappedMap> getMemberInfo(@RequestParam Map<String, Object> params) {

		return oracleRepository.selectMemberInfo(params);
	}

	@GetMapping("/agreementInfo")
	public List<AutoMappedMap> getAgreementInfo(@RequestParam Map<String, Object> params) {

		String mbrId = oracleRepository.selectMbrId(params);
		return querycacheRepository.selectAgreementInfo(mbrId);
	}

	@GetMapping("/joinInfoOcbapp")
	public List<AutoMappedMap> getJoinInfoOcbapp(@RequestParam Map<String, Object> params) {

		String mbrId = oracleRepository.selectMbrId(params);
		List<AutoMappedMap> result = querycacheRepository.selectJoinInfo(mbrId);

		if (result == null || result.size() <= 0) {
			AutoMappedMap m = new AutoMappedMap();
			m.put("OCB_APP_JOIN_YN", "N");
			result.add(m);
		} else {
			log.info("{}", result);
			AutoMappedMap m = result.get(0);

			m.put("OCB_APP_JOIN_YN", "Y");
		}

		return result;
	}

	@GetMapping("/joinInfoOcbcom")
	public List<AutoMappedMap> getJoinInfoOcbcom(@RequestParam Map<String, Object> params) {

		return oracleRepository.selectJoinInfo(params);
	}

	@GetMapping("/lastestUsageInfo")
	public List<AutoMappedMap> getLastestUsageInfo(@RequestParam Map<String, Object> params) {

		return oracleRepository.selectLastestUsageInfo(params);
	}

	@GetMapping("/marketingMemberInfo")
	public List<AutoMappedMap> getMarketingMemberInfo(@RequestParam Map<String, Object> params) {

		return oracleRepository.selectMarketingMemberInfo(params);
	}

	@GetMapping("/marketingMemberInfoHistory")
	public List<AutoMappedMap> getMarketingMemberInfoHistory(@RequestParam Map<String, Object> params) {

		return oracleRepository.selectMarketingMemberInfoHistory(params);
	}

	@GetMapping("/thirdPartyProvideHistory")
	public List<AutoMappedMap> getThirdPartyProvideHistory(@RequestParam Map<String, Object> params) {

		return oracleRepository.selectThirdPartyProvideHistory(params);
	}

	@GetMapping("/cardList")
	public List<AutoMappedMap> getCardList(@RequestParam Map<String, Object> params) {

		return oracleRepository.selectCardList(params);
	}

	@GetMapping("/transactionHistory")
	public List<AutoMappedMap> getTransactionHistory(@RequestParam Map<String, Object> params) {

		String mbrId = oracleRepository.selectMbrId(params);
		return querycacheRepository.selectTransactionHistory(mbrId);
	}

	@GetMapping("/emailSendHistory")
	public List<AutoMappedMap> getEmailSendHistory(@RequestParam Map<String, Object> params) {

		String mbrId = oracleRepository.selectMbrId(params);
		return querycacheRepository.selectEmailSendHistory(mbrId);
	}

	@GetMapping("/appPushHistory")
	public List<AutoMappedMap> getAppPushHistory(@RequestParam Map<String, Object> params) {

		String mbrId = oracleRepository.selectMbrId(params);
		return querycacheRepository.selectAppPushHistory(mbrId);
	}

	@GetMapping("/clphnNoDup")
	public List<AutoMappedMap> getClphnNoDup(@RequestParam Map<String, Object> params) {
		return oracleRepository.selectClphnNoDup(params);
	}

	@GetMapping("/emailAddrDup")
	public List<AutoMappedMap> getEmailAddrDup(@RequestParam Map<String, Object> params) {
		return oracleRepository.selectEmailAddrDup(params);
	}

	@PostMapping("/sendPts")
	public ApiResponse sendPts(@RequestParam String ptsUsername, @RequestParam boolean ptsMasking,
			@RequestParam String pageId) {

		String username = AuthController.getUserInfo().getUsername();

		UploadProgress uploadProgress = uploadService.getFinishedUploadProgress(pageId, username);

		ptsService.send(ptsUsername, ptsMasking, uploadProgress);

		return ApiResponse.builder().message("PTS 전송 성공").build();
	}

	@PostMapping("/extractMemberInfo")
	public ApiResponse extractMemberInfo(@RequestParam String pageId, @RequestParam String inputDataType,
			@RequestParam String periodType, @RequestParam String periodFrom, @RequestParam String periodTo) {

		String username = AuthController.getUserInfo().getUsername();

		UploadProgress uploadProgress = uploadService.getFinishedUploadProgress(pageId, username);

		sshService.execute(username, inputDataType, periodType, periodFrom, periodTo, uploadProgress.getFilename());

		return ApiResponse.builder().message("추출 완료").build();
	}

	@GetMapping("/extinctionSummary")
	public List<AutoMappedMap> getExtinctionSummary(@RequestParam Map<String, Object> params) {
		return oracleRepository.selectExtinctionSummary(params);
	}

	@GetMapping("/extinctionTargets")
	public ApiResponse getExtinctionTargets(@RequestParam Map<String, Object> params) {

		List<AutoMappedMap> list = oracleRepository.selectExtinctionTargets(params);
		int count = oracleRepository.countExtinctionTargets(params);
		return ApiResponse.builder().value(list).totalRecords(count).build();
	}

	@PostMapping("/noticeExtinction")
	public ApiResponse noticeExtinction(@RequestParam Map<String, Object> params) {
		NotificationType notiType = NotificationType.valueOf(((String) params.get("notiType")).toUpperCase());

		switch (notiType) {
		case OCBCOM:
		case EM:
			noticeService.noticeUsingFtp(params, notiType);
			break;
		case SMS:
			noticeService.noticeUsingSms(params);
			break;
		case TM:
			break;
		case ALL:
			noticeService.noticeUsingFtp(params, NotificationType.OCBCOM);
			noticeService.noticeUsingFtp(params, NotificationType.EM);
			noticeService.noticeUsingSms(params);
			break;
		default:
			throw new BizException("전송 대상이 지정되지 않았습니다");
		}

		return ApiResponse.builder().message("전송 완료").build();
	}

}
