package com.skplanet.web.security;

import java.io.IOException;
import java.util.Arrays;


import com.skplanet.web.service.UserService;
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
import org.springframework.web.client.ResourceAccessException;
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

    @Autowired
    private UserService userService;

   	@Autowired
	private RestTemplate restTemplate;

	@Value("${idms.url}")
	private String idmsUrl;

	@Value("${idms.id}")
	private String idmsId;

	@Value("${app.enable.idms}")
	private boolean idmsEnabled;

	private ObjectMapper objectMapper = new ObjectMapper();

	@Autowired
	AuthenticationCheck authenticationCheck;
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

			// IDMS에 아이디 등록이 되여 있는지 체크
			authenticateWithIdms(authentication.getName());

            // LDAP에 로그인하기전에 패스워드가 틀렸는지 체크하가 위한 로직 추가 ( 두번 로그인을 하는 부분으로 추후 로직 변경 요망 )
			boolean result = authenticationCheck.getLdapBindAuthentication(authentication.getPrincipal().toString()
                    ,authentication.getCredentials().toString());

            if(result == false){

                Integer cnt = userService.selectPassCount(authentication.getPrincipal().toString());
                if(cnt == null){
                    userService.insertPassCount(authentication.getPrincipal().toString());
                }else{
                    if(cnt >= 4){
                        // 유저 disable
                        userService.deletePassCount(authentication.getPrincipal().toString());
						userService.updateUserEnabled(authentication.getPrincipal().toString());
                        Throwable e = new Throwable();
                        throw new InternalAuthenticationServiceException("로그인에 5회 실패하였습니다. 관리자에게 문의하세요 비활성화 되였습니다", e);
                    }else{
                        userService.updatePassCount(authentication.getPrincipal().toString());
                    }
                }

            }else{
				userService.deletePassCount(authentication.getPrincipal().toString());
			}

            // local test only aa
            if (Arrays.asList(env.getActiveProfiles()).contains("local")) {
                ((UserInfo) userDetails).setEmailAddr("1600328@partner.skcc.com");
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
		if (!idmsEnabled) {
			return;
		}
		
		try {
			String result = restTemplate.getForObject(idmsUrl, String.class, idmsId, username,
					Helper.currentClientIp());

			JsonNode resultAsJson = objectMapper.readValue(result, JsonNode.class);

			String resultCd = resultAsJson.get("RESULT_CD").asText();
			String resultMsg = resultAsJson.get("RESULT_MSG").asText();

			log.info("IDMS RESULT_CD={}, RESULT_MSG={}", resultCd, resultMsg);

			if (!"E0".equals(resultCd) && !"E99".equals(resultCd)) {
				throw new InternalAuthenticationServiceException(resultMsg);
			}
		} catch (IOException | ResourceAccessException e) {
			log.info(e.toString());
			throw new InternalAuthenticationServiceException("IDMS Error", e);
		}
	}

}
