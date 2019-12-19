package com.hkt.btu.common.spring.security.exception.ldap;

import org.springframework.security.core.AuthenticationException;

public class LdapBadCredentialsException extends AuthenticationException {
    public LdapBadCredentialsException(String msg, Throwable t) {
        super(msg, t);
    }

    public LdapBadCredentialsException(String msg) {
        super(msg);
    }
}
