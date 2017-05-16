package com.skplanet.pandora.controller;

import com.skplanet.pandora.model.TransmissionType;
import com.skplanet.pandora.repository.oracle.OracleRepository;
import com.skplanet.pandora.repository.querycache.QueryCacheRepository;
import com.skplanet.pandora.service.TransmissionService;
import com.skplanet.web.exception.BizException;
import com.skplanet.web.model.ApiResponse;
import com.skplanet.web.model.AutoMap;
import com.skplanet.web.model.MenuProgress;
import com.skplanet.web.model.ProgressStatus;
import com.skplanet.web.repository.mysql.SingleReqRepository;
import com.skplanet.web.repository.oracle.UploadTempRepository;
import com.skplanet.web.security.UserInfo;
import com.skplanet.web.service.ExcelService;
import com.skplanet.web.service.IdmsLogService;
import com.skplanet.web.service.PtsService;
import com.skplanet.web.service.UploadService;
import com.skplanet.web.util.Constant;
import com.skplanet.web.util.Helper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.JobParameters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("api")
@Slf4j
public class ApiController {

	@Autowired
	private OracleRepository oracleRepository;

	@Autowired
	private QueryCacheRepository querycacheRepository;

	@Autowired
	private SingleReqRepository singleReqRepository;

	@Autowired
	private UploadTempRepository uploadTempRepository;

	@Autowired
	private UploadService uploadService;

	@Autowired
	private TransmissionService transmissionService;

	@Autowired
	private IdmsLogService idmsLogService;

	@Autowired
	private ExcelService excelService;	
	
	@Autowired
	private PtsService ptsService;	

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

	@GetMapping("/queryCacheTest")
	public List<AutoMap> getQueryCacheTest(@RequestParam Map<String, Object> params) {

//		log.info("call::testing.......");
//		log.info("call::getQuerycache...");
//		log.info("params={}", params);

//		String mbrId = oracleRepository.selectMbrId(params);
//		List<AutoMap> list = querycacheRepository.selectTrSingleRequest(params);
//		log.info("list={}", list);

//		String mbrId = String.valueOf(params.get("memberId"));
//		String mbrKorNm = oracleRepository.selectMbrKorNmQc(params);
//		log.info("username={}, mbrId={}, mbrKorNm={}", username, mbrId, mbrKorNm);

		return null;
	}


	@GetMapping("/memberLedger")
	public List<AutoMap> getMemberLedger(@RequestParam Map<String, Object> params) {
		params.put("ptsMasking", "true");
		List<AutoMap> result = oracleRepository.selectMemberLedger(params);
		idmsLogService.memberSearch(Helper.nowDateTimeString(), Helper.currentUser().getUsername(), Helper.currentClientIp(), null, null, (String) params.get("menuId"), result.size());
		return result;
	}
	@GetMapping("/marketingLedger")
	public List<AutoMap> getMarketingLedger(@RequestParam Map<String, Object> params) {
		params.put("ptsMasking", "true");
		List<AutoMap> result = oracleRepository.selectMarketingLedger(params);
		idmsLogService.memberSearch(Helper.nowDateTimeString(), Helper.currentUser().getUsername(), Helper.currentClientIp(), null, null, (String) params.get("menuId"), result.size());
		return result;
	}
	@GetMapping("/marketingHistory")
	public List<AutoMap> getMarketingHistory(@RequestParam Map<String, Object> params) {
		params.put("ptsMasking", "true");
		List<AutoMap> result = oracleRepository.selectMarketingHistory(params);
		idmsLogService.memberSearch(Helper.nowDateTimeString(), Helper.currentUser().getUsername(), Helper.currentClientIp(), null, null, (String) params.get("menuId"), result.size());
		return result;
	}

	private AutoMap makeHeader(String[] headerTexts){
        AutoMap tmpHeader = new AutoMap();
        for (int i = 0; i < headerTexts.length; i++) {
            tmpHeader.put(Integer.toString(i), headerTexts[i]);
        }
        return tmpHeader;
    }

