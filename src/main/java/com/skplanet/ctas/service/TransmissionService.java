package com.skplanet.ctas.service;

import java.nio.file.Path;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.skplanet.ocb.service.FtpService;

@Service
public class TransmissionService {

	@Value("${ctas.ftp.host}")
	private String ftpHost;

	@Value("${ctas.ftp.port}")
	private int ftpPort;

	@Value("${ctas.ftp.username}")
	private String ftpUsername;

	@Value("${ctas.ftp.password}")
	private String ftpPassword;

	@Autowired
	private FtpService ftpService;

	public void sendToFtp(Path localPath, String remotePath) {
		ftpService.send(localPath, remotePath, ftpHost, ftpPort, ftpUsername, ftpPassword);
	}
	
	public void sendToPts() {
		
	}

}
