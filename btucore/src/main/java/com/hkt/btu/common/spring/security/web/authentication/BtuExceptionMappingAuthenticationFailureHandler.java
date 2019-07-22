package com.hkt.btu.common.spring.security.web.authentication;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.web.authentication.ExceptionMappingAuthenticationFailureHandler;

import java.util.HashMap;
import java.util.Map;


public class BtuExceptionMappingAuthenticationFailureHandler extends ExceptionMappingAuthenticationFailureHandler {

    public static class LOGIN_ERROR{
        public static final String LOCK = "LOCK";
        public static final String BAD_CREDENTIALS = "BAD";
        public static final String FORBIDDEN = "FORBIDDEN";
        public static final String TIMEOUT = "TIMEOUT";
        public static final String LOGIN = "LOGIN";
    }

    public BtuExceptionMappingAuthenticationFailureHandler(){
        Map<String, String> failureUrlMap = new HashMap<>();
        failureUrlMap.put(CredentialsExpiredException.class.getName(), "/reset-password?init");
        failureUrlMap.put(DisabledException.class.getName(), "/login?error=" + LOGIN_ERROR.LOCK);
        failureUrlMap.put(BadCredentialsException.class.getName(), "/login?error=" + LOGIN_ERROR.BAD_CREDENTIALS);
        super.setExceptionMappings(failureUrlMap);
    }
}
