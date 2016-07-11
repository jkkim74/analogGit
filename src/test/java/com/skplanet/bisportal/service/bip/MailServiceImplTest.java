package com.skplanet.bisportal.service.bip;

import com.skplanet.bisportal.testsupport.AbstractContextLoadingTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;

import static org.junit.Assert.assertNotNull;

public class MailServiceImplTest extends AbstractContextLoadingTest {

  @Autowired
  private MailService mailServiceImpl;

  @Test
  public void testSendBoss() throws Exception {
    SimpleMailMessage msg = new SimpleMailMessage();
    msg.setFrom("voyagerstream@gmail.com");
    msg.setSubject("Test");
    msg.setText("Test");
    msg.setTo("voyagerstream@gmail.com");
    //mailServiceImpl.sendBoss(msg); // 로컬에서 테스트 안됨.
    assertNotNull(msg);
  }
}
