package com.skplanet.bisportal.service.acl;

import com.skplanet.bisportal.model.acl.BipUser;
import com.skplanet.bisportal.testsupport.AbstractContextLoadingTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class LdapServiceImplTest extends AbstractContextLoadingTest {
	@Autowired
	private LdapService ldapServiceImpl;
	private static final String username = "pp39093";
	private static final String password = "Wn664483";

	@Test
	public void testAuthenticate() throws Exception {
		boolean isAuth = ldapServiceImpl.authenticate(username, password);
		assertTrue(isAuth);
	}

	@Test
	public void testLogin() throws Exception {
		BipUser bipUser = ldapServiceImpl.login(username, password);
		assertNotNull(bipUser);
	}

	@Test
	public void testGetBipUser() throws Exception {
		BipUser bipUser = ldapServiceImpl.getBipUserUser(username);
		assertNotNull(bipUser);
	}
}
