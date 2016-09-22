package com.skplanet.pandora.configuration;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.ldap.authentication.UserDetailsServiceLdapAuthoritiesPopulator;
import org.springframework.security.provisioning.JdbcUserDetailsManager;

import com.skplanet.pandora.security.CombinedAuthenticationProvider;

@Configuration
@Order(SecurityProperties.ACCESS_OVERRIDE_ORDER)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

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
			CombinedAuthenticationProvider combinedAuthenticationProvider) throws Exception {

		auth.authenticationProvider(combinedAuthenticationProvider);

		auth.ldapAuthentication().userSearchFilter("(&(objectClass=*)(CN={0}))").userSearchBase(ldapBaseDn)
				.ldapAuthoritiesPopulator(new UserDetailsServiceLdapAuthoritiesPopulator(userDetailsService))
				.contextSource().url(ldapUrl).managerDn(ldapUsername).managerPassword(ldapPassword);

	}

	@Bean
	public JdbcUserDetailsManager jdbcUserDetailsManager() {
		JdbcUserDetailsManager userDetailsManager = new JdbcUserDetailsManager();
		userDetailsManager.setDataSource(mysqlDataSource);
		userDetailsManager.afterPropertiesSet();
		return userDetailsManager;
	}

}
