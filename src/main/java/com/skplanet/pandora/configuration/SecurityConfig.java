package com.skplanet.pandora.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.ldap.authentication.NullLdapAuthoritiesPopulator;
import org.springframework.security.ldap.userdetails.LdapAuthoritiesPopulator;

@Configuration
@Order(SecurityProperties.ACCESS_OVERRIDE_ORDER)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	@Value("${ldap.url}")
	private String url;

	@Value("${ldap.baseDn}")
	private String baseDn;

	@Value("${ldap.username}")
	private String username;

	@Value("${ldap.password}")
	private String password;

	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {

		auth.ldapAuthentication().userSearchFilter("(&(objectClass=*)(CN={0}))").userSearchBase(baseDn)
				.ldapAuthoritiesPopulator(ldapAuthoritiesPopulator()).contextSource().url(url).managerDn(username)
				.managerPassword(password);

		// auth.authenticationProvider(activeDirectoryLdapAuthenticationProvider());

		// auth.jdbcAuthentication().dataSource(mysqlDataSource).withDefaultSchema();
	}

	@Bean
	public LdapAuthoritiesPopulator ldapAuthoritiesPopulator() {
		return new NullLdapAuthoritiesPopulator();
	}

	// @Bean
	// public ActiveDirectoryLdapAuthenticationProvider
	// activeDirectoryLdapAuthenticationProvider() {
	// ActiveDirectoryLdapAuthenticationProvider provider = new
	// ActiveDirectoryLdapAuthenticationProvider("skp.ad",
	// "ldap://10.40.29.172:389"); /// OU=Person,DC=SKP,DC=AD
	// provider.setConvertSubErrorCodesToExceptions(true);
	// provider.setUseAuthenticationRequestCredentials(true);
	// provider.setSearchFilter("(&(objectClass=*)(CN={0}))");
	// return provider;
	// }

	// @Bean
	// public LdapTemplate ldapTemplate() {
	// LdapTemplate template = new LdapTemplate();
	// template.setContextSource(contextSource(null, null, null, null));
	// return template;
	// }

	// @Bean
	// public LdapContextSource contextSource(@Value("${ldap.url}") String url,
	// @Value("${ldap.baseDn}") String baseDn,
	// @Value("${ldap.username}") String username, @Value("${ldap.password}")
	// String password) {
	// LdapContextSource contextSource = new LdapContextSource();
	// contextSource.setUrl(url);
	// contextSource.setBase(baseDn);
	// contextSource.setUserDn(username);
	// contextSource.setPassword(password);
	// return contextSource;
	// }

}
