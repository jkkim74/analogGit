package com.skplanet.ocb.service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.skplanet.ocb.exception.BizException;
import com.skplanet.ocb.model.AutoMappedMap;
import com.skplanet.ocb.repository.mysql.IdmsRepository;
import com.skplanet.ocb.util.Constant;
import com.skplanet.ocb.util.CsvCreatorTemplate;
import com.skplanet.ocb.util.Helper;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class IdmsService {

	private static final String IDMS_BIZ_SITE_ID = "ctas";

	/*
	 * Biz 사용자ID 발급 시 IDMS를 통하는 계정(1,2)과 Biz 사용자가 직접 발급하는 계정을 구분하기 위한 항목. 1:관리자,
	 * 2:제휴사 관리자, 3:임의발급계정
	 */
	public static final int BIZ_USER_ID_TYPE = 1;

	/*
	 * Biz 사용자 ID의 계정 운용 상태 정보. 1:정상, 2:사용정지/잠금, 3:해지
	 */
	public static final int BIZ_USER_STATUS_ENABLED = 1;
	public static final int BIZ_USER_STATUS_DIASABLED = 2;

	@Autowired
	private IdmsRepository idmsRepository;

	@Autowired
	private FtpService ftpService;

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

	private static void writeToFileAsCsv(String filename, Object... values) {
		Path filePath = Paths.get(Constant.APP_FILE_DIR, filename);

		try (CSVPrinter printer = CSVFormat.DEFAULT.print(Files.newBufferedWriter(filePath, StandardCharsets.UTF_8,
				StandardOpenOption.CREATE, StandardOpenOption.APPEND))) {

			printer.printRecord(values);

		} catch (IOException e) {
			throw new BizException("로그 파일 쓰기 실패", e);
		}
	}

	/*
	 * 이벤트 발생 시마다 기록 (파일명이 오늘)
	 */
	public void logForSearchMemberInfo(String searchDt, String bizSiteIp, String bizUserId, String customerId,
			String customerName, String bizSitePageId, String funcCode, int searchCount) {

		String filename = IDMS_BIZ_SITE_ID + "_CUS_" + Helper.nowDateString() + ".log";

		writeToFileAsCsv(filename, searchDt, bizSiteIp, bizUserId, customerId, customerName, bizSitePageId, funcCode,
				searchCount);
	}

	/*
	 * 로그전송 시 기록 (파일명이 어제)
	 */
	public void logForUserInfo(String bizUserId, String bizUserName, String bizUserCompany, int bizUserIdStatus,
			String bizUserIdCreatedDttm, String bizUserIdUsageFromDt, String bizUserIdUsageToDt) {

		String filename = IDMS_BIZ_SITE_ID + "_ID_" + Helper.yesterdayDateString() + ".log";

		writeToFileAsCsv(filename, bizUserId, bizUserName, bizUserCompany, bizUserIdStatus, BIZ_USER_ID_TYPE,
				bizUserIdCreatedDttm, bizUserIdUsageFromDt, bizUserIdUsageToDt);
	}

	/*
	 * 이벤트 발생 시마다 기록 (파일명이 오늘)
	 */
	@Async
	public void logForLogInOut(String bizUserId, String bizUserIp, String loginDttm, String logoutDttm) {
		String filename = IDMS_BIZ_SITE_ID + "_LOGIN_" + Helper.nowDateString() + ".log";

		writeToFileAsCsv(filename, bizUserId, bizUserIp, loginDttm, logoutDttm);
	}

	/*
	 * 로그전송 시 기록 (파일명이 어제)
	 */
	public String logForJobInfo(String logBeginDttm, String logEndDttm) {
		String filename = IDMS_BIZ_SITE_ID + "_JOB_" + Helper.yesterdayDateString() + ".log";

		if (StringUtils.isEmpty(logEndDttm)) {
			writeToFileAsCsv(filename, logBeginDttm);
		} else {
			writeToFileAsCsv(filename, logBeginDttm, logEndDttm);
		}

		return filename;
	}

	@Scheduled(cron = "0 0 1 * * ?")
	public void send() {
		if (!enabled) {
			log.debug("disabled");
			return;
		}

		// 로그 작성 시간 정보 수집
		Date begin = new Date();
		String jobInfoFilename = logForJobInfo(Helper.toDatetimeString(begin), null);
		Path jobInfoPath = Paths.get(Constant.APP_FILE_DIR, jobInfoFilename);

		ftpService.send(jobInfoPath, "/" + jobInfoPath.getFileName(), idmsHost, idmsPort, idmsUsername, idmsPassword);

		// 고객정보조회 로그 수집
		Path searchMemberInfoPath = aggregateSearchMemberInfo();
		ftpService.send(searchMemberInfoPath, "/" + searchMemberInfoPath.getFileName(), idmsHost, idmsPort,
				idmsUsername, idmsPassword);

		// 사용자 계정 정보 수집
		Path userInfoPath = aggregateUserInfo();
		ftpService.send(userInfoPath, "/" + userInfoPath.getFileName(), idmsHost, idmsPort, idmsUsername, idmsPassword);

		// 로그인/아웃 로그 수집
		Path logInOutPath = aggregateLoginout();
		ftpService.send(logInOutPath, "/" + logInOutPath.getFileName(), idmsHost, idmsPort, idmsUsername, idmsPassword);

		// 로그 작성 시간 정보 수집 종료시간 포함하여 덮어쓰기
		logForJobInfo(Helper.toDatetimeString(begin), Helper.toDatetimeString(new Date()));

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

	private Path aggregateSearchMemberInfo() {
		String searchMemberInfoFilename = IDMS_BIZ_SITE_ID + "_CUS_" + Helper.yesterdayDateString() + ".log";
		Path searchMemberInfoPath = Paths.get(Constant.APP_FILE_DIR, searchMemberInfoFilename);
		return searchMemberInfoPath;
	}

	private Path aggregateUserInfo() {
		String userInfoFilename = IDMS_BIZ_SITE_ID + "_ID_" + Helper.yesterdayDateString() + ".log";
		Path userInfoPath = Paths.get(Constant.APP_FILE_DIR, userInfoFilename);
		return userInfoPath;
	}

	private Path aggregateLoginout() {
		Path logInOutPath = Paths.get(Constant.APP_FILE_DIR,
				IDMS_BIZ_SITE_ID + "_LOGIN_" + Helper.yesterdayDateString() + ".log");

		new CsvCreatorTemplate<AutoMappedMap>() {
			boolean done;

			protected List<AutoMappedMap> nextList() {
				List<AutoMappedMap> list = done ? Collections.<AutoMappedMap> emptyList()
						: idmsRepository.selectYesterdayLoginout();
				done = true;
				return list;
			}

			protected void printRecord(CSVPrinter printer, AutoMappedMap t) throws IOException {
				printer.printRecord(t);
			}
		}.create(logInOutPath);

		return logInOutPath;
	}

}
