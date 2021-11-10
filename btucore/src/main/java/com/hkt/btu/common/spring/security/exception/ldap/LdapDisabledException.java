package com.hkt.btu.common.spring.security.exception.ldap;

import org.springframework.security.core.AuthenticationException;

public class LdapDisabledException extends AuthenticationException {

    public LdapDisabledException(String msg, Throwable t) {
        super(msg, t);
    }

    public LdapDisabledException(String msg) {
        super(msg);
    }
}
