package com.skplanet.web.security;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ldap.core.DirContextAdapter;
import org.springframework.ldap.core.DirContextOperations;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.ldap.userdetails.UserDetailsContextMapper;
import org.springframework.stereotype.Component;

@Component
public class CustomUserDetailsContextMapper implements UserDetailsContextMapper {

	@Autowired
	private UserDetailsService userDetailsService;

	@Override
	public UserDetails mapUserFromContext(DirContextOperations ctx, String username,
			Collection<? extends GrantedAuthority> authorities) {

		UserInfo userInfo = (UserInfo) userDetailsService.loadUserByUsername(username);

		userInfo.setFullname(ctx.getStringAttribute("exADKoreanName"));

		String email = ctx.getStringAttribute("mail");
		if (email == null || email.isEmpty()) {
			email = ctx.getStringAttribute("exADExternalMailAddress");
		}
		userInfo.setEmailAddr(email);

		return userInfo;
	}

	@Override
	public void mapUserToContext(UserDetails user, DirContextAdapter ctx) {
		throw new UnsupportedOperationException();
	}

}
