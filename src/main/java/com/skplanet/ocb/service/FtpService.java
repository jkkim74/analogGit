package com.skplanet.ocb.service;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.skplanet.ocb.exception.BizException;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class FtpService {

	@Value("${app.enable.ftp}")
	private boolean enabled;

	public void send(Path localPath, String remotePath, String host, int port, String username, String password) {
		log.info("Sending file from [{}] to [{}/{}]", localPath, host, remotePath);

		if (!enabled) {
			log.debug("disabled");
			return;
		}

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

}
