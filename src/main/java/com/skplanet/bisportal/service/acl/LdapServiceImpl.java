package com.skplanet.bisportal.service.acl;

import com.skplanet.bisportal.model.acl.BipUser;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ldap.core.AttributesMapper;
import org.springframework.ldap.core.CollectingAuthenticationErrorCallback;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.filter.AndFilter;
import org.springframework.ldap.filter.EqualsFilter;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.naming.NamingException;
import javax.naming.directory.Attributes;
import java.util.List;

/**
 * The LdapServiceImpl class(LDAP 구현 클래스).
 * 
 * @author HoJin-Ha (mimul@wiseeco.com)
 * 
 */
@Slf4j
@Service
public class LdapServiceImpl implements LdapService {
	@Autowired
	private LdapTemplate ldapTemplate;

	/**
	 * LDAP 로그인 확인.
	 *
	 * @param username, password
	 * @return boolean
	 * @throws Exception
	 */
	@Override
	public boolean authenticate(String username, String password) {
		AndFilter filter = new AndFilter();
		filter.and(new EqualsFilter("cn", username));
		CollectingAuthenticationErrorCallback errorCallback = new CollectingAuthenticationErrorCallback();
		boolean result = ldapTemplate.authenticate(StringUtils.EMPTY, filter.toString(), password, errorCallback);
		if (!result) {
			Exception error = errorCallback.getError();
			log.warn("authenticate error: {}", error);
		}
		return result;
	}

	/**
	 * LDAP 로그인 처리.
	 *
	 * @param username, password
	 * @return BipUser
	 * @throws Exception
	 */
	@Override
	public BipUser login(String username, String password) {
		if (authenticate(username, password)) {
			AndFilter filter = new AndFilter();
			filter.and(new EqualsFilter("cn", username));
			@SuppressWarnings("unchecked")
			List<BipUser> result = ldapTemplate.search(StringUtils.EMPTY, filter.toString(), new AttributesMapper<BipUser>() {

				@Override
				public BipUser mapFromAttributes(Attributes attrs) throws NamingException {
					return makeUserUsingLdapAttrs(attrs);
				}
			});
			if (!CollectionUtils.isEmpty(result)) {
				return result.get(0);
			} else {
				return null;
			}
		} else {
			return null;
		}
	}

	/**
	 * LDAP에서 사용자 정보 조회.
	 *
	 * @param username
	 * @return BipUser
	 * @throws Exception
	 */
	@Override
	public BipUser getBipUserUser(String username) {
		AndFilter filter = new AndFilter();
		filter.and(new EqualsFilter("objectclass", "Person")).and(new EqualsFilter("cn", username));
		@SuppressWarnings("unchecked")
		List<BipUser> result = ldapTemplate.search(StringUtils.EMPTY, filter.toString(), new AttributesMapper<BipUser>() {

			@Override
			public BipUser mapFromAttributes(Attributes attrs) throws NamingException {
				return makeUserUsingLdapAttrs(attrs);
			}
		});
		if (!CollectionUtils.isEmpty(result)) {
			return result.get(0);
		} else {
			return null;
		}
	}

	private BipUser makeUserUsingLdapAttrs(Attributes attrs) throws NamingException {
		BipUser user = new BipUser();
		if (attrs != null) {
			user.setUsername(StringUtils.defaultIfEmpty((String) attrs.get("cn").get(), StringUtils.EMPTY));
			if (attrs.get("ExADKoreanName") != null) {
				user.setFullname(
						StringUtils.defaultIfEmpty((String) attrs.get("ExADKoreanName").get(), StringUtils.EMPTY));
			} else {
				user.setFullname(user.getUsername());
			}
			if (attrs.get("mail") != null)
				user.setEmail(StringUtils.defaultIfEmpty((String) attrs.get("mail").get(), StringUtils.EMPTY));
			if (attrs.get("mobile") != null)
				user.setMobile(StringUtils.defaultIfEmpty((String) attrs.get("mobile").get(), StringUtils.EMPTY));
		}
		return user;
	}
}
