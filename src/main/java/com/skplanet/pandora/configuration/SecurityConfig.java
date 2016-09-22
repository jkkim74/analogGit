package com.skplanet.pandora.configuration;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.ldap.core.support.BaseLdapPathContextSource;
import org.springframework.ldap.core.support.LdapContextSource;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.authority.mapping.SimpleAuthorityMapper;
import org.springframework.security.ldap.authentication.BindAuthenticator;
import org.springframework.security.ldap.authentication.LdapAuthenticationProvider;
import org.springframework.security.ldap.authentication.NullLdapAuthoritiesPopulator;
import org.springframework.security.ldap.authentication.UserDetailsServiceLdapAuthoritiesPopulator;
import org.springframework.security.ldap.search.FilterBasedLdapUserSearch;
import org.springframework.security.ldap.userdetails.LdapAuthoritiesPopulator;
import org.springframework.security.provisioning.JdbcUserDetailsManager;

import com.skplanet.pandora.service.CombinedAuthenticationProvider;

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
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {

		auth.authenticationProvider(combinedAuthenticationProvider());

//		auth.jdbcAuthentication().dataSource(mysqlDataSource);// .withUser("user").roles("USER");

//		auth.ldapAuthentication().userSearchFilter("(&(objectClass=*)(CN={0}))").userSearchBase(ldapBaseDn)
//				.ldapAuthoritiesPopulator(ldapAuthoritiesPopulator()).contextSource().url(ldapUrl)
//				.managerDn(ldapUsername).managerPassword(ldapPassword);

		// auth.authenticationProvider(activeDirectoryLdapAuthenticationProvider());

	}

	@Bean
	public LdapAuthoritiesPopulator ldapAuthoritiesPopulator() {
		return new NullLdapAuthoritiesPopulator();
	}

	@Bean
	public CombinedAuthenticationProvider combinedAuthenticationProvider() {
		return new CombinedAuthenticationProvider(jdbcUserDetailsManager(), ldapAuthenticationProvider());
	}

	@Bean
	public JdbcUserDetailsManager jdbcUserDetailsManager() {
		JdbcUserDetailsManager userDetailsManager = new JdbcUserDetailsManager();
		userDetailsManager.setDataSource(mysqlDataSource);
		userDetailsManager.afterPropertiesSet();
		return userDetailsManager;
	}

	@Bean
	@Autowired
	public BaseLdapPathContextSource ldapContextSource() {
		LdapContextSource contextSource = new LdapContextSource();
		contextSource.setUrl(ldapUrl);
		contextSource.setBase(ldapBaseDn);
		contextSource.setUserDn(ldapUsername);
		contextSource.setPassword(ldapPassword);
		return contextSource;
	}

	@Bean
	public LdapAuthenticationProvider ldapAuthenticationProvider()  {
		BaseLdapPathContextSource contextSource = ldapContextSource();

		BindAuthenticator authenticator = new BindAuthenticator(contextSource);
		authenticator.setUserSearch(new FilterBasedLdapUserSearch("", "(&(objectClass=*)(CN={0}))", contextSource));

		LdapAuthoritiesPopulator authoritiesPopulator = new UserDetailsServiceLdapAuthoritiesPopulator(
				jdbcUserDetailsManager());

		LdapAuthenticationProvider provider = new LdapAuthenticationProvider(authenticator, authoritiesPopulator);

		SimpleAuthorityMapper simpleAuthorityMapper = new SimpleAuthorityMapper();

		provider.setAuthoritiesMapper(simpleAuthorityMapper);

		return provider;
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

}
