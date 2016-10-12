package com.skplanet.pandora.service;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;

import org.apache.commons.csv.CSVPrinter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.skplanet.ocb.util.AutoMappedMap;
import com.skplanet.ocb.util.Helper;
import com.skplanet.pandora.model.TransmissionType;
import com.skplanet.pandora.repository.oracle.OracleRepository;
import com.skplanet.pandora.util.CsvCreatorTemplate;

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

	public void noticeUsingFtp(final Map<String, Object> params, final TransmissionType notiType) {

		log.info("notice using ftp: {}, {}", params, notiType);

		String remotePath = "";
		if (notiType == TransmissionType.OCBCOM) {
			remotePath = "pointExEmail/extinction_" + Helper.nowDateString() + ".txt";
		} else if (notiType == TransmissionType.EM) {
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

				return oracleRepository.selectExtinctionTargets(params);
			}

			@Override
			public void printRecord(CSVPrinter printer, AutoMappedMap map) throws IOException {
				String extnctObjDt = (String) map.get("extnctObjDt");

				if (notiType == TransmissionType.OCBCOM) {
					// 소명예정년,소멸예정월,소멸예정일,EC_USER_ID
					printer.printRecord(extnctObjDt.substring(0, 4), extnctObjDt.substring(4, 6),
							extnctObjDt.substring(6, 8), map.get("unitedId"));
				} else if (notiType == TransmissionType.EM) {
					String mbrId = (String) map.get("mbrId");
					String unitedId = (String) map.get("unitedId");
					String encrypted = Helper.skpEncrypt(mbrId + "," + unitedId);

					// 소명예정년,소멸예정월,소멸예정일,고객성명,이메일주소,암호화값
					printer.printRecord(extnctObjDt.substring(0, 4), extnctObjDt.substring(4, 6),
							extnctObjDt.substring(6, 8), map.get("mbrKorNm"), map.get("emailAddr"), encrypted);
				}
			}

		};

		Path filePath = csvCreator.create(Helper.uniqueCsvFilename(notiType.name().toLowerCase()));

		ftpService.sendForNotification(filePath, remotePath);
	}

	public void noticeUsingSms(final Map<String, Object> params) {
		params.put("noPaging", true);
		List<AutoMappedMap> list = oracleRepository.selectExtinctionTargets(params);

		smsService.send(list);
	}

}
