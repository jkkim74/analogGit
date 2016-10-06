package com.skplanet.pandora.service;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
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

	public void send(String ptsUsername, boolean ptsMasking, UploadProgress uploadProgress) {
		String csvFile = createCsvFile(ptsUsername, ptsMasking, uploadProgress);
		
		process(csvFile,ptsUsername);
	}

	private String createCsvFile(String ptsUsername, final boolean ptsMasking, final UploadProgress uploadProgress) {

		CsvCreatorTemplate<AutoMappedMap> csvCreator = new CsvCreatorTemplate<AutoMappedMap>() {

			int offset = 0;
			int limit = 10000;

			@Override
			protected List<AutoMappedMap> nextList() {
				List<AutoMappedMap> list = oracleRepository.selectMembers(uploadProgress, offset, limit, ptsMasking);
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

	private void process(String filename, String ptsUsername) {
		log.info("Sending file [{}]", filename);
		

		
		String ptsProperties = Resources.getResource("config/PTS.properties").getPath();

		log.debug("PTS.properties location={}", ptsProperties);

		String theFilename = filename;
		
		
		
		BufferedReader in = null;
		FileWriter fw = null;
		BufferedWriter bw = null;

		try {
			in = new BufferedReader(new FileReader(ptsProperties));
			fw = new FileWriter(ptsProperties + "." + ptsUsername);
			bw = new BufferedWriter(fw);

			String line = null;
			int cnt = 0;
			while ((line = in.readLine()) != null) {
				if ( cnt == 0 )
					bw.write(line + filename.substring(0,filename.lastIndexOf("/")));
				else
					bw.write(line);

				bw.newLine();
				cnt++;
			}

			in.close();
			bw.close();
			fw.close();

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (in != null)
					in.close();
				if (bw != null)
					bw.close();
				if (fw != null)
					fw.close();
			} catch (IOException e) {
				e.printStackTrace();
			}

		}		
		
		log.info("Sending file [{}]", filename.substring(filename.lastIndexOf("/")+1,filename.length()));
		log.info("Sending pts dir [{}]", ptsProperties + "." + ptsUsername);

		String[] command = { filename.substring(filename.lastIndexOf("/")+1,filename.length()), ptsProperties + "." + ptsUsername };

		try {
			ApiClient.main(command);
		} catch (IOException e) {
			throw new BizException("PTS 전송 실패", e);
		}
	}

}
