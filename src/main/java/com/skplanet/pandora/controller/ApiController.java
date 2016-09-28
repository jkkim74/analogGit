package com.skplanet.pandora.controller;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;

import org.apache.commons.csv.CSVPrinter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.skplanet.pandora.common.BizException;
import com.skplanet.pandora.common.CsvCreatorTemplate;
import com.skplanet.pandora.common.Helper;
import com.skplanet.pandora.model.ApiResponse;
import com.skplanet.pandora.model.AutoMappedMap;
import com.skplanet.pandora.model.UploadProgress;
import com.skplanet.pandora.repository.oracle.OracleRepository;
import com.skplanet.pandora.repository.querycache.QueryCacheRepository;
import com.skplanet.pandora.service.FtpService;
import com.skplanet.pandora.service.PtsService;
import com.skplanet.pandora.service.SmsService;
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
	private SmsService smsService;

	@Autowired
	private FtpService ftpService;

	@Value("${ftp.notice.host}")
	private String ftpHost;

	@Value("${ftp.notice.port}")
	private int ftpPort;

	@Value("${ftp.notice.username}")
	private String ftpUsername;

	@Value("${ftp.notice.password}")
	private String ftpPassword;

	@GetMapping("/members")
	public ApiResponse getMembers(@RequestParam String pageId, @RequestParam(defaultValue = "0") int offset,
			@RequestParam(defaultValue = "20") int limit) {

		String username = AuthController.getUserInfo().getUsername();

		UploadProgress uploadProgress = uploadService.getFinishedUploadProgress(pageId, username);
		List<AutoMappedMap> list = oracleRepository.selectMembers(uploadProgress, offset, limit);
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
	public ApiResponse sendPts(@RequestParam String ptsUsername, @RequestParam String ptsMasking,
			@RequestParam String pageId) {

		String username = AuthController.getUserInfo().getUsername();

		UploadProgress uploadProgress = uploadService.getFinishedUploadProgress(pageId, username);

		ptsService.send(ptsUsername, uploadProgress);

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

	@GetMapping("/expirePointTargets")
	public List<AutoMappedMap> getExpirePointTargets(@RequestParam Map<String, Object> params) {
		return oracleRepository.selectExpirePointTargets(params);
	}

	@GetMapping("/noticeResults")
	public ApiResponse getNoticeResults(@RequestParam Map<String, Object> params) {

		List<AutoMappedMap> list = oracleRepository.selectNoticeResults(params);
		int count = oracleRepository.countNoticeResults(params);
		return ApiResponse.builder().value(list).totalRecords(count).build();
	}

	@PostMapping("/noticeExpirePoint")
	public ApiResponse noticeExpirePoint(@RequestParam Map<String, Object> params) {
		String notiTarget = (String) params.get("notiTarget");

		switch (notiTarget) {
		case "ocbcom":
		case "em":
			noticeUsingFtp(params, notiTarget);
			break;
		case "sms":
			noticeUsingSms(params);
			break;
		case "tm":
			break;
		case "all":
			noticeUsingFtp(params, "ocbcom");
			noticeUsingFtp(params, "em");
			noticeUsingSms(params);
			break;
		default:
			throw new BizException("전송 대상이 지정되지 않았습니다");
		}

		return ApiResponse.builder().message("전송 완료").build();
	}

	private void noticeUsingFtp(final Map<String, Object> params, final String notiTarget) {

		String remotePath = "";
		if ("ocbcom".equals(notiTarget)) {
			remotePath = "pointExEmail/extinction_" + Helper.nowDateString() + ".txt";
		} else if ("em".equals(notiTarget)) {
			remotePath = "pointExEmail/extinction_em_" + Helper.nowDateString() + ".txt";
		}

		CsvCreatorTemplate<AutoMappedMap> csvCreator = new CsvCreatorTemplate<AutoMappedMap>() {

			int offset = 0;
			int limit = 1000;

			@Override
			public List<AutoMappedMap> nextList() {
				params.put("offset", offset);
				params.put("limit", limit);

				List<AutoMappedMap> list = oracleRepository.selectNoticeResults(params);

				offset += limit;
				return list;
			}

			@Override
			public void printEach(CSVPrinter printer, AutoMappedMap map) throws IOException {
				String extnctObjDt = (String) map.get("extnctObjDt");

				if ("ocbcom".equals(notiTarget)) {
					// 소명예정년,소멸예정월,소멸예정일,EC_USER_ID
					printer.printRecord(extnctObjDt.substring(0, 4), extnctObjDt.substring(4, 6),
							extnctObjDt.substring(6, 8), map.get("unitedId"));
				} else if ("em".equals(notiTarget)) {
					String mbrId = (String) map.get("mbrId");
					String unitedId = (String) map.get("unitedId");
					String encrypted = Helper.skpEncrypt(mbrId + "," + unitedId);

					// 소명예정년,소멸예정월,소멸예정일,고객성명,이메일주소,암호화값
					printer.printRecord(extnctObjDt.substring(0, 4), extnctObjDt.substring(4, 6),
							extnctObjDt.substring(6, 8), map.get("mbrKorNm"), map.get("emailAddr"), encrypted);
				}
			}

		};

		Path filePath = null;
		try {
			filePath = csvCreator.create(Helper.uniqueCsvFilename());
		} catch (IOException e) {
			throw new BizException("CSV 파일 생성 실패", e);
		}

		ftpService.send(filePath, remotePath, ftpHost, ftpPort, ftpUsername, ftpPassword);
	}

	private void noticeUsingSms(final Map<String, Object> params) {
		params.put("noPaging", true);
		List<AutoMappedMap> list = oracleRepository.selectNoticeResults(params);

		smsService.send(list);
	}

}
