package com.skplanet.web.service;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.apache.commons.csv.CSVPrinter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.google.common.collect.Maps;
import com.skplanet.web.repository.mysql.IdmsRepository;
import com.skplanet.web.security.UserInfo;
import com.skplanet.web.util.Constant;
import com.skplanet.web.util.CsvCreatorTemplate;
import com.skplanet.web.util.Helper;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class IdmsService {

	/**
	 * Biz 사용자ID 발급 시 IDMS를 통하는 계정(1,2)과 Biz 사용자가 직접 발급하는 계정을 구분하기 위한 항목. 1:관리자,
	 * 2:제휴사 관리자, 3:임의발급계정
	 */
	public static final String USER_ID_TYPE_ADMIN = "1";

	/**
	 * Biz 사용자 ID의 계정 운용 상태 정보. 1:정상, 2:사용정지/잠금, 3:해지
	 */
	public static final String USER_STATUS_ENABLED = "1";
	public static final String USER_STATUS_DIASABLED = "2";

	@Autowired
	private IdmsRepository idmsRepository;

	@Autowired
	private FtpService ftpService;

	@Autowired
	private UserService userService;

	@Value("${app.enable.idms}")
	private boolean enabled;

	@Value("${ftp.idms.host}")
	private String idmsHost;

	@Value("${ftp.idms.port}")
	private int idmsPort;

	@Value("${ftp.idms.username}")
	private String idmsUsername;

	@Value("${ftp.idms.password}")
	private String idmsPassword;

	@Value("${app.idms.bizSiteId}")
	private String bizSiteId;

	/**
	 * 기능 코드 - 화면에서 발생한 기능 코드 (IDMS 연동_정보요청양식.xlsx의 3.sample시트 참조)
	 */
	@Value("${app.idms.funcCd}")
	private String funcCd;

	@Scheduled(cron = "0 0 1 * * ?")
	public void send() {
		if (!enabled) {
			log.debug("disabled");
			return;
		}

		// 로그 작성 시간 정보 수집
		String beginDttm = Helper.nowDateTimeString();
		Path jobInfoPath = createJobInfoFile(beginDttm);
		ftpService.send(jobInfoPath, "/" + jobInfoPath.getFileName(), idmsHost, idmsPort, idmsUsername, idmsPassword);

		// 고객정보조회 로그 수집
		Path memberSearchLogPath = createMemberSearchLogFile();
		ftpService.send(memberSearchLogPath, "/" + memberSearchLogPath.getFileName(), idmsHost, idmsPort, idmsUsername,
				idmsPassword);

		// 사용자 계정 정보 수집
		Path userInfoPath = createUserInfoFile();
		ftpService.send(userInfoPath, "/" + userInfoPath.getFileName(), idmsHost, idmsPort, idmsUsername, idmsPassword);

		// 로그인/아웃 로그 수집
		Path accessLogPath = createAccessLogFile();
		ftpService.send(accessLogPath, "/" + accessLogPath.getFileName(), idmsHost, idmsPort, idmsUsername,
				idmsPassword);

		// 로그 작성 시간 정보 수집 종료시간 포함하여 덮어쓰기
		String endDttm = Helper.nowDateTimeString();
		jobInfoPath = createJobInfoFile(beginDttm, endDttm);
		ftpService.send(jobInfoPath, "/" + jobInfoPath.getFileName(), idmsHost, idmsPort, idmsUsername, idmsPassword);
	}

	@Async
	public void login(String username, String userIp, String loginDttm) {
		idmsRepository.insertLogin(username, userIp, loginDttm);
	}

	@Async
	public void logout(String username, String userIp, String logoutDttm) {
		idmsRepository.updateLogout(username, userIp, logoutDttm);
	}

	/**
	 * 
	 * @param selDttm
	 *            발생일시 - 년, 월, 일, 시, 분, 초를 붙여서 14자리로 기록. 예)2010-05-11 10:05:35
	 *            -> 20100511100535
	 * @param username
	 *            Biz 사용자 ID - Biz 사이트에 로그인한 사용자(관리자, 상담원 등)로그인ID
	 * @param userIp
	 *            Biz 사용자 IP - 로그인 사용자(관리자, 상담원 등)의 Client IP(사용자PC IP)
	 * @param mbrId
	 *            고객 ID - Biz 사용자가 조회한 고객의 식별자 (ID나 해당 Biz 사이트에서 관리되고 있는 번호).
	 *            Null은 고객 리스트를 보여주는 경우와 고객정보와 무관한 업무처리를 하는 경우에만 허용
	 * @param mbrKorNm
	 *            고객 명 - Biz 사용자가 조회한 고객 명
	 * @param pageId
	 *            Biz 사이트 화면 ID -사용자가 조회 또는 처리한 화면 ID (IDMS 연동_정보요청양식.xlsx의
	 *            3.화면정보 시트 참조)
	 * @param mbrCnt
	 *            고객 리스트 건수 - 고객을 출력(화면,인쇄,다운로드)하는 경우 대상 고객 수를 기록. 예) 고객정보조회가
	 *            아닌경우 0, 한명을 조회하는 경우 1, 다수를 조회하는 경우 고객수로 기록
	 */
	@Async
	public void memberSearch(String selDttm, String username, String userIp, String mbrId, String mbrKorNm,
			String pageId, int mbrCnt) {
		/*
		 * Biz 사이트 IP - Biz 사이트 서버 IP(WAS IP)
		 */
		String wasIp = Helper.serverIp();
		idmsRepository.insertMemberSearch(selDttm, wasIp, username, userIp, mbrId, mbrKorNm, pageId, funcCd, mbrCnt);
	}

	private Path createJobInfoFile(final String... dttm) {
		Path jobInfoPath = Paths.get(Constant.APP_FILE_DIR,
				bizSiteId + "_JOB_" + Helper.yesterdayDateString() + ".log");

		new CsvCreatorTemplate<String>() {
			boolean done;

			protected List<String> nextList() {
				if (done) {
					return Collections.emptyList();
				}
				done = true;
				return Arrays.asList(dttm);
			}

			protected void printRecord(CSVPrinter printer, String t) throws IOException {
				printer.print(t);
			}
		}.create(jobInfoPath);

		return jobInfoPath;
	}

	private Path createMemberSearchLogFile() {
		Path memberSearchLogPath = Paths.get(Constant.APP_FILE_DIR,
				bizSiteId + "_CUS_" + Helper.yesterdayDateString() + ".log");

		new CsvCreatorTemplate<Map<String, Object>>() {
			boolean done;

			protected List<Map<String, Object>> nextList() {
				if (done) {
					return Collections.emptyList();
				}
				done = true;
				return idmsRepository.selectMemberSearchLogAtYesterday();
			}

			protected void printRecord(CSVPrinter printer, Map<String, Object> t) throws IOException {
				printer.printRecord(t.values());
			}
		}.create(memberSearchLogPath);

		return memberSearchLogPath;
	}

	private Path createUserInfoFile() {
		Path userInfoPath = Paths.get(Constant.APP_FILE_DIR,
				bizSiteId + "_ID_" + Helper.yesterdayDateString() + ".log");

		new CsvCreatorTemplate<UserInfo>() {
			boolean done;

			protected List<UserInfo> nextList() {
				if (done) {
					return Collections.emptyList();
				}
				done = true;
				return userService.getUsers(Maps.<String, Object> newHashMap());
			}

			protected void printRecord(CSVPrinter printer, UserInfo t) throws IOException {
				printer.printRecord(t.getUsername(), t.getFullname(), null,
						(t.isEnabled() ? USER_STATUS_ENABLED : USER_STATUS_DIASABLED), USER_ID_TYPE_ADMIN,
						t.getCreateDttm(), (t.getBeginDttm() != null ? t.getBeginDttm().substring(0, 8) : null),
						(t.getEndDttm() != null ? t.getEndDttm().substring(0, 8) : null));
			}
		}.create(userInfoPath);

		return userInfoPath;
	}

	private Path createAccessLogFile() {
		Path accessLogPath = Paths.get(Constant.APP_FILE_DIR,
				bizSiteId + "_LOGIN_" + Helper.yesterdayDateString() + ".log");

		new CsvCreatorTemplate<Map<String, Object>>() {
			boolean done;

			protected List<Map<String, Object>> nextList() {
				if (done) {
					return Collections.emptyList();
				}
				done = true;
				return idmsRepository.selectAccessLogAtYesterday();
			}

			protected void printRecord(CSVPrinter printer, Map<String, Object> t) throws IOException {
				printer.printRecord(t.values());
			}
		}.create(accessLogPath);

		return accessLogPath;
	}

}
