package com.skplanet.web.service;

import java.io.InputStream;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.UserInfo;
import com.skplanet.web.exception.BizException;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class SshService {

	@Value("${ssh.host}")
	private String sshHost;

	@Value("${ssh.port}")
	private int sshPort;

	@Value("${ssh.username}")
	private String sshUsername;

	@Value("${ssh.password}")
	private String sshPassword;

	@Value("${app.enable.ssh}")
	private boolean enabled;

	public void execute(String username, String inputDataType, String periodType, String periodFrom, String periodTo,
			String filename, int extractionTarget, String extractionCond, String singleReq, String extractionBase) {

		StringBuilder builder = new StringBuilder("sh /app/home/bi_ocb/WEB/web_2_5.sh ");
		String command = builder.append(username)
				.append(' ').append(inputDataType)
				.append(' ').append(periodType)
				.append(' ').append(periodFrom)
				.append(' ').append(periodTo)
				.append(' ').append(extractionTarget)
				.append(' ').append(filename)
				.append(' ').append(extractionCond)
				.append(' ').append(singleReq)
				.append(' ').append(extractionBase)
				.toString();

		log.info("execute ssh command: {}", command);

		if (!enabled) {
			log.debug("disabled");
			return;
		}

		JSch sshClient = new JSch();
		ChannelExec execChannel = null;
		Session session = null;

		try {
			session = sshClient.getSession(sshUsername, sshHost, sshPort);

			UserInfo ui = new MyUI(sshPassword);
			session.setUserInfo(ui);
			session.connect();

			execChannel = (ChannelExec) session.openChannel("exec");
			execChannel.setCommand(command);
			// execChannel.start();

			execChannel.setInputStream(null);
			execChannel.setErrStream(System.err);

			InputStream in = execChannel.getInputStream();

			execChannel.connect();

			byte[] tmp = new byte[1024];
			while (true) {
				while (in.available() > 0) {
					int i = in.read(tmp, 0, 1024);
					if (i < 0)
						break;
					
					String s = new String(tmp, 0, i);
					log.info(s);
					
					if (s.indexOf("exception") != -1) {
						throw new RuntimeException(s.substring(s.indexOf("exception")));
					}
				}

				if (execChannel.isClosed()) {
					if (in.available() > 0)
						continue;
					log.info("exit_status: {}", execChannel.getExitStatus());
					break;
				}

				try {
					Thread.sleep(1000);
				} catch (Exception e) {
				}
			}

			// execChannel.disconnect();
			// session.disconnect();
		} catch (Exception e) {
			log.error(e.toString());
			throw new BizException("Failed to send using SSH", e);
		} finally {
			if (execChannel != null) {
				execChannel.disconnect();
				execChannel = null;
			}
			if (session != null) {
				session.disconnect();
				session = null;
			}
		}
	}

	private static class MyUI implements UserInfo {

		private String password;

		public MyUI(String password) {
			this.password = password;
		}

		@Override
		public String getPassphrase() {
			return null;
		}

		@Override
		public String getPassword() {
			return password;
		}

		@Override
		public boolean promptPassphrase(String arg0) {
			return true;
		}

		@Override
		public boolean promptPassword(String arg0) {
			return true;
		}

		@Override
		public boolean promptYesNo(String arg0) {
			return true;
		}

		@Override
		public void showMessage(String arg0) {
			System.out.println(arg0);
		}

	}

}
