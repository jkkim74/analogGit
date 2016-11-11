package com.skplanet.pandora.service;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.csv.CSVPrinter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.skplanet.pandora.model.TransmissionType;
import com.skplanet.pandora.repository.oracle.OracleRepository;
import com.skplanet.web.model.AutoMap;
import com.skplanet.web.model.UploadProgress;
import com.skplanet.web.model.ProgressStatus;
import com.skplanet.web.service.FtpService;
import com.skplanet.web.service.MailService;
import com.skplanet.web.service.PtsService;
import com.skplanet.web.service.SmsService;
import com.skplanet.web.service.SshService;
import com.skplanet.web.service.UploadService;
import com.skplanet.web.util.Constant;
import com.skplanet.web.util.CsvCreatorTemplate;
import com.skplanet.web.util.Helper;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class TransmissionService {

	@Autowired
	private OracleRepository oracleRepository;

	@Autowired
	private PtsService ptsService;

	@Autowired
	private SmsService smsService;

	@Autowired
	private SshService sshService;

	@Autowired
	private MailService mailService;

	@Autowired
	private UploadService uploadService;

	@Autowired
	private FtpService ftpService;

	@Value("${ftp.extraction.host}")
	private String extractionHost;

	@Value("${ftp.extraction.port}")
	private int extractionPort;

	@Value("${ftp.extraction.username}")
	private String extractionUsername;

	@Value("${ftp.extraction.password}")
	private String extractionPassword;

	@Value("${ftp.extinction.host}")
	private String extinctionHost;

	@Value("${ftp.extinction.port}")
	private int extinctionPort;

	@Value("${ftp.extinction.username}")
	private String extinctionUsername;

	@Value("${ftp.extinction.password}")
	private String extinctionPassword;

	@Value("${app.files.encoding.pts}")
	private String encodingForPts;

	@Value("${app.files.encoding.ftp}")
	private String encodingForFtp;

	public String sendToPts(String ptsUsername, final boolean ptsMasking, final UploadProgress uploadProgress) {

		CsvCreatorTemplate<AutoMap> csvCreator = new CsvCreatorTemplate<AutoMap>(10000) {

			@Override
			protected List<AutoMap> nextList(int offset, int limit) {
				List<AutoMap> list = oracleRepository.selectMembers(uploadProgress, offset, limit, ptsMasking);
				return list;
			}

			@Override
			protected void printHeader(CSVPrinter printer, List<AutoMap> list) throws IOException {

				if ("PAN0101".equals(uploadProgress.getPageId())) {
					printer.printRecord("회원ID", "OCB닷컴 로그인ID", "CI번호", "한글성명", "카드번호", "시럽스마트월렛회원ID", "11번가회원ID", "NO");
				} else if ("PAN0103".equals(uploadProgress.getPageId())) {
					printer.printRecord("회원ID", "카드번호", "CI번호", "한글성명", "생년월일", "성별", "OCB카드여부", "CI일치여부", "성명일치여부",
							"생년월일일치여부", "성별일치여부", "불일치항목포함여부", "NO");
				} else if ("PAN0105".equals(uploadProgress.getPageId())) {
					if ("TR_MBR_KOR_NM".equals(uploadProgress.getColumnName())) {
						printer.printRecord("접수일시", "승인일시", "대표 승인번호", "승인번호", "매출일시", "회원ID", "카드코드", "카드코드명", "카드번호",
								"정산제휴사코드", "정산제휴사명", "정산가맹점코드", "정산가맹점명", "발생제휴사코드", "발생제휴사명", "발생가맹점코드", "발생가맹점명",
								"포인트종류코드", "포인트종류명", "전표코드", "전표명", "매출금액", "포인트", "제휴사연회비", "수수료", "지불수단코드", "지불수단명",
								"기관코드", "기관명", "유종코드", "유종명", "쿠폰코드", "쿠폰명", "회원한글명");
					} else {
						printer.printRecord("접수일시", "승인일시", "대표 승인번호", "승인번호", "매출일시", "회원ID", "카드코드", "카드코드명", "카드번호",
								"정산제휴사코드", "정산제휴사명", "정산가맹점코드", "정산가맹점명", "발생제휴사코드", "발생제휴사명", "발생가맹점코드", "발생가맹점명",
								"포인트종류코드", "포인트종류명", "전표코드", "전표명", "매출금액", "포인트", "제휴사연회비", "수수료", "지불수단코드", "지불수단명",
								"기관코드", "기관명", "유종코드", "유종명", "쿠폰코드", "쿠폰명");
					}
				} else if ("PAN0106".equals(uploadProgress.getPageId())) {
					printer.printRecord("회원ID", "OCB닷컴 United ID", "성별", "연령", "자택 행정동 대그룹명", "자택 행정동 중그룹명",
							"직장 행정동 대그룹명", "직장 행정동 중그룹명", "결혼 여부", "마케팅 동의 여부", "이메일 수신 동의 여부", "광고성 SMS 수신 동의 여부",
							"정보성 SMS 수신 동의 여부", "앱 푸시 수신 동의 여부", "혜택/모바일전단 푸시 동의 여부", "포인트사용적립 푸시 동의 여부",
							"친구와 함께쓰기 푸시 동의 여부", "코인알림 푸시 동의 여부", "위치활용 동의 여부", "BLE 동의 여부", "Geo-Fencing 동의 여부",
							"OCB닷컴 가입 여부", "OCB앱 가입 여부", "OCB플러스 가입 여부", "629세그먼트코드", "트래픽세그먼트코드", "OCB닷컴 최종 로그인 일자",
							"OCB앱 최종 사용 일자", "앱출석 최종 사용 일자", "룰렛 최종 사용 일자", "게임 최종 사용 일자", "OCB플러스 최종 유실적 일자",
							"OCB락 최종 유실적 일자", "미리줌 최종 유실적 일자", "더줌 최종 유실적 일자", "상품전단 최종 유실적 일자", "모바일카드 보유 여부",
							"SKP카드 보유 여부", "신용카드 보유 여부", "체크카드 보유 여부", "통신카드 보유 여부", "엔크린보너스카드 보유 여부", "화물복지카드 보유 여부",
							"주유전용카드 보유 여부", "주유 최종 유실적 일자", "통신 최종 유실적 일자", "금융 최종 유실적 일자", "기타 최종 유실적 일자",
							"온라인 최종 유실적 일자", "온라인쿠폰 최종 유실적 일자", "가용포인트", "NO");
				} else {
					Set<?> keyList = list.get(0).keySet();
					printer.printRecord(keyList);
				}
			}

			@Override
			protected void printRecord(CSVPrinter printer, AutoMap map) throws IOException {
				String pageId = uploadProgress.getPageId();
				switch (pageId) {
				case "PAN0101":
				case "PAN0103":
				case "PAN0106":
					Object rnum = map.get("rnum");
					map.remove("rnum");
					map.put("rnum", rnum); // 맨 뒤로 보내기
					printer.printRecord(map.values());
					break;
				default:
					map.remove("rnum");
					printer.printRecord(map.values());

				}
			}

		};

		Path filePath = Paths.get(Constant.APP_FILE_DIR, Helper.uniqueCsvFilename("P140802BKhub_" + ptsUsername));
		csvCreator.create(filePath, Charset.forName(encodingForPts));

		ptsService.send(filePath.toFile().getAbsolutePath(), ptsUsername);

		return filePath.getFileName().toString();
	}

	@Async
	public void sendForExtraction(String username, String inputDataType, String periodType, String periodFrom,
			String periodTo, String ptsUsername, boolean ptsMasking, String emailAddr, UploadProgress uploadProgress) {
		try {
			String filename = uploadProgress.getFilename();
			Path localPath = Paths.get(Constant.APP_FILE_DIR, filename);
			String remotePath = "web/" + filename;

			log.info("remotePath={}", remotePath);

			ftpService.send(localPath, remotePath, extractionHost, extractionPort, extractionUsername,
					extractionPassword);

			int extractionTarget = "MBR_ID".equals(uploadProgress.getColumnName()) ? 2 : 1;

			sshService.execute(username, inputDataType, periodType, periodFrom, periodTo, filename, extractionTarget);

			String sentFilename = sendToPts(ptsUsername, ptsMasking, uploadProgress);

			HashMap<String, Object> map = new HashMap<>();
			map.put("filename", sentFilename.substring(sentFilename.lastIndexOf('_') + 1));
			mailService.sendAsTo("pan0105.vm", map, "거래 실적 및 유실적 고객 추출 완료 안내", emailAddr);
		} finally {
			uploadService.markStatus(ProgressStatus.FINISHED, uploadProgress.getPageId(), username, null, null);
		}
	}

	public void sendToFtpForExtinction(final Map<String, Object> params, final TransmissionType transmissionType) {

		CsvCreatorTemplate<AutoMap> csvCreator = new CsvCreatorTemplate<AutoMap>(100000) {

			@Override
			public List<AutoMap> nextList(int offset, int limit) {
				params.put("offset", offset);
				params.put("limit", limit);

				return oracleRepository.selectExtinctionTargets(params);
			}

			@Override
			public void printRecord(CSVPrinter printer, AutoMap map) throws IOException {
				String extnctObjDt = (String) map.get("extnctObjDt");

				if (transmissionType == TransmissionType.OCBCOM) {
					// 소멸예정년,소멸예정월,소멸예정일,EC_USER_ID
					printer.printRecord(extnctObjDt.substring(0, 4), extnctObjDt.substring(4, 6),
							extnctObjDt.substring(6, 8), map.get("unitedId"));
				} else if (transmissionType == TransmissionType.EM) {
					String mbrId = (String) map.get("mbrId");
					String unitedId = (String) map.get("unitedId");
					String encrypted = Helper.skpEncrypt(mbrId + "," + unitedId);

					// 소멸예정년,소멸예정월,소멸예정일,고객성명,이메일주소,암호화값
					printer.printRecord(extnctObjDt.substring(0, 4), extnctObjDt.substring(4, 6),
							extnctObjDt.substring(6, 8), map.get("mbrKorNm"), map.get("emailAddr"), encrypted);
				}
			}

		};

		Path filePath = Paths.get(Constant.APP_FILE_DIR,
				Helper.uniqueCsvFilename(transmissionType.name().toLowerCase()));

		csvCreator.create(filePath, Charset.forName(encodingForFtp));

		String remotePath = "";
		if (transmissionType == TransmissionType.OCBCOM) {
			remotePath = "pointExEmail/extinction_" + Helper.nowDateString() + ".txt";
		} else if (transmissionType == TransmissionType.EM) {
			remotePath = "pointExEmail/extinction_em_" + Helper.nowDateString() + ".txt";
		}

		log.info("remotePath={}", remotePath);

		// ftpService.send(localPath, remotePath, extinctionHost,
		// extinctionPort, extinctionUsername,
		// extinctionPassword);
	}

	public void sendToSms(final Map<String, Object> params) {
		params.put("noPaging", true);
		List<AutoMap> list = oracleRepository.selectExtinctionTargets(params);

		smsService.send(list);
	}

}
