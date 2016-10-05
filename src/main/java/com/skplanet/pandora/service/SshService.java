package com.skplanet.pandora.service;

import java.io.InputStream;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.UserInfo;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class SshService {

	@Value("${pandora.ssh.host}")
	private String sshHost;

	@Value("${pandora.ssh.port}")
	private int sshPort;

	@Value("${pandora.ssh.username}")
	private String sshUsername;

	@Value("${pandora.ssh.password}")
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

			UserInfo ui = new MyUI(sshPassword);
			session.setUserInfo(ui);
			session.connect();

			execChannel = (ChannelExec) session.openChannel("exec");
			execChannel.connect();
			execChannel.setCommand(command);
			// execChannel.start();

			execChannel.setInputStream(null);
			execChannel.setErrStream(System.err);

			try (InputStream in = execChannel.getInputStream()) {

				byte[] tmp = new byte[1024];
				while (true) {
					while (in.available() > 0) {
						int i = in.read(tmp, 0, 1024);
						if (i < 0)
							break;
						System.out.println(new String(tmp, 0, i));
					}

					if (execChannel.isClosed()) {
						if (in.available() > 0)
							continue;
						System.out.println("exit_status: " + execChannel.getExitStatus());
						break;
					}

					try {
						Thread.sleep(1000);
					} catch (Exception e) {
					}
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (execChannel != null) {
				execChannel.disconnect();
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
