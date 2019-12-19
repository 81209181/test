package com.hkt.btu.common.spring.security.exception.ldap;

import org.springframework.security.core.AuthenticationException;

public class LdapNotPermittedLogonException extends AuthenticationException {

    public LdapNotPermittedLogonException(String msg, Throwable t) {
        super(msg, t);
    }

    public LdapNotPermittedLogonException(String msg) {
        super(msg);
    }
}
