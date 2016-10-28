package com.skplanet.web.security;

import java.io.IOException;
import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.skplanet.web.util.Helper;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class CustomAuthenticationProvider implements AuthenticationProvider {

	@Autowired
	private Environment env;

	@Autowired
	private UserDetailsService userDetailsService;

	@Value("${idms.url}")
	private String idmsUrl;

	@Value("${idms.id}")
	private String idmsId;

	@Autowired
	private RestTemplate restTemplate;

	private ObjectMapper objectMapper = new ObjectMapper();

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

			authenticateWithIdms(authentication.getName());

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

	private void authenticateWithIdms(String username) {
		try {
			String result = restTemplate.getForObject(idmsUrl, String.class, idmsId, username,
					Helper.currentClientIp());

			JsonNode resultAsJson = objectMapper.readValue(result, JsonNode.class);

			String resultCd = resultAsJson.get("RESULT_CD").asText();
			String resultMsg = resultAsJson.get("RESULT_MSG").asText();

			log.info("IDMS RESULT_CD={}, RESULT_MSG={}", resultCd, resultMsg);

			if (!"E0".equals(resultCd) && !"E99".equals(resultCd)) {
				// throw new InternalAuthenticationServiceException(resultMsg);
			}
		} catch (IOException e) {
			log.info(e.toString());
			// throw new InternalAuthenticationServiceException("IDMS Error", e);
		}
	}

}
