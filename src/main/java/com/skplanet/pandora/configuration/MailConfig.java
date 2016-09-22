package com.skplanet.pandora.configuration;

import java.util.Properties;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

@Configuration
public class MailConfig {

	@Value("${mail.host}")
	private String host;

	@Value("${mail.port}")
	private int port;

	@Value("${mail.username:}")
	private String username;

	@Value("${mail.password:}")
	private String password;

	@Value("${mail.smtp.ehlo:true}")
	private String smtpEhlo;

	@Value("${mail.smtp.auth:false}")
	private String smtpAuth;

	@Value("${mail.smtp.starttls.enable:false}")
	private String smtpStartTlsEnable;

	@Value("${mail.debug:false}")
	private String debug;

	@Bean
	public JavaMailSender mailSender() {

		JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
		mailSender.setHost(host);
		mailSender.setPort(port);
		mailSender.setUsername(username);
		mailSender.setPassword(password);
		mailSender.setDefaultEncoding("UTF-8");

		Properties properties = new Properties();
		properties.setProperty("mail.smtp.ehlo", smtpEhlo);
		properties.setProperty("mail.smtp.auth", smtpAuth);
		properties.setProperty("mail.smtp.starttls.enable", smtpStartTlsEnable);
		properties.setProperty("mail.debug", debug);
		mailSender.setJavaMailProperties(properties);

		return mailSender;
	}

}
