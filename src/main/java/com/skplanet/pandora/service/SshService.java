package com.skplanet.pandora.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.UIKeyboardInteractive;
import com.jcraft.jsch.UserInfo;
import com.skplanet.pandora.common.BizException;

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

	public void execute(String username, String inputDataType, String periodType, String periodFrom, String periodTo,
			String filename) {

		StringBuilder builder = new StringBuilder("sh /app/home/bi_ocb/WEB/web_2_5.sh ");
		String command = builder.append(username).append(' ').append(inputDataType).append(' ').append(periodType)
				.append(' ').append(periodFrom).append(' ').append(periodTo).append(" 1 ").append(filename).toString();

		log.info("execute ssh command: {}", command);

		JSch sshClient = new JSch();
		ChannelExec execChannel = null;

		try {
			Session session = sshClient.getSession(sshUsername, sshHost, sshPort);
			session.setPassword(sshPassword);
			session.connect();

			execChannel = (ChannelExec) session.openChannel("exec");
			execChannel.connect();
			execChannel.setCommand(command);
			execChannel.start();
		} catch (JSchException e) {
			throw new BizException("Failed to execute SSH", e);
		} finally {
			if (execChannel != null) {
				execChannel.disconnect();
			}
		}
	}

	@Slf4j
	private static class MyUI implements UserInfo, UIKeyboardInteractive {

		private String password;

		public MyUI(String password) {
			this.password = password;
		}

		@Override
		public String[] promptKeyboardInteractive(String arg0, String arg1, String arg2, String[] arg3,
				boolean[] arg4) {
			return null;
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
			log.info(arg0);
		}

	}

}
