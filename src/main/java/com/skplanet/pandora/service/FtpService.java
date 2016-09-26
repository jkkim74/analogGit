package com.skplanet.pandora.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.skplanet.pandora.common.Constant;

@Service
public class FtpService {

	@Value("${ftp.host}")
	private String host;

	@Value("${ftp.port}")
	private int port;

	@Value("${ftp.username}")
	private String username;

	@Value("${ftp.password}")
	private String password;

	public void send(String filename) {
		FTPClient ftpClient = new FTPClient();
		try {
			ftpClient.connect(host, port);
			ftpClient.login(username, password);
			ftpClient.enterLocalPassiveMode();

			ftpClient.setFileType(FTP.BINARY_FILE_TYPE);

			// APPROACH #1: uploads first file using an InputStream
			File localPath = new File(Constant.UPLOADED_FILE_DIR + "/" + filename);

			String remotePath = "web/" + filename;

			try (InputStream inputStream = new FileInputStream(localPath)) {
				System.out.println("Start uploading file");
				boolean done = ftpClient.storeFile(remotePath, inputStream);
				if (done) {
					System.out.println("The file is uploaded successfully.");
				}
			}

			// APPROACH #2: uploads second file using an OutputStream
			// File secondLocalFile = new File("E:/Test/Report.doc");
			// String secondRemoteFile = "test/Report.doc";
			// inputStream = new FileInputStream(secondLocalFile);
			//
			// System.out.println("Start uploading second file");
			// OutputStream outputStream =
			// ftpClient.storeFileStream(secondRemoteFile);
			// byte[] bytesIn = new byte[4096];
			// int read = 0;
			//
			// while ((read = inputStream.read(bytesIn)) != -1) {
			// outputStream.write(bytesIn, 0, read);
			// }
			// inputStream.close();
			// outputStream.close();
			//
			// boolean completed = ftpClient.completePendingCommand();
			// if (completed) {
			// System.out.println("The second file is uploaded successfully.");
			// }

		} catch (IOException e) {
			throw new IllegalStateException(e);
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
