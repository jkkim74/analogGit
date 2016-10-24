package com.skplanet.pandora.service;

import java.nio.file.Path;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.skplanet.ocb.model.TransmissionType;
import com.skplanet.ocb.service.FtpService;
import com.skplanet.ocb.service.UploadService;
import com.skplanet.ocb.util.Helper;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ForwardService {

	@Autowired
	private FtpService ftpService;
	
	@Autowired
	private UploadService uploadService;

	@Value("${pandora.ftp.extraction.host}")
	private String extractionHost;

	@Value("${pandora.ftp.extraction.port}")
	private int extractionPort;

	@Value("${pandora.ftp.extraction.username}")
	private String extractionUsername;

	@Value("${pandora.ftp.extraction.password}")
	private String extractionPassword;

	@Value("${pandora.ftp.notification.host}")
	private String notificationHost;

	@Value("${pandora.ftp.notification.port}")
	private int notificationPort;

	@Value("${pandora.ftp.notification.username}")
	private String notificationUsername;

	@Value("${pandora.ftp.notification.password}")
	private String notificationPassword;

	public void sendForExtraction(Path localPath) {
		String remotePath = "web/" + localPath.getFileName().toString();

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

	public void sendForLogging(Path localPath, String remotePath) {

	}

	public void forwardToFtpServer(MultipartFile file, String pageId, String username, String columnName) {
		Path filePath = uploadService.saveUploadFile(file);

		uploadService.prepareTemporaryTable(pageId, username);

		uploadService.getNumberOfColumnsAndValidate(pageId, filePath);

		uploadService.markRunning(pageId, username, columnName, filePath.getFileName().toString());

		sendForExtraction(filePath);

		uploadService.markFinish(pageId, username);

		uploadService.removeUploadedFile(filePath);
	}

}
