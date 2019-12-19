package com.hkt.btu.common.spring.security.exception.ldap;

import org.springframework.security.core.AuthenticationException;

public class LdapLockedException extends AuthenticationException {

    public LdapLockedException(String msg, Throwable t) {
        super(msg, t);
    }

    public LdapLockedException(String msg) {
        super(msg);
    }
}
