package com.skplanet.pandora.configuration;

import java.util.Properties;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

@Configuration
public class MailConfig {

	@Bean
	public JavaMailSender mailSender(@Value("${mail.host}") String host, @Value("${mail.port}") int port,
			@Value("${mail.username:}") String username, @Value("${mail.password:}") String password,
			@Value("${mail.smtp.ehlo:true}") String smtpEhlo, @Value("${mail.smtp.auth:false}") String smtpAuth,
			@Value("${mail.smtp.starttls.enable:false}") String smtpStartTlsEnable,
			@Value("${mail.debug:false}") String debug) {

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
