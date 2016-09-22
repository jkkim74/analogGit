package com.skplanet.pandora.service;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.ldap.authentication.LdapAuthenticationProvider;
import org.springframework.security.provisioning.JdbcUserDetailsManager;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class CombinedAuthenticationProvider implements AuthenticationProvider {

	private JdbcUserDetailsManager jdbcUserDetailsManager;

	private LdapAuthenticationProvider ldapAuthenticationProvider;

	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		UserDetails userDetails = jdbcUserDetailsManager.loadUserByUsername(authentication.getName());
		if (userDetails == null) {
			throw new UsernameNotFoundException("등록된 사용자가 아닙니다.");
		}

		return ldapAuthenticationProvider.authenticate(authentication);
	}

	@Override
	public boolean supports(Class<?> authentication) {
		return ldapAuthenticationProvider.supports(authentication);
	}

}
