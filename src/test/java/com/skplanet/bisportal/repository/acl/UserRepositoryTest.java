package com.skplanet.bisportal.repository.acl;

import com.skplanet.bisportal.model.acl.UserSign;
import com.skplanet.bisportal.testsupport.AbstractContextLoadingTest;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static junit.framework.Assert.assertNotNull;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;

/**
 * The UserSignRepositoryTest class.
 *
 * @author kyoungoh lee
 */
@Slf4j
@Transactional
public class UserRepositoryTest extends AbstractContextLoadingTest {
    @Autowired
    private UserRepository userRepository;

    @Test
    public void testCreateUserSign() throws Exception {
        UserSign userSign = new UserSign();
        userSign.setLoginId("PP39094");
        int result = userRepository.createUserSign(userSign);
        assertThat(result, is(1));
    }

    @Test
    public void testGetUserSign() throws Exception {
        List<UserSign> userSignList = userRepository.getUserSign("PP39094");
        for (UserSign userSignFor : userSignList) {
            assertNotNull(userSignFor.getLoginId());
            assertNull(userSignFor.getSignYn());
            assertNull(userSignFor.getSignDt());
            assertNotNull(userSignFor.getUserNm());
            assertNotNull(userSignFor.getOrgNm());
        }
    }

    @Test
    public void testGetCntUserSign() throws Exception {
        int result = userRepository.getCntUserSign("PP39094");
        assertThat(result, is(0));
    }
}
