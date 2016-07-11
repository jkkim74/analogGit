package com.skplanet.bisportal.service.acl;

import com.skplanet.bisportal.model.acl.UserSign;
import com.skplanet.bisportal.repository.acl.UserRepository;
import com.skplanet.bisportal.testsupport.AbstractContextLoadingTest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.List;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class UserServiceImplTest extends AbstractContextLoadingTest {
  @Mock
  private UserRepository userRepository;

  @InjectMocks
  private UserService mockUserService = new UserServiceImpl();

  @Test
  public void testUserSign() throws Exception {
    UserSign userSign = new UserSign();
    userSign.setLoginId("PP44651");
    int result = mockUserService.userSign(userSign);
    assertThat(result, is(0));
    verify(userRepository, times(1)).createUserSign(userSign);
    reset(userRepository);
  }

  @Test
  public void testGetUserSign() throws Exception {
    String loginId = "PP44651";
    List<UserSign> userSign = mockUserService.getUserSign(loginId);
    if (userSign.size() > 0)
      assertNotNull(userSign);
  }

  @Test
  public void testCntUserSign() throws Exception {
    String loginId = "PP44651";
    int result = mockUserService.cntUserSign(loginId);
    assertThat(result, is(0));
    verify(userRepository, times(1)).getCntUserSign(loginId);
    reset(userRepository);
  }
}
