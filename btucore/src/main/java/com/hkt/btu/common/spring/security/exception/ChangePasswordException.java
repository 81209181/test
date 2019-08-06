package com.hkt.btu.common.spring.security.exception;

import org.springframework.security.core.AuthenticationException;

public class ChangePasswordException extends AuthenticationException {
    public ChangePasswordException(String msg, Throwable t) {
        super(msg, t);
    }

    public ChangePasswordException(String msg) {
        super(msg);
    }
}
