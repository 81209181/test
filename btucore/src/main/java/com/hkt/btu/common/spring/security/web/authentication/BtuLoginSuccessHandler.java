package com.hkt.btu.common.spring.security.web.authentication;

import com.hkt.btu.common.core.service.BtuAuditTrailService;
import com.hkt.btu.common.core.service.BtuUserService;
import com.hkt.btu.common.core.service.bean.BtuUserBean;
import com.hkt.btu.common.spring.security.core.userdetails.BtuUser;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

//@Service
public class BtuLoginSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {

    @Resource(name = "userService")
    BtuUserService btuUserService;

    @Resource(name = "auditTrailService")
    BtuAuditTrailService auditTrailService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) throws IOException, ServletException {
        // log user login
        BtuUser user = (authentication == null || !(authentication.getPrincipal() instanceof BtuUser)) ?
                null : (BtuUser) authentication.getPrincipal();

        BtuUserBean userBean = user.getUserBean();

        btuUserService.verifyLdapUser(user, userBean);

        // erase user ldapPassword
        user.setLdapPassword(null);

        // set timeout for inactive session
        HttpSession session = httpServletRequest.getSession(false);
        if (session != null) {
            // 15 min
            session.setMaxInactiveInterval(900);
        }
        clearAuthenticationAttributes(httpServletRequest);

        btuUserService.setLogonPage(this);

        super.onAuthenticationSuccess(httpServletRequest, httpServletResponse, authentication);
    }
}
