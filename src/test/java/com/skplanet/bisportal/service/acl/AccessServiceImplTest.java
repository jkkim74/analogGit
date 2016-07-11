package com.skplanet.bisportal.service.acl;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.skplanet.bisportal.model.acl.AccessLog;
import com.skplanet.bisportal.model.acl.LoginLog;
import com.skplanet.bisportal.repository.acl.AccessLogRepository;

@RunWith(MockitoJUnitRunner.class)
public class AccessServiceImplTest {
  @Mock
  private AccessLogRepository accessLogRepository;

  @InjectMocks
  private AccessService mockAccessService = new AccessServiceImpl();

  @Before
  public void init() {
    LoginLog loginLog = new LoginLog();
    loginLog.setIp("pp39093");
    loginLog.setUsername("127.0.0.1");
    when(accessLogRepository.createLoginLog(loginLog)).thenReturn(1);
    AccessLog accessLog = new AccessLog();
    accessLog.setUsername("pp39093");
    accessLog.setIp("127.0.0.1");
    when(accessLogRepository.createAccessLog(accessLog)).thenReturn(1);
  }

  @Test
  public void testCreateAccessLog() throws Exception {
    AccessLog accessLog = new AccessLog();
    accessLog.setUsername("pp39093");
    accessLog.setIp("127.0.0.1");
    int result = mockAccessService.createAccessLog(accessLog);
    assertThat(result, is(1));
    verify(accessLogRepository, times(1)).createAccessLog(accessLog);
    reset(accessLogRepository);
  }

  @Test
  public void testCreateLoginLog() throws Exception {
    LoginLog loginLog = new LoginLog();
    loginLog.setIp("pp39093");
    loginLog.setUsername("127.0.0.1");
    int result = mockAccessService.createLoginLog(loginLog);
    assertThat(result, is(1));
    verify(accessLogRepository, times(1)).createLoginLog(loginLog);
    reset(accessLogRepository);
  }
}
