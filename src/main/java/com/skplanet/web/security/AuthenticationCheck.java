package com.skplanet.web.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.core.support.LdapContextSource;
import org.springframework.ldap.filter.EqualsFilter;
import org.springframework.ldap.filter.Filter;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class AuthenticationCheck {

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

    public boolean getLdapBindAuthentication(String userName, String userPassword) {
        boolean authed = false;
        try {
            LdapContextSource contextSource = new LdapContextSource();
            contextSource.setUrl(ldapUrl);
            contextSource.setBase(ldapBaseDn);
            contextSource.setUserDn(ldapUsername);
            contextSource.setPassword(ldapPassword);
            contextSource.afterPropertiesSet();

            LdapTemplate ldapTemplate = new LdapTemplate(contextSource);
            ldapTemplate.afterPropertiesSet();

            // Perform the authentication.
            Filter filter = new EqualsFilter("sAMAccountName", userName);

            authed = ldapTemplate.authenticate("", filter.encode(), userPassword);

        } catch (Exception e) {
            throw new InternalAuthenticationServiceException("로그인에 실패하였습니다.", e);
        }
        return authed;
    }
}
