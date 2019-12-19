package com.hkt.btu.common.spring.security.exception.ldap;

import org.springframework.security.core.AuthenticationException;

public class LdapCredentialsExpiredException extends AuthenticationException {

    public LdapCredentialsExpiredException(String msg, Throwable t) {
        super(msg, t);
    }

    public LdapCredentialsExpiredException(String msg) {
        super(msg);
    }
}
