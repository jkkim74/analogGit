package com.skplanet.pandora.service;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.skplanet.pandora.exception.BizException;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class FtpService {

	@Value("${ftp.extraction.host}")
	private String extractionHost;

	@Value("${ftp.extraction.port}")
	private int extractionPort;

	@Value("${ftp.extraction.username}")
	private String extractionUsername;

	@Value("${ftp.extraction.password}")
	private String extractionPassword;

	@Value("${ftp.notification.host}")
	private String notificationHost;

	@Value("${ftp.notification.port}")
	private int notificationPort;

	@Value("${ftp.notification.username}")
	private String notificationUsername;

	@Value("${ftp.notification.password}")
	private String notificationPassword;

	public void send(Path localPath, String remotePath, String host, int port, String username, String password) {
		log.info("Sending file from [{}] to [{}/{}]", localPath, host, remotePath);

		FTPClient ftpClient = new FTPClient();

		try {
			log.info("Connecting... {}:{}", host, port);
			ftpClient.connect(host, port);
			log.info("Login... {}:{}", host, port);
			ftpClient.login(username, password);
			ftpClient.enterLocalPassiveMode();
			ftpClient.setFileType(FTP.BINARY_FILE_TYPE);

			try (InputStream localIn = new FileInputStream(localPath.toFile())) {
				boolean done = ftpClient.storeFile(remotePath, localIn);
				if (done) {
					log.info("The file is sent successfully.");
				}
			}
		} catch (IOException e) {
			throw new BizException("Failed to send using FTP", e);
		} finally {
			try {
				if (ftpClient.isConnected()) {
					ftpClient.logout();
					ftpClient.disconnect();
				}
			} catch (IOException e) {
				throw new IllegalStateException(e);
			}
		}
	}

	public void sendForExtraction(Path localPath, String remotePath) {
		send(localPath, remotePath, extractionHost, extractionPort, extractionUsername, extractionPassword);
	}

	public void sendForNotification(Path localPath, String remotePath) {
		send(localPath, remotePath, notificationHost, notificationPort, notificationUsername, notificationPassword);
	}

}
