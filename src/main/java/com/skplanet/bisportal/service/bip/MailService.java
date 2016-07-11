package com.skplanet.bisportal.service.bip;

import org.springframework.mail.SimpleMailMessage;

/**
 * Created by pepsi on 2014. 10. 7..
 */
public interface MailService {
	void sendBoss(SimpleMailMessage msg) throws Exception;
}
