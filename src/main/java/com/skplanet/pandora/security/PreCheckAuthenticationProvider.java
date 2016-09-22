package com.skplanet.pandora.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.stereotype.Component;

@Component
public class PreCheckAuthenticationProvider implements AuthenticationProvider {

	@Autowired
	private JdbcUserDetailsManager jdbcUserDetailsManager;

	/*
	 * LDAP 연동 전에 MySql에 사용자 정보가 있는지 확인하여, 있다면 LDAP 연동으로 진행할 수 있도록 NULL을 반환하고
	 * 없다면 예외를 발생시켜 LDAP 연동에 도달하지 못하도록 한다.
	 */
	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		UserDetails userDetails = jdbcUserDetailsManager.loadUserByUsername(authentication.getName());
		if (userDetails == null) {
			throw new UsernameNotFoundException("등록된 사용자가 아닙니다.");
		} else if (!userDetails.isEnabled()) {
			throw new DisabledException("사용 정지된 사용자입니다.");
		}
		return null;
	}

	@Override
	public boolean supports(Class<?> authentication) {
		return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
	}

}
