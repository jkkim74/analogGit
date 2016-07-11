package com.skplanet.bisportal.service.acl;

import com.skplanet.bisportal.model.acl.BipUser;

/**
 * The LdapService interface(LDAP 인터페이스).
 *
 * @author HoJin-Ha (mimul@wiseeco.com)
 *
 */
public interface LdapService {
	/**
	 * @param username
	 * @param password
	 * @return
	 */
	boolean authenticate(String username, String password);

	BipUser login(String username, String password);

	BipUser getBipUserUser(String username);

}
