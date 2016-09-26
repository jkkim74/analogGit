package com.skplanet.pandora.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.naming.NamingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.ldap.core.ContextMapper;
import org.springframework.ldap.core.DirContextOperations;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.skplanet.pandora.common.BizException;
import com.skplanet.pandora.common.Constant;
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

	private List<SimpleGrantedAuthority> defaultAuthorities = new ArrayList<>();

	{
		defaultAuthorities.add(new SimpleGrantedAuthority("ROLE_USER"));
	}

	@Transactional("mysqlTxManager")
	public void createUser(final String username) {
		if (userDetailsManager.userExists(username)) {
			throw new BizException("이미 존재하는 사용자입니다");
		}

		UserInfo user = getUserFromLdap(username, defaultAuthorities);
		user.setEnabled(true);

		userDetailsManager.createUser(user);
	}

	private UserInfo getUserFromLdap(final String username, final Collection<? extends GrantedAuthority> authorities) {
		try {
			return ldapTemplate.searchForObject(ldapBaseDn, Constant.LDAP_USER_SEARCH_FILTER.replace("{0}", username),
					new ContextMapper<UserInfo>() {
						@Override
						public UserInfo mapFromContext(Object ctx) throws NamingException {
							return (UserInfo) userDetailsContextMapper.mapUserFromContext((DirContextOperations) ctx,
									username, authorities);
						}
					});
		} catch (IncorrectResultSizeDataAccessException e) {
			throw new BizException("유효하지 않은 Pnet ID입니다");
		}
	}

	public List<UserInfo> getUsers() {
		List<String> users = mysqlRepository.selectUsers();
		List<UserInfo> userInfoList = new ArrayList<>(users.size());

		for (String username : users) {
			UserDetails userWithAuthorities = userDetailsManager.loadUserByUsername(username);

			UserInfo userInfo = getUserFromLdap(username, userWithAuthorities.getAuthorities());
			userInfo.setEnabled(userWithAuthorities.isEnabled());

			userInfoList.add(userInfo);
		}

		return userInfoList;
	}

	@Transactional("mysqlTxManager")
	public void enableUser(String username, boolean enable) {
		// mysqlRepository.updateUser(enable);
	}

}
