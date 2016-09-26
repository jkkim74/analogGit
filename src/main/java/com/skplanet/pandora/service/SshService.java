package com.skplanet.pandora.service;

import java.io.InputStream;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.UIKeyboardInteractive;
import com.jcraft.jsch.UserInfo;

import lombok.extern.slf4j.Slf4j;

@Service
public class SshService {

	@Value("${ssh.host}")
	private String host;

	@Value("${ssh.port}")
	private int port;

	@Value("${ssh.username}")
	private String username;

	@Value("${ssh.password}")
	private String password;

	public void execute(String username, String fromDt, String toDt, String filename) {
		JSch jsch = new JSch();

		try {
			Session session = jsch.getSession(username, host, port);

			UserInfo ui = new MyUI(password);
			session.setUserInfo(ui);
			session.connect();

			String command = "sh /app/home/bi_ocb/WEB/web_2_5.sh " + username + " mbr_id rcv_dt " + fromDt + " " + toDt + " 1 " + filename;

			Channel channel = session.openChannel("exec");
			((ChannelExec) channel).setCommand(command);

			channel.setInputStream(null);

			((ChannelExec) channel).setErrStream(System.err);

			InputStream in = channel.getInputStream();

			channel.connect();

			byte[] tmp = new byte[1024];
			while (true) {
				while (in.available() > 0) {
					int i = in.read(tmp, 0, 1024);
					if (i < 0)
						break;
					System.out.println(new String(tmp, 0, i));
				}

				if (channel.isClosed()) {
					if (in.available() > 0)
						continue;
					System.out.println("exit_status: " + channel.getExitStatus());
					break;
				}

				try {
					Thread.sleep(1000);
				} catch (Exception e) {
				}
			}

			channel.disconnect();
			session.disconnect();

		} catch (Exception e) {
			e.printStackTrace();
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
