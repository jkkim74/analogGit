package com.skplanet.web.configuration;

import javax.sql.DataSource;

import com.skplanet.web.security.CustomAuthenticationProvider;
import com.skplanet.web.security.CustomUserDetailsContextMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.ldap.core.support.LdapContextSource;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.ldap.authentication.NullLdapAuthoritiesPopulator;
import org.springframework.security.ldap.search.FilterBasedLdapUserSearch;
import org.springframework.security.ldap.search.LdapUserSearch;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JdbcTokenStore;



@Configuration
public class SecurityConfig {

	@Configuration
	@EnableGlobalMethodSecurity(jsr250Enabled = true)
	@Order(SecurityProperties.ACCESS_OVERRIDE_ORDER)
	protected static class WebSecurityConfig extends WebSecurityConfigurerAdapter {

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

		@Value("${ldap.userSearchFilter}")
		private String ldapUserSearchFilter;

       	@Autowired
		public void configureGlobal(AuthenticationManagerBuilder auth, UserDetailsService userDetailsService,
                   CustomUserDetailsContextMapper userDetailsContextMapper,
            CustomAuthenticationProvider authenticationProvider) throws Exception {

            auth.authenticationProvider(authenticationProvider);

            auth.ldapAuthentication().userSearchFilter(ldapUserSearchFilter).userSearchBase(ldapBaseDn)
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
		public LdapUserSearch ldapUserSearch() {
			return new FilterBasedLdapUserSearch(ldapBaseDn, ldapUserSearchFilter, ldapContextSource());
		}

	}

	@Configuration
	@EnableAuthorizationServer
	@EnableResourceServer
	protected static class OAuthServerConfiguration {

		@Autowired
		@Qualifier("mysqlDataSource")
		private DataSource mysqlDataSource;

		@Bean
		public TokenStore jdbcTokenStore() {
			return new JdbcTokenStore(mysqlDataSource);
		}

	}


}
