package com.hkt.btu.common.spring.security.web.authentication.logout;

import com.hkt.btu.common.core.service.BtuAuditTrailService;
import com.hkt.btu.common.spring.security.core.userdetails.BtuUser;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.SimpleUrlLogoutSuccessHandler;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


public class BtuLogoutSuccessHandler extends SimpleUrlLogoutSuccessHandler {

    @Resource(name = "auditTrailService")
    BtuAuditTrailService auditTrailService;

    public BtuLogoutSuccessHandler(){
        super();
        super.setDefaultTargetUrl("/login?logout");
    }

    @Override
    public void onLogoutSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) throws IOException, ServletException {
        // log user logout
        BtuUser user = (authentication==null || !(authentication.getPrincipal() instanceof BtuUser) ) ?
                null : (BtuUser) authentication.getPrincipal();

        // add audit trail
        auditTrailService.insertLogoutAuditTrail(user);

        // redirect to defaultTargetUrl
        super.onLogoutSuccess(httpServletRequest, httpServletResponse, authentication);
    }
}
