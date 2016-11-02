package com.skplanet.pandora.controller;

import java.io.IOException;
import java.nio.file.Paths;
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

import com.skplanet.pandora.repository.oracle.OracleRepository;
import com.skplanet.pandora.repository.querycache.QueryCacheRepository;
import com.skplanet.pandora.service.ForwardService;
import com.skplanet.pandora.service.NoticeService;
import com.skplanet.pandora.service.RequestService;
import com.skplanet.web.exception.BizException;
import com.skplanet.web.model.ApiResponse;
import com.skplanet.web.model.AutoMap;
import com.skplanet.web.model.TransmissionType;
import com.skplanet.web.model.UploadProgress;
import com.skplanet.web.repository.oracle.UploadTempRepository;
import com.skplanet.web.service.IdmsLogService;
import com.skplanet.web.service.SshService;
import com.skplanet.web.service.UploadService;
import com.skplanet.web.util.Constant;
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
	private ForwardService forwardService;

	@Autowired
	private RequestService ptsService;

	@Autowired
	private SshService sshService;

	@Autowired
	private NoticeService noticeService;

	@Autowired
	private IdmsLogService idmsLogService;

	@PostMapping("files")
	public ApiResponse handleUpload(@RequestParam("file") MultipartFile file, @RequestParam String pageId,
			@RequestParam String columnName) throws IOException {

		String username = Helper.currentUser().getUsername();

		log.info("Uploading file... pageId={}, username={}, columnName={}", pageId, username, columnName);

		if (file.isEmpty()) {
			throw new BizException("빈 파일입니다");
		}

		JobParameters jobParameters = uploadService.readyToImport(file, pageId, username, columnName);

		uploadService.beginImport(jobParameters);

		return ApiResponse.builder().message("업로드 성공").build();
	}

	@GetMapping("files")
	public ApiResponse getUploaded(@RequestParam String pageId,
			@RequestParam(defaultValue = "false") boolean countOnly) {

		String username = Helper.currentUser().getUsername();

		if (countOnly) {
			UploadProgress uploadProgress = uploadService.getUploadProgress(pageId, username);

			int count = uploadTempRepository.countUploaded(pageId, username);

			return ApiResponse.builder().message(uploadProgress.getUploadStatus().toString()).totalItems(count).build();
		} else {
			List<AutoMap> list = uploadTempRepository.selectUploaded(pageId, username);
			return ApiResponse.builder().value(list).build();
		}
	}

	@GetMapping("/members")
	public ApiResponse getMembers(@RequestParam String pageId, @RequestParam(defaultValue = "0") int offset,
			@RequestParam(defaultValue = "20") int limit) {

		String username = Helper.currentUser().getUsername();

		UploadProgress uploadProgress = uploadService.getFinishedUploadProgress(pageId, username);
		List<AutoMap> list = oracleRepository.selectMembers(uploadProgress, offset, limit, true);
		int count = oracleRepository.countMembers(uploadProgress);

		idmsLogService.memberSearch(Helper.nowDateTimeString(), username, Helper.currentClientIp(), null, pageId,
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

		idmsLogService.memberSearch(Helper.nowDateTimeString(), username, Helper.currentClientIp(), mbrId,
				(String) params.get("pageId"), 1);

		return list;
	}

	@GetMapping("/agreementInfo")
	public List<AutoMap> getAgreementInfo(@RequestParam Map<String, Object> params) {

		String mbrId = oracleRepository.selectMbrId(params);
		List<AutoMap> list = querycacheRepository.selectAgreementInfo(mbrId);

		String username = Helper.currentUser().getUsername();

		idmsLogService.memberSearch(Helper.nowDateTimeString(), username, Helper.currentClientIp(), mbrId,
				(String) params.get("pageId"), 1);

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

		idmsLogService.memberSearch(Helper.nowDateTimeString(), username, Helper.currentClientIp(), mbrId,
				(String) params.get("pageId"), 1);

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
	public ApiResponse sendPts(@RequestParam boolean ptsMasking, @RequestParam String pageId) {

		String username = Helper.currentUser().getUsername();
		String ptsUsername = Helper.currentUser().getPtsUsername();

		UploadProgress uploadProgress = uploadService.getFinishedUploadProgress(pageId, username);

		ptsService.sendToPts(ptsUsername, ptsMasking, uploadProgress);

		return ApiResponse.builder().message("PTS 전송 성공").build();
	}

	@PostMapping("/extractMemberInfo")
	public ApiResponse extractMemberInfo(@RequestParam String pageId, @RequestParam String inputDataType,
			@RequestParam String periodType, @RequestParam String periodFrom, @RequestParam String periodTo) {

		String username = Helper.currentUser().getUsername();

		UploadProgress uploadProgress = uploadService.getFinishedUploadProgress(pageId, username);

		forwardService.sendForExtraction(Paths.get(Constant.APP_FILE_DIR, uploadProgress.getFilename()));

		sshService.execute(username, inputDataType, periodType, periodFrom, periodTo, uploadProgress.getFilename());

		return ApiResponse.builder().message("추출 완료").build();
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
		TransmissionType notiType = TransmissionType.valueOf(((String) params.get("notiType")).toUpperCase());

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
			noticeService.noticeUsingFtp(params, TransmissionType.OCBCOM);
			noticeService.noticeUsingFtp(params, TransmissionType.EM);
			noticeService.noticeUsingSms(params);
			break;
		default:
			throw new BizException("전송 대상이 지정되지 않았습니다");
		}

		return ApiResponse.builder().message("전송 완료").build();
	}

}
