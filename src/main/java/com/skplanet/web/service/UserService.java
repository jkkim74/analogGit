package com.skplanet.web.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.ldap.core.DirContextOperations;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.ldap.search.LdapUserSearch;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.skplanet.web.exception.BizException;
import com.skplanet.web.repository.mysql.UserRepository;
import com.skplanet.web.security.CustomUserDetailsContextMapper;
import com.skplanet.web.security.UserInfo;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class UserService implements UserDetailsService {

	@Autowired
	private Environment env;

	@Autowired
	private LdapUserSearch ldapUserSearch;

	@Autowired
	private CustomUserDetailsContextMapper userDetailsContextMapper;

	@Autowired
	private UserRepository userRepository;

	@Value("${app.menuIds.user}")
	private String userMenu;

	@Value("${app.menuIds.admin}")
	private String adminMenu;

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
			ldapUserSearch.searchForUser(username);
		} catch (UsernameNotFoundException e) {
			throw new BizException("유효하지 않은 Pnet ID입니다");
		}
	}

	private UserInfo getUserFromLdap(final String username) {
		try {
			DirContextOperations ctx = ldapUserSearch.searchForUser(username);
			return (UserInfo) userDetailsContextMapper.mapUserFromContext(ctx, username, null);
		} catch (Exception e) {
			log.warn("유효하지 않은 Pnet ID입니다 - {}", username);
			return UserInfo.builder().username(username).build();
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
			UserInfo user = getUserFromLdap(u.getUsername());
			if(user.getUsername() != null && user.getUsername() != ""
					&& user.getEmailAddr() != null && user.getEmailAddr() != "") {
				users.add(user);
			}
		}

		return users;
	}

	public UserInfo updateUser(Map<String, Object> params) {
		userRepository.upsertUser(params);

		List<UserInfo> users = userRepository.selectUsers(params);
		return users.get(0);
	}

	public void updateAccesses(String username, String menuList) {
		userRepository.deleteAccesses(username, userMenu);

		if (!StringUtils.isEmpty(menuList)) {
			userRepository.insertAccesses(username, menuList);
		}
	}

	public void updateAdmin(String username, boolean isAdmin) {
		if (isAdmin) {
			userRepository.insertAuthorities(username, "ROLE_ADMIN");
			userRepository.insertAccesses(username, adminMenu);
		} else {
			userRepository.deleteAuthorities(username, "ROLE_ADMIN");
			userRepository.deleteAccesses(username, adminMenu);
		}
	}

	public void updateMasking(String username, String maskingYn){
	    userRepository.updateMasking(username, maskingYn);
	}

	public Integer selectPassCount(String username){
		Integer count = userRepository.selectPassCount(username);
		return count;
	}

	public void updatePassCount(String username){
		userRepository.updatePassCount(username);
	}

	public void insertPassCount(String username){
		userRepository.insertPassCount(username);
	}

    public void deletePassCount(String username){
        userRepository.deletePassCount(username);
    }

	public void updateUserEnabled(String username){
		userRepository.updateUserEnabled(username);
	}
}
