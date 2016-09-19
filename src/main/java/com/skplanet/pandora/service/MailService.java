package com.skplanet.pandora.service;

import java.util.Map;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.apache.velocity.app.VelocityEngine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.ui.velocity.VelocityEngineUtils;

import com.skplanet.pandora.common.BizException;

@SuppressWarnings("deprecation")
@Service
public class MailService {

	@Autowired
	private JavaMailSender mailSender;

	@Autowired
	private VelocityEngine velocityEngine;

	@Async
	public void send(String from, String to, String subject, String templateLocation, Map<String, Object> model) {

		MimeMessage mimeMessage = mailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(mimeMessage);

		try {
			helper.setFrom(from);
			helper.setTo(to);
			helper.setSubject(subject);

			String text = VelocityEngineUtils.mergeTemplateIntoString(velocityEngine, templateLocation, model);
			helper.setText(text, true);

			mailSender.send(mimeMessage);
		} catch (MessagingException e) {
			throw new BizException("Failed to send email", e);
		}
	}

}