    private AutoMap emptyLine(){
		AutoMap dummyMap = new AutoMap();
		dummyMap.put("dummy", "");
		return dummyMap;
	}

	@PostMapping("/sendPts")
	public ApiResponse sendPts(@RequestParam boolean ptsMasking, @RequestParam(defaultValue = "") String ptsPrefix,
					   @RequestParam String menuId, @RequestParam Map<String, Object> params) {

		log.info("call::sendPts...");
		log.info("ptsMasking={}, ptsPrefix={}, menuId={}, params={}", ptsMasking, ptsPrefix, menuId, params);

		String mbrId = oracleRepository.selectMbrId(params);
		String username = Helper.currentUser().getUsername();
		String ptsUsername = Helper.currentUser().getPtsUsername();

		if(!menuId.equals("PAN0105")){
			log.info("insert parms mbrId");
			params.put("mbrId", mbrId);
		}

		switch (menuId) {

			case "PAN0108": {
				log.info("call::PAN0108 PTS...");
				log.info("PAN0108 PTS call...params={}", params);

                if (Boolean.valueOf(String.valueOf(params.get("onHive")))) {
                    //hive call logic
					params.put("currentClientIp", Helper.currentClientIp());
					transmissionService.sendForSearchEmail(username, Helper.currentUser().getEmailAddr(), ptsUsername, ptsPrefix, params);
					return ApiResponse.builder().message("PTS전송되었습니다. 메일로 안내됩니다.").build();
                } else {
                    //oracle call logic
                    List<AutoMap> resultList = new ArrayList<>();

                    String headers[][] = {
                            {"MBR_ID","OCBCOM 로그인 ID","이메일 주소","중복여부","최종원장 변경일자"},
                            {"MBR_ID","OCBCOM 로그인 ID","이메일 주소","중복여부","최종원장 변경일자"},
                            {"MBR_ID","OCBCOM 로그인 ID","이메일 주소","최소일자","최종일자"}
                    };
                    List<AutoMap> list1 = oracleRepository.selectMemberLedger(params);
                    List<AutoMap> list2 = oracleRepository.selectMarketingLedger(params);
                    List<AutoMap> list3 = oracleRepository.selectMarketingHistory(params);
                    log.info("memberLedger={}, marketingLedger={}, marketingHistory={}"
                            , String.valueOf(list1), String.valueOf(list2), String.valueOf(list3));

                    resultList.add(makeHeader(headers[0]));
                    resultList.addAll(list1);
                    resultList.add(emptyLine());
                    resultList.add(makeHeader(headers[1]));
                    resultList.addAll(list2);
					resultList.add(emptyLine());
                    resultList.add(makeHeader(headers[2]));
                    resultList.addAll(list3);

                    log.info("PTS DATA...={}", resultList);

                    StringBuilder filename = new StringBuilder("P140802BKhub_").append(ptsUsername).append('_')
                            .append(Helper.nowDateTimeString()).append('_');
                    if (!StringUtils.isEmpty(ptsPrefix)) {
                        filename.append(ptsPrefix).append('-');
                    }
                    filename.append(username).append('-').append(Helper.nowDateTimeString()).append(".xls");

                    Path filePath = Paths.get(Constant.APP_FILE_DIR, filename.toString());
                    excelService.create(filePath, "이메일조회", resultList);
                    ptsService.send(filePath.toFile().getAbsolutePath(), ptsUsername);

					idmsLogService.memberSearch(Helper.nowDateTimeString(), username, Helper.currentClientIp(),
							null, null, (String) params.get("menuId"), list1.size()+list2.size()+list3.size());
                }
                break;

			}

			case "PAN0102": {

				List<AutoMap> list1;
				List<AutoMap> list2;
				List<AutoMap> list3;
				List<AutoMap> list4;
				List<AutoMap> list5;
				List<AutoMap> list6;

				if (ptsMasking) {
					list1 = oracleRepository.selectTotMbrInfo1(params);
					list2 = oracleRepository.selectTotMbrInfo2(params);
					list3 = oracleRepository.selectTotMbrInfo3(params);
					list4 = oracleRepository.selectTotMbrInfo4(params);
					list5 = oracleRepository.selectTotMbrInfo5(params);
					list6 = oracleRepository.selectTotMbrInfo6(params);
				} else {
					list1 = oracleRepository.selectTotMbrInfoNoMas1(params);
					list2 = oracleRepository.selectTotMbrInfoNoMas2(params);
					list3 = oracleRepository.selectTotMbrInfoNoMas3(params);
					list4 = oracleRepository.selectTotMbrInfoNoMas4(params);
					list5 = oracleRepository.selectTotMbrInfoNoMas5(params);
					list6 = oracleRepository.selectTotMbrInfoNoMas6(params);
				}

				String header1[] = {"회원구분", "회원상태", "회원ID", "CI번호", "포인트 소멸 여부", "한글성명", "법정생년월일", "성별", "OCB 최초 카드 본등록 일자", "휴대전화번호", "휴대전화번호 최종 유입 일자", "휴대전화번호 중복 여부", "자택전화번호", "자택전화번호 최종 유입 일자", "직장전화번호", "직장전화번호 최종 유입 일자", "자택주소", "자택주소 최종 유입 일자", "직장주소", "직장주소 최종 유입 일자", "이메일주소", "이메일주소 최종 유입 일자", "이메일주소 중복 여부", "개인정보 유효 기간 만료 예정일자"};
				String header2[] = {"OCB 적립/사용/할인/현금환급", "OCB 카드 본등록", "상담실 문의", "상품 구매", "포인트 조회"};
				String header3[] = {"한글성명", "휴대전화번호", "휴대전화번호 최종 유입 출처/일자", "휴대전화번호 중복 여부", "자택전화번호", "자택전화번호 최종 유입 출처/일자", "직장전화번호", "직장전화번호 최종 유입 출처/일자", "자택주소", "자택주소 최종 유입 출처/일자", "직장주소", "직장주소 최종 유입 출처/일자", "이메일주소", "이메일주소 최종 유입 출처/일자", "이메일주소 중복 여부"};
				String header4[] = {"유입기관코드", "동의버전", "동의일자", "동의여부", "한글성명", "생년월일", "성별", "이메일주소", "휴대전화번호", "자택전화번호", "직장전화번호"};
				String header5[] = {"제공대상기관", "유입기관코드", "동의버전", "동의일자", "동의여부", "한글성명", "생년월일", "성별", "이메일주소", "휴대전화번호", "자택전화번호", "직장전화번호"};
				String header6[] = {"카드코드 및 명칭", "카드상태", "카드번호", "카드발급일자", "카드본등록일자"};

				AutoMap dummyMap = new AutoMap();
				dummyMap.put("dummy", "");

				AutoMap hMap1 = new AutoMap();
				AutoMap hMap2 = new AutoMap();
				AutoMap hMap3 = new AutoMap();
				AutoMap hMap4 = new AutoMap();
				AutoMap hMap5 = new AutoMap();
				AutoMap hMap6 = new AutoMap();

				int i = 0;
				for (i = 0; i < header1.length; i++) {
					hMap1.put(Integer.toString(i), header1[i]);
				}
				for (i = 0; i < header2.length; i++) {
					hMap2.put(Integer.toString(i), header2[i]);
				}
				for (i = 0; i < header3.length; i++) {
					hMap3.put(Integer.toString(i), header3[i]);
				}
				for (i = 0; i < header4.length; i++) {
					hMap4.put(Integer.toString(i), header4[i]);
				}
				for (i = 0; i < header5.length; i++) {
					hMap5.put(Integer.toString(i), header5[i]);
				}
				for (i = 0; i < header6.length; i++) {
					hMap6.put(Integer.toString(i), header6[i]);
				}

				List<AutoMap> list = new ArrayList<>();

				list.add(hMap1);
				list.addAll(list1);
				list.add(dummyMap);
				list.add(hMap2);
				list.addAll(list2);
				list.add(dummyMap);
				list.add(hMap3);
				list.addAll(list3);
				list.add(dummyMap);
				list.add(hMap4);
				list.addAll(list4);
				list.add(dummyMap);
				list.add(hMap5);
				list.addAll(list5);
				list.add(dummyMap);
				list.add(hMap6);
				list.addAll(list6);

				StringBuilder filename = new StringBuilder("P140802BKhub_").append(ptsUsername).append('_')
						.append(Helper.nowDateTimeString()).append('_');
				if (!StringUtils.isEmpty(ptsPrefix)) {
					filename.append(ptsPrefix).append('-');
				}

				filename.append(username).append('-').append(Helper.nowDateTimeString()).append(".xls");

				Path filePath = Paths.get(Constant.APP_FILE_DIR, filename.toString());
				excelService.create(filePath, "고객정보 기본조회", list);

				ptsService.send(filePath.toFile().getAbsolutePath(), ptsUsername);
				break;
			}
			case "PAN0107": {

				List<AutoMap> list1 = querycacheRepository.selectAgreementInfo(mbrId);
				List<AutoMap> list2 = querycacheRepository.selectJoinInfo(mbrId);

				if (list2 == null || list2.size() <= 0 || list2.get(0) == null) {
					AutoMap m = new AutoMap();
					m.put("OCB_APP_JOIN_YN", "N");
					list2.clear();
					list2.add(m);
				} else {
					log.info("{}", list2);
					AutoMap m = list2.get(0);

					m.put("OCB_APP_JOIN_YN", "Y");
				}

				List<AutoMap> list3 = querycacheRepository.selectEmailSendHistory(mbrId);
				List<AutoMap> list4 = querycacheRepository.selectAppPushHistory(mbrId);
				List<AutoMap> list5 = oracleRepository.selectJoinInfo(params);

				AutoMap dummyMap = new AutoMap();
				dummyMap.put("dummy", "");

				String header1[] = {"회원ID", "마케팅 동의 여부", "마케팅 최종 동의 유입 출처/일자", "교차 활용 동의 여부", "TM 수신 동의 여부", "이메일 수신 동의 여부", "광고성 SMS 수신 동의 여부", "정보성 SMS 수신 동의 여부", "앱 푸시 동의 여부", "포인트 사용적립 동의 여부", "혜택/모바일전단 푸시 동의 여부", "친구와 함께쓰기 푸시 동의 여부", "코인알림 푸시 동의 여부", "위치활용 동의 여부", "BLE 동의 여부"};
				String header2[] = {"OCB앱 최종 가입 일자", "OCB앱 최종 로그인 일자", "OCB앱 가입 여부"};
				String header3[] = {"발송일자", "이메일 제목", "이메일 발송 결과"};
				String header4[] = {"발송일자", "푸시 제목", "푸시 결과"};
				String header5[] = {"OCB닷컴 United ID", "OCB닷컴 로그인ID", "OCB닷컴 가입 일자", "OCB닷컴 최종 로그인 일자"};

				AutoMap hMap1 = new AutoMap();
				AutoMap hMap2 = new AutoMap();
				AutoMap hMap3 = new AutoMap();
				AutoMap hMap4 = new AutoMap();
				AutoMap hMap5 = new AutoMap();

				int i = 0;
				for (i = 0; i < header1.length; i++) {
					hMap1.put(Integer.toString(i), header1[i]);
				}
				for (i = 0; i < header2.length; i++) {
					hMap2.put(Integer.toString(i), header2[i]);
				}
				for (i = 0; i < header3.length; i++) {
					hMap3.put(Integer.toString(i), header3[i]);
				}
				for (i = 0; i < header4.length; i++) {
					hMap4.put(Integer.toString(i), header4[i]);
				}
				for (i = 0; i < header5.length; i++) {
					hMap5.put(Integer.toString(i), header5[i]);
				}

				List<AutoMap> list = new ArrayList<>();

				list.add(hMap1);
				list.addAll(list1);
				list.add(dummyMap);
				list.add(hMap2);
				list.addAll(list2);
				list.add(dummyMap);
				list.add(hMap3);
				list.addAll(list3);
				list.add(dummyMap);
				list.add(hMap4);
				list.addAll(list4);
				list.add(dummyMap);
				list.add(hMap5);
				list.addAll(list5);

				StringBuilder filename = new StringBuilder("P140802BKhub_").append(ptsUsername).append('_')
						.append(Helper.nowDateTimeString()).append('_');
				if (!StringUtils.isEmpty(ptsPrefix)) {
					filename.append(ptsPrefix).append('-');
				}

				filename.append(username).append('-').append(Helper.nowDateTimeString()).append(".xls");

				Path filePath = Paths.get(Constant.APP_FILE_DIR, filename.toString());
				excelService.create(filePath, "고객정보 상세조회", list);

				ptsService.send(filePath.toFile().getAbsolutePath(), ptsUsername);

				break;
			}
			case "PAN0105":{
				/**
				 * step1. load request count.
				 * step2. under 5 or not?
				 * step3. if under 5, call sendForSingleRequest
				 * step4. if over 5, send message 'Your request count is over limit..!!!, just wait....'
				 */
				int currentReqCall = singleReqRepository.selectSingleReqProcessingCnt(username);
				log.info("Current Request Call={}", currentReqCall);
				if( currentReqCall < Constant.MAX_SINGLE_REQUEST_CALL) {
					transmissionService.sendForSingleRequest(username, Helper.currentUser().getEmailAddr(), ptsUsername, ptsPrefix, params);
				} else {
					return ApiResponse.builder().message("최대 "+ Constant.MAX_SINGLE_REQUEST_CALL + "건 까지 동시 요청 가능합니다.").build();
				}

				break;
			}
			default:
				MenuProgress progress = uploadService.getFinishedMenuProgress(menuId, username);
				transmissionService.sendToPts(ptsUsername, ptsMasking, ptsPrefix, progress);
				break;
		}
		if(!menuId.equals("PAN0105")){
			return ApiResponse.builder().message("PTS 전송 성공").build();
		}else{
			return ApiResponse.builder().message("PTS전송되었습니다. 메일로 안내됩니다.").build();
		}
	}

