package com.skplanet.pandora.service;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

import org.apache.commons.csv.CSVPrinter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.skplanet.ocb.service.SmsService;
import com.skplanet.ocb.util.AutoMappedMap;
import com.skplanet.ocb.util.Constant;
import com.skplanet.ocb.util.CsvCreatorTemplate;
import com.skplanet.ocb.util.Helper;
import com.skplanet.pandora.model.TransmissionType;
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
	private ForwardService forwardService;

	public void noticeUsingFtp(final Map<String, Object> params, final TransmissionType transmissionType) {

		log.info("notice using ftp: {}, {}", params, transmissionType);

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
				String extnctObjDt = String.valueOf(map.get("extnctObjDt"));

				if (transmissionType == TransmissionType.OCBCOM) {
					// 소명예정년,소멸예정월,소멸예정일,EC_USER_ID
					printer.printRecord(extnctObjDt.substring(0, 4), extnctObjDt.substring(4, 6),
							extnctObjDt.substring(6, 8), map.get("unitedId"));
				} else if (transmissionType == TransmissionType.EM) {
					String mbrId = String.valueOf(map.get("mbrId"));
					String unitedId = String.valueOf(map.get("unitedId"));
					String encrypted = Helper.skpEncrypt(mbrId + "," + unitedId);

					// 소명예정년,소멸예정월,소멸예정일,고객성명,이메일주소,암호화값
					printer.printRecord(extnctObjDt.substring(0, 4), extnctObjDt.substring(4, 6),
							extnctObjDt.substring(6, 8), map.get("mbrKorNm"), map.get("emailAddr"), encrypted);
				}
			}

		};

		Path filePath = Paths.get(Constant.APP_FILE_DIR,
				Helper.uniqueCsvFilename(transmissionType.name().toLowerCase()));

		csvCreator.create(filePath, Charset.forName("x-IBM949"));

		forwardService.sendForNotification(filePath, transmissionType);
	}

	public void noticeUsingSms(final Map<String, Object> params) {
		params.put("noPaging", true);
		List<AutoMappedMap> list = oracleRepository.selectExtinctionTargets(params);

		smsService.send(list);
	}

}
