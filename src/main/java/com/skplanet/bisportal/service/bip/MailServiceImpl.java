package com.skplanet.bisportal.service.bip;

import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Service;

/**
 * Created by pepsi on 2014. 10. 7..
 */
@Service
public class MailServiceImpl implements MailService {
  @Autowired
  private JavaMailSender mailSender;

  @Override
  public void sendBoss(final SimpleMailMessage msg) throws Exception {
    MimeMessagePreparator preparator = new MimeMessagePreparator() {
      @Override
      public void prepare(MimeMessage mimeMessage) throws Exception {
        MimeMessageHelper message = new MimeMessageHelper(mimeMessage);
        message.setTo(msg.getTo());
        message.setFrom(msg.getFrom());
        message.setSubject(msg.getSubject());
        message.setText(msg.getText(), true); // HTML 발송
      }
    };

    mailSender.send(preparator);
  }
}