	@PostMapping("/extractMemberInfo")
	public ApiResponse extractMemberInfo(@RequestParam String menuId, @RequestParam String inputDataType,
			@RequestParam String extractionCond, @RequestParam String periodType, @RequestParam String periodFrom, @RequestParam String periodTo,
			@RequestParam boolean ptsMasking, @RequestParam(defaultValue = "") String ptsPrefix,
			@RequestParam(defaultValue = "N") String singleReq) {

		UserInfo user = Helper.currentUser();
		String username = user.getUsername();
		String ptsUsername = user.getPtsUsername();
		String emailAddr = user.getEmailAddr();

		System.out.println(extractionCond);
		
		// 임시로 하드코딩 수정 menuId 새로 생성
		MenuProgress progress = uploadService.getFinishedMenuProgressTmp(menuId, username);
		uploadService.getFinishedMenuProgressTmp("PAN0005", username);
		// 임시로 하드코딩 수정 menuId 새로 생성
		uploadService.markStatus(ProgressStatus.PROCESSING, "PAN0005", username, null, null);

		transmissionService.sendForExtraction(username, inputDataType, periodType, periodFrom, periodTo, ptsUsername,
				ptsMasking, ptsPrefix, emailAddr, progress, extractionCond, singleReq);

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
			params.put("transmissionType", "OCBCOM");
			transmissionService.sendToFtpForExtinction(params, TransmissionType.OCBCOM);
			params.put("transmissionType", "EM");
			transmissionService.sendToFtpForExtinction(params, TransmissionType.EM);
			params.put("transmissionType", "SMS");
			transmissionService.sendToSms(params);
			break;
		default:
			throw new BizException("전송 대상이 지정되지 않았습니다");
		}

		return ApiResponse.builder().message("전송 완료").build();
	}

}
