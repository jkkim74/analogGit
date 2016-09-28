package com.skplanet.pandora.service;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.springframework.stereotype.Service;

import com.skplanet.pandora.common.BizException;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class FtpService {

	public void send(Path localPath, String remotePath, String host, int port, String username, String password) {
		FTPClient ftpClient = new FTPClient();

		try {
			ftpClient.connect(host, port);
			ftpClient.login(username, password);
			ftpClient.enterLocalPassiveMode();
			ftpClient.setFileType(FTP.BINARY_FILE_TYPE);

			try (InputStream localIn = new FileInputStream(localPath.toFile())) {
				log.info("Put file to {}/{}", host, remotePath);
				boolean done = ftpClient.storeFile(remotePath, localIn);
				if (done) {
					log.info("The file is uploaded successfully.");
				}
			}
		} catch (IOException e) {
			throw new BizException("Failed to forward using FTP", e);
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
