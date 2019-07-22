package com.hkt.btu.common.spring.security.web.authentication;

import com.hkt.btu.common.spring.security.web.authentication.BtuLoginUrlAuthenticationEntryPoint.LOGIN_ERROR;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.web.authentication.ExceptionMappingAuthenticationFailureHandler;

import java.util.HashMap;
import java.util.Map;


public class BtuExceptionMappingAuthenticationFailureHandler extends ExceptionMappingAuthenticationFailureHandler {

    public BtuExceptionMappingAuthenticationFailureHandler(){
        Map<String, String> failureUrlMap = new HashMap<>();
        failureUrlMap.put(CredentialsExpiredException.class.getName(), "/reset-password?init");
        failureUrlMap.put(DisabledException.class.getName(), LOGIN_ERROR.LOCK.getUri());
        failureUrlMap.put(BadCredentialsException.class.getName(), LOGIN_ERROR.BAD_CREDENTIALS.getUri());
        super.setExceptionMappings(failureUrlMap);
    }

}
