package com.hkt.btu.common.spring.security.exception;

import org.springframework.security.core.AuthenticationException;

public class NotPermittedLogonException extends AuthenticationException {

    public NotPermittedLogonException(String msg, Throwable t) {
        super(msg, t);
    }

    public NotPermittedLogonException(String msg) {
        super(msg);
    }
}
