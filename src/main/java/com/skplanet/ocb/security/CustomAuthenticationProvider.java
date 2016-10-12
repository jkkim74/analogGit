package com.skplanet.ocb.security;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
public class CustomAuthenticationProvider implements AuthenticationProvider {

	@Autowired
	private Environment env;

	@Autowired
	private UserDetailsService userDetailsService;

	/*
	 * LDAP 연동 전에 MySql에 사용자 정보가 있는지 확인하여, 있다면 LDAP 연동으로 진행할 수 있도록 NULL을 반환하고
	 * 없다면 예외를 발생시켜 LDAP 연동에 도달하지 못하도록 한다.
	 */
	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		try {
			UserDetails userDetails = userDetailsService.loadUserByUsername(authentication.getName());
			if (userDetails != null && !userDetails.isEnabled()) {
				throw new DisabledException("사용 정지된 사용자입니다.");
			}

			// local test only
			if (Arrays.asList(env.getActiveProfiles()).contains("local")) {
				return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
			}
		} catch (UsernameNotFoundException e) {
			throw new InternalAuthenticationServiceException("등록된 사용자가 아닙니다.", e);
		}
		return null;
	}

	@Override
	public boolean supports(Class<?> authentication) {
		return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
	}

}
