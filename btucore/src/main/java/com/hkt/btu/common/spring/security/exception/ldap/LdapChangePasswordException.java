package com.hkt.btu.common.spring.security.exception.ldap;

import org.springframework.security.core.AuthenticationException;

public class LdapChangePasswordException extends AuthenticationException {
    public LdapChangePasswordException(String msg, Throwable t) {
        super(msg, t);
    }

    public LdapChangePasswordException(String msg) {
        super(msg);
    }
}
