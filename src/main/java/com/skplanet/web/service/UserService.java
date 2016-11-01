package com.skplanet.web.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.ldap.core.ContextMapper;
import org.springframework.ldap.core.DirContextOperations;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.core.support.AbstractContextMapper;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.skplanet.web.exception.BizException;
import com.skplanet.web.repository.mysql.UserRepository;
import com.skplanet.web.security.CustomUserDetailsContextMapper;
import com.skplanet.web.security.UserInfo;

@Service
public class UserService implements UserDetailsService {

	@Autowired
	private Environment env;

	@Autowired
	private LdapTemplate ldapTemplate;

	@Autowired
	private CustomUserDetailsContextMapper userDetailsContextMapper;

	@Autowired
	private UserRepository userRepository;

	@Value("${ldap.baseDn}")
	private String ldapBaseDn;

	@Value("${ldap.userSearchFilter}")
	private String ldapUserSearchFilter;

	@Value("${app.pageIds.user}")
	private String userPageIds;

	@Value("${app.pageIds.admin}")
	private String adminPageIds;

	private ContextMapper<UserInfo> nullContextMapper = new AbstractContextMapper<UserInfo>() {
		public UserInfo doMapFromContext(DirContextOperations ctx) {
			return null;
		}
	};

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Map<String, Object> params = new HashMap<>();
		params.put("username", username);

		List<UserInfo> users = userRepository.selectUsers(params);

		if (users.size() <= 0) {
			throw new UsernameNotFoundException("존재하지 않는 사용자입니다");
		}

		return users.get(0);
	}

	public void createUser(Map<String, Object> params) {
		if (userRepository.selectUsers(params).size() > 1) {
			throw new BizException("이미 존재하는 사용자입니다");
		}

		throwIfNotExistInLdap((String) params.get("username"));

		userRepository.upsertUser(params);

		userRepository.insertAuthorities((String) params.get("username"), "ROLE_USER");
	}

	/**
	 * check a user exist in LDAP
	 */
	private void throwIfNotExistInLdap(final String username) {
		// local test only
		if (Arrays.asList(env.getActiveProfiles()).contains("local")) {
			return;
		}
		try {
			ldapTemplate.searchForObject(ldapBaseDn, ldapUserSearchFilter.replace("{0}", username), nullContextMapper);
		} catch (IncorrectResultSizeDataAccessException e) {
			throw new BizException("유효하지 않은 Pnet ID입니다");
		}
	}

	private UserInfo getUserFromLdap(final String username) {
		try {
			return ldapTemplate.searchForObject(ldapBaseDn, ldapUserSearchFilter.replace("{0}", username),
					new AbstractContextMapper<UserInfo>() {
						public UserInfo doMapFromContext(DirContextOperations ctx) {
							return (UserInfo) userDetailsContextMapper.mapUserFromContext(ctx, username, null);
						}
					});
		} catch (IncorrectResultSizeDataAccessException e) {
			throw new BizException("유효하지 않은 Pnet ID입니다");
		}
	}

	public List<UserInfo> getUsers(Map<String, Object> params) {
		// local test only
		if (Arrays.asList(env.getActiveProfiles()).contains("local")) {
			return userRepository.selectUsers(params);
		}
		params.put("onlyUsername", true);
		List<UserInfo> usernames = userRepository.selectUsers(params);
		List<UserInfo> users = new ArrayList<>(usernames.size());

		for (UserInfo u : usernames) {
			users.add(getUserFromLdap(u.getUsername()));
		}

		return users;
	}

	public UserInfo updateUser(Map<String, Object> params) {
		userRepository.upsertUser(params);

		List<UserInfo> users = userRepository.selectUsers(params);
		return users.get(0);
	}

	public void updateAccesses(String username, String pageList) {
		userRepository.deleteAccesses(username, userPageIds);

		if (!StringUtils.isEmpty(pageList)) {
			userRepository.insertAccesses(username, pageList);
		}
	}

	public void updateAdmin(String username, boolean isAdmin) {
		if (isAdmin) {
			userRepository.insertAuthorities(username, "ROLE_ADMIN");
			userRepository.insertAccesses(username, adminPageIds);
		} else {
			userRepository.deleteAuthorities(username, "ROLE_ADMIN");
			userRepository.deleteAccesses(username, adminPageIds);
		}
	}

}
