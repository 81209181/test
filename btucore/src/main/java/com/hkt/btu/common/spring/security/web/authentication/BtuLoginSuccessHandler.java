package com.hkt.btu.common.spring.security.web.authentication;

import com.hkt.btu.common.core.service.BtuAuditTrailService;
import com.hkt.btu.common.spring.security.core.userdetails.BtuUser;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@Service
public class BtuLoginSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {

    @Resource(name = "auditTrailService")
    BtuAuditTrailService auditTrailService;

    public BtuLoginSuccessHandler(){
        super();
        this.setDefaultTargetUrl("/user/");
        this.setAlwaysUseDefaultTargetUrl(true);
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) throws IOException, ServletException {
        // log user login
        BtuUser user = (authentication==null || !(authentication.getPrincipal() instanceof BtuUser) ) ?
                null : (BtuUser) authentication.getPrincipal();

        // set timeout for inactive session
        HttpSession session = httpServletRequest.getSession(false);
        if( session!=null ){
            session.setMaxInactiveInterval(900); // 15 min
        }
        clearAuthenticationAttributes(httpServletRequest);

        // redirect to defaultTargetUrl
        super.onAuthenticationSuccess(httpServletRequest, httpServletResponse, authentication);
    }
}
