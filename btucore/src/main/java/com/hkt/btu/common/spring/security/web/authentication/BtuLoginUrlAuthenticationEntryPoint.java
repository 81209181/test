package com.hkt.btu.common.spring.security.web.authentication;

import com.hkt.btu.common.core.service.BtuSessionService;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

// ref: https://stackoverflow.com/questions/23901950/spring-security-ajax-session-timeout-issue
public class BtuLoginUrlAuthenticationEntryPoint extends LoginUrlAuthenticationEntryPoint {

    @Resource(name = "sessionService")
    private BtuSessionService btuSessionService;

    public BtuLoginUrlAuthenticationEntryPoint() {
        super("/login?error=" + BtuExceptionMappingAuthenticationFailureHandler.LOGIN_ERROR.LOGIN);
    }

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException)
            throws IOException, ServletException {
        boolean isHandled = btuSessionService.handleInvalidSession(request, response, authException);
        if(isHandled){
            return;
        }

        super.commence(request, response, authException);
    }
}
