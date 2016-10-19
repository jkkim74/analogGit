package com.skplanet.ctas.service;

import java.nio.file.Path;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.skplanet.ocb.service.FtpService;
import com.skplanet.ocb.service.PtsService;

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

	@Autowired
	private PtsService ptsService;

	public void sendToFtp(Path localPath) {
		String remotePath = localPath.getFileName().toString();

		//ftpService.send(localPath, remotePath, ftpHost, ftpPort, ftpUsername, ftpPassword);
	}

	public void sendToPts(Path localPath, String ptsUsername) {
		String filename = localPath.toFile().getAbsolutePath();

		//ptsService.send(filename, ptsUsername);
	}

}
