package com.skplanet.pandora.service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Date;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.skplanet.ocb.exception.BizException;
import com.skplanet.ocb.util.Helper;
import com.skplanet.pandora.util.Constant;

@Service
public class IdmsService {

	private static final String IDMS_BIZ_SITE_ID = "PANDORA";

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
	private FtpService ftpService;

	private static void writeToFileAsCsv(String filename, Object... values) {
		Path filePath = Paths.get(Constant.UPLOADED_FILE_DIR, filename);

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
	// @Async
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

	// 0시 ~ 0시 30분경 FTP 전송
	// @Scheduled
	public void send() {
		// 로그 작성 시간 정보 수집
		Date begin = new Date();
		String jobInfoFilename = logForJobInfo(Helper.toDatetimeString(begin), null);
		Path jobInfoPath = Paths.get(Constant.UPLOADED_FILE_DIR, jobInfoFilename);
		ftpService.sendForLogging(jobInfoPath, null);

		// 고객정보조회 로그 수집
		String searchMemberInfoFilename = IDMS_BIZ_SITE_ID + "_CUS_" + Helper.yesterdayDateString() + ".log";
		Path searchMemberInfoPath = Paths.get(Constant.UPLOADED_FILE_DIR, searchMemberInfoFilename);
		ftpService.sendForLogging(searchMemberInfoPath, null);

		// 사용자 계정 정보 수집
		// create user info log
		String userInfoFilename = IDMS_BIZ_SITE_ID + "_ID_" + Helper.yesterdayDateString() + ".log";
		Path userInfoPath = Paths.get(Constant.UPLOADED_FILE_DIR, userInfoFilename);
		ftpService.sendForLogging(userInfoPath, null);

		// 로그인/로그아웃 로그 수집
		String logInOutFilename = IDMS_BIZ_SITE_ID + "_LOGIN_" + Helper.yesterdayDateString() + ".log";
		Path logInOutPath = Paths.get(Constant.UPLOADED_FILE_DIR, logInOutFilename);
		ftpService.sendForLogging(logInOutPath, null);

		// 로그 작성 시간 정보 수집 종료시간 포함하여 덮어쓰기
		logForJobInfo(Helper.toDatetimeString(begin), Helper.toDatetimeString(new Date()));
		ftpService.sendForLogging(jobInfoPath, null);
	}

}
