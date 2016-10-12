package com.skplanet.ocb.configuration;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.core.support.LdapContextSource;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.ldap.authentication.NullLdapAuthoritiesPopulator;

import com.skplanet.ocb.security.CustomAuthenticationProvider;
import com.skplanet.ocb.security.CustomUserDetailsContextMapper;

@Configuration
@Order(SecurityProperties.ACCESS_OVERRIDE_ORDER)
@EnableGlobalMethodSecurity(jsr250Enabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	public static final String LDAP_USER_SEARCH_FILTER = "(&(objectClass=*)(CN={0}))";

	@Autowired
	@Qualifier("mysqlDataSource")
	private DataSource mysqlDataSource;

	@Value("${ldap.url}")
	private String ldapUrl;

	@Value("${ldap.baseDn}")
	private String ldapBaseDn;

	@Value("${ldap.username}")
	private String ldapUsername;

	@Value("${ldap.password}")
	private String ldapPassword;

	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth, UserDetailsService userDetailsService,
			CustomUserDetailsContextMapper userDetailsContextMapper,
			CustomAuthenticationProvider authenticationProvider) throws Exception {

		auth.authenticationProvider(authenticationProvider);

		auth.ldapAuthentication().userSearchFilter(LDAP_USER_SEARCH_FILTER).userSearchBase(ldapBaseDn)
				.ldapAuthoritiesPopulator(new NullLdapAuthoritiesPopulator())
				.userDetailsContextMapper(userDetailsContextMapper).contextSource(ldapContextSource());

	}

	@Bean
	public LdapContextSource ldapContextSource() {
		LdapContextSource contextSource = new LdapContextSource();
		contextSource.setUrl(ldapUrl);
		contextSource.setUserDn(ldapUsername);
		contextSource.setPassword(ldapPassword);
		return contextSource;
	}

	// 별도로 사용자 관리 시 사용
	@Bean
	public LdapTemplate ldapTemplate() {
		LdapTemplate template = new LdapTemplate();
		template.setContextSource(ldapContextSource());
		return template;
	}

}
