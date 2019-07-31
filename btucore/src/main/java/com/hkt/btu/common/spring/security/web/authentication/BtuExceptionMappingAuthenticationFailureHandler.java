package com.hkt.btu.common.spring.security.web.authentication;

import com.hkt.btu.common.spring.security.exception.ChangePasswordException;
import com.hkt.btu.common.spring.security.exception.NotPermittedLogonException;
import com.hkt.btu.common.spring.security.web.authentication.BtuLoginUrlAuthenticationEntryPoint.LOGIN_ERROR;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.web.authentication.ExceptionMappingAuthenticationFailureHandler;

import java.util.HashMap;
import java.util.Map;


public class BtuExceptionMappingAuthenticationFailureHandler extends ExceptionMappingAuthenticationFailureHandler {

    public BtuExceptionMappingAuthenticationFailureHandler() {
        Map<String, String> failureUrlMap = new HashMap<>();
        failureUrlMap.put(CredentialsExpiredException.class.getName(), "/reset-password?init");
        failureUrlMap.put(DisabledException.class.getName(), LOGIN_ERROR.DISABLED.getUri());
        failureUrlMap.put(BadCredentialsException.class.getName(), LOGIN_ERROR.BAD_CREDENTIALS.getUri());
        failureUrlMap.put(NotPermittedLogonException.class.getName(), LOGIN_ERROR.NOT_PERMITTED.getUri());
        failureUrlMap.put(ChangePasswordException.class.getName(), LOGIN_ERROR.CHANGE_PWD.getUri());
        failureUrlMap.put(LockedException.class.getName(), LOGIN_ERROR.LOCK.getUri());
        super.setExceptionMappings(failureUrlMap);
    }

}
