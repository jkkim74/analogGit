package com.skplanet.pandora.service;

import java.util.Map;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.apache.velocity.app.VelocityEngine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.ui.velocity.VelocityEngineUtils;

import com.skplanet.ocb.exception.BizException;

@Service
@SuppressWarnings("deprecation")
public class MailService {

	@Autowired
	private JavaMailSender mailSender;

	@Autowired
	private VelocityEngine velocityEngine;

	@Value("${mail.from}")
	private String from;

	@Async
	public void send(String to, String subject, String templateName, Map<String, Object> model) {
		MimeMessage mimeMessage = mailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(mimeMessage);

		try {
			helper.setFrom(from);
			helper.setTo(to);
			helper.setSubject(subject);

			String text = VelocityEngineUtils.mergeTemplateIntoString(velocityEngine, "/templates/mail/" + templateName,
					model);
			helper.setText(text, true);

			mailSender.send(mimeMessage);
		} catch (MessagingException e) {
			throw new BizException("Failed to send EMAIL", e);
		}
	}

	public void send(String[] toList, String subject, String templateName, Map<String, Object> model) {
		for (String to : toList) {
			send(to, subject, templateName, model);
		}
	}

}
