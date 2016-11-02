package com.skplanet.pandora.service;

import java.nio.file.Path;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.skplanet.web.model.TransmissionType;
import com.skplanet.web.service.FtpService;
import com.skplanet.web.util.Helper;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ForwardService {

	@Autowired
	private FtpService ftpService;

	@Value("${ftp.extraction.host}")
	private String extractionHost;

	@Value("${ftp.extraction.port}")
	private int extractionPort;

	@Value("${ftp.extraction.username}")
	private String extractionUsername;

	@Value("${ftp.extraction.password}")
	private String extractionPassword;

	@Value("${ftp.extinction.host}")
	private String extinctionHost;

	@Value("${ftp.extinction.port}")
	private int extinctionPort;

	@Value("${ftp.extinction.username}")
	private String extinctionUsername;

	@Value("${ftp.extinction.password}")
	private String extinctionPassword;

	public void sendForExtraction(Path localPath) {
		String remotePath = "web/" + localPath.getFileName();

		log.info("remotePath={}", remotePath);

		ftpService.send(localPath, remotePath, extractionHost, extractionPort, extractionUsername, extractionPassword);
	}

	public void sendForNotification(Path localPath, TransmissionType transmissionType) {
		String remotePath = "";
		if (transmissionType == TransmissionType.OCBCOM) {
			remotePath = "pointExEmail/extinction_" + Helper.nowDateString() + ".txt";
		} else if (transmissionType == TransmissionType.EM) {
			remotePath = "pointExEmail/extinction_em_" + Helper.nowDateString() + ".txt";
		}

		log.info("remotePath={}", remotePath);

		// ftpService.send(localPath, remotePath, notificationHost,
		// notificationPort, notificationUsername,
		// notificationPassword);
	}

}
