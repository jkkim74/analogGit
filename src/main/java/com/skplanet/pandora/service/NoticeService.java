package com.skplanet.pandora.service;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;

import org.apache.commons.csv.CSVPrinter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.skplanet.pandora.common.BizException;
import com.skplanet.pandora.common.CsvCreatorTemplate;
import com.skplanet.pandora.common.Helper;
import com.skplanet.pandora.model.AutoMappedMap;
import com.skplanet.pandora.repository.oracle.OracleRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class NoticeService {

	@Autowired
	private OracleRepository oracleRepository;

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

	public void noticeUsingFtp(final Map<String, Object> params, final String notiTarget) {

		log.info("notice using ftp: {}, {}", params, notiTarget);

		String remotePath = "";
		if ("ocbcom".equals(notiTarget)) {
			remotePath = "pointExEmail/extinction_" + Helper.nowDateString() + ".txt";
		} else if ("em".equals(notiTarget)) {
			remotePath = "pointExEmail/extinction_em_" + Helper.nowDateString() + ".txt";
		}

		log.info("remotePath={}", remotePath);

		CsvCreatorTemplate<AutoMappedMap> csvCreator = new CsvCreatorTemplate<AutoMappedMap>() {

			int offset = 0;
			int limit = 10000;

			@Override
			public List<AutoMappedMap> nextList() {
				params.put("offset", offset);
				params.put("limit", limit);
				offset += limit;

				return oracleRepository.selectNoticeResults(params);
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

	public void noticeUsingSms(final Map<String, Object> params) {
		params.put("noPaging", true);
		List<AutoMappedMap> list = oracleRepository.selectNoticeResults(params);

		smsService.send(list);
	}

}