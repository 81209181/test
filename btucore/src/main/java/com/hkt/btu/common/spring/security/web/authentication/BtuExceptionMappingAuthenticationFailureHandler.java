package com.hkt.btu.common.spring.security.web.authentication;

import com.hkt.btu.common.spring.security.exception.ldap.*;
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
        failureUrlMap.put(LockedException.class.getName(), LOGIN_ERROR.LOCK.getUri());
        failureUrlMap.put(DisabledException.class.getName(), LOGIN_ERROR.DISABLED.getUri());
        failureUrlMap.put(BadCredentialsException.class.getName(), LOGIN_ERROR.BAD_CREDENTIALS.getUri());

        // LDAP exception
        failureUrlMap.put(LdapBadCredentialsException.class.getName(), LOGIN_ERROR.LDAP_BAD_CREDENTIALS.getUri());
        failureUrlMap.put(LdapChangePasswordException.class.getName(), LOGIN_ERROR.CHANGE_PWD.getUri());
        failureUrlMap.put(LdapCredentialsExpiredException.class.getName(), LOGIN_ERROR.LDAP_CREDENTIALS_EXPIRED.getUri());
        failureUrlMap.put(LdapDisabledException.class.getName(), LOGIN_ERROR.LDAP_DISABLED.getUri());
        failureUrlMap.put(LdapLockedException.class.getName(), LOGIN_ERROR.LDAP_LOCK.getUri());
        failureUrlMap.put(LdapNotPermittedLogonException.class.getName(), LOGIN_ERROR.NOT_PERMITTED.getUri());

        super.setExceptionMappings(failureUrlMap);
    }

}
