package com.skplanet.pandora.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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

import com.skplanet.pandora.exception.BizException;
import com.skplanet.pandora.model.UserInfo;
import com.skplanet.pandora.repository.mysql.MysqlRepository;
import com.skplanet.pandora.security.CustomUserDetailsContextMapper;
import com.skplanet.pandora.util.Constant;

@Service
public class UserService implements UserDetailsService {

	@Autowired
	private LdapTemplate ldapTemplate;

	@Autowired
	private CustomUserDetailsContextMapper userDetailsContextMapper;

	@Autowired
	private MysqlRepository mysqlRepository;

	@Value("${pandora.ldap.baseDn}")
	private String ldapBaseDn;

	private ContextMapper<UserInfo> nullContextMapper = new AbstractContextMapper<UserInfo>() {
		public UserInfo doMapFromContext(DirContextOperations ctx) {
			return null;
		}
	};

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Map<String, Object> params = new HashMap<>();
		params.put("username", username);

		List<UserInfo> users = mysqlRepository.selectUsers(params);

		if (users.size() <= 0) {
			throw new UsernameNotFoundException("존재하지 않는 사용자입니다");
		}

		return users.get(0);
	}

	public void createUser(Map<String, Object> params) {
		if (mysqlRepository.selectUsers(params).size() > 1) {
			throw new BizException("이미 존재하는 사용자입니다");
		}

		throwIfNotExistInLdap((String) params.get("username"));

		mysqlRepository.upsertUser(params);

		mysqlRepository.insertAuthorities((String) params.get("username"), "ROLE_USER");
	}

	/**
	 * check a user exist in LDAP
	 */
	private void throwIfNotExistInLdap(final String username) {
		try {
			ldapTemplate.searchForObject(ldapBaseDn, Constant.LDAP_USER_SEARCH_FILTER.replace("{0}", username),
					nullContextMapper);
		} catch (IncorrectResultSizeDataAccessException e) {
			throw new BizException("유효하지 않은 Pnet ID입니다");
		}
	}

	private UserInfo getUserFromLdap(final String username) {
		try {
			return ldapTemplate.searchForObject(ldapBaseDn, Constant.LDAP_USER_SEARCH_FILTER.replace("{0}", username),
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
		params.put("onlyUsername", true);
		List<UserInfo> usernames = mysqlRepository.selectUsers(params);
		List<UserInfo> users = new ArrayList<>(usernames.size());

		for (UserInfo u : usernames) {
			users.add(getUserFromLdap(u.getUsername()));
		}

		return users;
	}

	public UserInfo updateUser(Map<String, Object> params) {
		mysqlRepository.upsertUser(params);

		List<UserInfo> users = mysqlRepository.selectUsers(params);
		return users.get(0);
	}

	public void updateAccesses(String username, String pageList) {
		mysqlRepository.deleteAccesses(username, Constant.USER_ACCESSES);

		if (!StringUtils.isEmpty(pageList)) {
			mysqlRepository.insertAccesses(username, pageList);
		}
	}

	public void updateAdmin(String username, boolean isAdmin) {
		if (isAdmin) {
			mysqlRepository.insertAuthorities(username, "ROLE_ADMIN");
			mysqlRepository.insertAccesses(username, Constant.ADMIN_ACCESSES);
		} else {
			mysqlRepository.deleteAuthorities(username, "ROLE_ADMIN");
			mysqlRepository.deleteAccesses(username, Constant.ADMIN_ACCESSES);
		}
	}

}
