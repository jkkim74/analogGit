package com.skplanet.ocb.service;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.google.common.io.Resources;
import com.ptsapi.client.ApiClient;
import com.skplanet.ocb.exception.BizException;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class PtsService {

	@Value("${app.pts.enabled}")
	private boolean enabled;

	public void send(String filename, String ptsUsername) {
		log.info("Sending file [{}]", filename);

		String ptsProperties = Resources.getResource("config/PTS.properties").getPath();

		log.debug("PTS.properties location={}", ptsProperties);

		if (!enabled) {
			log.debug("disabled");
			return;
		}

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
				if (cnt == 0)
					bw.write(line + filename.substring(0, filename.lastIndexOf("/")));
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

		log.info("Sending file [{}]", filename.substring(filename.lastIndexOf("/") + 1, filename.length()));
		log.info("Sending pts dir [{}]", ptsProperties + "." + ptsUsername);

		String[] command = { filename.substring(filename.lastIndexOf("/") + 1, filename.length()),
				ptsProperties + "." + ptsUsername };

		try {
			ApiClient.main(command);
		} catch (IOException e) {
			throw new BizException("PTS 전송 실패", e);
		}
	}

}
