package com.skplanet.pandora.service;

import java.util.ArrayList;
import java.util.List;

import javax.naming.NamingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.ldap.core.ContextMapper;
import org.springframework.ldap.core.DirContextOperations;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Service;

import com.skplanet.pandora.common.BizException;
import com.skplanet.pandora.common.Constant;
import com.skplanet.pandora.model.AutoMappedMap;
import com.skplanet.pandora.model.UserInfo;
import com.skplanet.pandora.repository.mysql.MysqlRepository;
import com.skplanet.pandora.security.CustomUserDetailsContextMapper;

@Service
public class UserService {

	@Autowired
	private UserDetailsManager userDetailsManager;

	@Autowired
	private LdapTemplate ldapTemplate;

	@Autowired
	private CustomUserDetailsContextMapper userDetailsContextMapper;

	@Autowired
	private MysqlRepository mysqlRepository;

	@Value("${ldap.baseDn}")
	private String ldapBaseDn;

	public void createUser(final String username) {
		if (userDetailsManager.userExists(username)) {
			throw new BizException("Already created user: " + username);
		}

		final List<SimpleGrantedAuthority> authorities = new ArrayList<>();
		authorities.add(new SimpleGrantedAuthority("ROLE_USER"));

		UserInfo user = ldapTemplate.searchForObject(ldapBaseDn, Constant.LDAP_USER_SEARCH_FILTER,
				new ContextMapper<UserInfo>() {
					@Override
					public UserInfo mapFromContext(Object ctx) throws NamingException {
						return (UserInfo) userDetailsContextMapper.mapUserFromContext((DirContextOperations) ctx,
								username, authorities);
					}
				});

		if (user == null) {
			throw new BizException("Invalid user in LDAP : " + username);
		}

		userDetailsManager.createUser(user);
		mysqlRepository.upsertUserInfo(user);
	}

	public List<AutoMappedMap> getUsers() {
		return mysqlRepository.selectUsers();
	}

	public void enableUser(String username, boolean enable) {
		mysqlRepository.updateUser(enable);
	}

}
