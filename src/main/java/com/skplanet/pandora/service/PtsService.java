package com.skplanet.pandora.service;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.datetime.DateFormatter;
import org.springframework.stereotype.Service;

import com.google.common.io.Resources;
import com.ptsapi.client.ApiClient;
import com.skplanet.pandora.common.BizException;
import com.skplanet.pandora.common.Constant;
import com.skplanet.pandora.model.AutoMappedMap;
import com.skplanet.pandora.model.UploadProgress;
import com.skplanet.pandora.repository.oracle.OracleRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class PtsService {

	@Autowired
	private OracleRepository oracleRepository;

	public void send(String ptsUsername, UploadProgress uploadProgress) {
		String csvFile = createCsvFile(ptsUsername, uploadProgress);

		process(csvFile);
	}

	private String createCsvFile(String ptsUsername, UploadProgress uploadProgress) {
		int offset = 0;
		int limit = 10000; // 1만건씩 DB에서 읽어서 CSV파일에 쓰기
		List<AutoMappedMap> list = Collections.emptyList();
		Path filePath = Paths.get(Constant.UPLOADED_FILE_DIR, getFilename(ptsUsername));

		try (BufferedWriter writer = Files.newBufferedWriter(filePath, StandardCharsets.UTF_8,
				StandardOpenOption.CREATE, StandardOpenOption.APPEND)) {

			CSVPrinter printer = null;

			do {
				list = oracleRepository.selectMembers(uploadProgress, offset, limit);

				// csv 첫줄에 헤더추가
				if (printer == null && !list.isEmpty()) {
					List<?> keyList = list.get(0).keyList();
					printer = CSVFormat.DEFAULT.withHeader(keyList.toArray(new String[0])).print(writer);
				}

				for (AutoMappedMap m : list) {
					printer.printRecord(m.valueList());
				}

				// 데이터가 없을 때까지 반복
				offset += limit;
			} while (!list.isEmpty());

		} catch (IOException e) {
			throw new BizException("CSV 파일 생성 실패", e);
		}

		return filePath.toFile().getAbsolutePath();
	}

	private String getFilename(String ptsUsername) {
		DateFormatter df = new DateFormatter("yyyyMMdd");
		String nowDt = df.print(new Date(), Locale.getDefault());
		return "P140802BKhub_" + ptsUsername + "_" + nowDt + "_" + UUID.randomUUID() + ".csv";
	}

	private void process(String filename) {
		String ptsProperties = Resources.getResource("config/PTS.properties").getPath();

		log.debug("PTS.properties location={}", ptsProperties);

		String theFilename = filename;

		// PTS API Client에서 '/'를 앞에 붙이므로 여기선 제거해준다.
		if (theFilename.startsWith("/")) {
			theFilename = theFilename.substring(1);
		}

		String[] command = { theFilename, ptsProperties };

		try {
			ApiClient.main(command);
		} catch (IOException e) {
			throw new BizException("PTS 전송 실패", e);
		}
	}

}
