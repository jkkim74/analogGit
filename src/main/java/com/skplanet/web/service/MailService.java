package com.skplanet.web.service;

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

import com.google.common.collect.ObjectArrays;
import com.skplanet.web.exception.BizException;
import com.skplanet.web.util.Helper;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@SuppressWarnings("deprecation")
public class MailService {

	@Autowired
	private JavaMailSender mailSender;

	@Autowired
	private VelocityEngine velocityEngine;

	@Value("${mail.from}")
	private String from;

	@Value("${mail.to}")
	private String withTo;

	@Value("${mail.cc}")
	private String withCc;

	@Value("${app.enable.mail}")
	private boolean enabled;

	@Async
	public void send(String templateName, Map<String, Object> model, String subject, String[] to, String[] cc) {
		if (!enabled) {
			log.debug("disabled");
			return;
		}

		MimeMessage mimeMessage = mailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(mimeMessage);

		try {
			helper.setFrom(from);
			helper.setSubject(subject);
			helper.setTo(Helper.eraseEmpty(to, String.class));
			helper.setCc(Helper.eraseEmpty(cc, String.class));

			String text = VelocityEngineUtils.mergeTemplateIntoString(velocityEngine, "/templates/mail/" + templateName,
					model);
			helper.setText(text, true);

			mailSender.send(mimeMessage);
		} catch (MessagingException e) {
			throw new BizException("Failed to send using EMAIL", e);
		}
	}

	public void sendAsTo(String templateName, Map<String, Object> model, String subject, String to) {
		send(templateName, model, subject, new String[] { to }, null);
	}

	public void sendMeAsTo(String templateName, Map<String, Object> model, String subject) {
		String me = Helper.currentUser().getEmailAddr();

		sendAsTo(templateName, model, subject, me);
	}

	public void sendMeAsCc(String templateName, Map<String, Object> model, String subject) {
		String me = Helper.currentUser().getEmailAddr();
		String[] toList = withTo.split("[,;]");
		String[] ccList = withCc.split("[,;]");

		send(templateName, model, subject, toList, ObjectArrays.concat(ccList, new String[] { me }, String.class));
	}

}
