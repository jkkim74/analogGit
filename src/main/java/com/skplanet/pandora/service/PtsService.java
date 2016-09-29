package com.skplanet.pandora.service;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

import org.apache.commons.csv.CSVPrinter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.io.Resources;
import com.ptsapi.client.ApiClient;
import com.skplanet.pandora.exception.BizException;
import com.skplanet.pandora.model.AutoMappedMap;
import com.skplanet.pandora.model.UploadProgress;
import com.skplanet.pandora.repository.oracle.OracleRepository;
import com.skplanet.pandora.util.CsvCreatorTemplate;
import com.skplanet.pandora.util.Helper;

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

	private String createCsvFile(String ptsUsername, final UploadProgress uploadProgress) {

		CsvCreatorTemplate<AutoMappedMap> csvCreator = new CsvCreatorTemplate<AutoMappedMap>() {

			int offset = 0;
			int limit = 10000;

			@Override
			protected List<AutoMappedMap> nextList() {
				List<AutoMappedMap> list = oracleRepository.selectMembers(uploadProgress, offset, limit);
				offset += limit;
				return list;
			}

			@Override
			protected void printHeader(CSVPrinter printer, List<AutoMappedMap> list) throws IOException {
				List<?> keyList = list.get(0).keyList();
				printer.printRecord(keyList);
			}

			@Override
			protected void printRecord(CSVPrinter printer, AutoMappedMap map) throws IOException {
				printer.printRecord(map.valueList());
			}

		};

		Path filePath = csvCreator.create(Helper.uniqueCsvFilename("P140802BKhub_" + ptsUsername));

		return filePath.toFile().getAbsolutePath();
	}

	private void process(String filename) {
		log.info("Sending file [{}]", filename);
		
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
