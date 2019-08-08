package com.hkt.btu.common.spring.security.authentication;


import com.hkt.btu.common.core.service.BtuAuditTrailService;
import com.hkt.btu.common.core.service.BtuSiteConfigService;
import com.hkt.btu.common.core.service.BtuUserService;
import com.hkt.btu.common.core.service.bean.BtuSiteConfigBean;
import com.hkt.btu.common.core.service.bean.BtuUserBean;
import com.hkt.btu.common.spring.security.core.userdetails.BtuUser;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;

import javax.annotation.Resource;

public class DbDaoAuthenticationProvider extends DaoAuthenticationProvider {
    private static final Logger LOG = LogManager.getLogger(DbDaoAuthenticationProvider.class);

    @Resource(name = "customBtuUserDetailsService")
    @Qualifier("userDetailsService")
    @Override
    public void setUserDetailsService(UserDetailsService userDetailsService) {
        super.setUserDetailsService(userDetailsService);
    }

    @Resource (name = "siteConfigService")
    BtuSiteConfigService siteConfigService;

    @Resource(name = "userService")
    BtuUserService userService;

    @Resource(name = "auditTrailService")
    BtuAuditTrailService auditTrailService;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        try {
            Authentication auth = super.authenticate(authentication);
            User user = (User) auth.getPrincipal();

            // if reach here, means login success, else exception will be thrown
            // reset the user login attempts
            userService.resetLoginTriedByUsername(user.getUsername());
            if(user instanceof BtuUser){
                BtuUser btuUser = (BtuUser) user;
                auditTrailService.insertLoginAuditTrail(btuUser);
                BtuUserBean btuUserBean = btuUser.getUserBean();
                if(btuUserBean!=null){
                    btuUserBean.setLoginTried(0);
                }
            }

            return auth;

        } catch (BadCredentialsException e) {
            BtuUserBean btuUserBean = null;
            if(authentication!=null && authentication.getPrincipal()!=null){
                btuUserBean = userService.getUserBeanByUsername(authentication.getPrincipal().toString());
            }

            if (btuUserBean!=null) {
                BtuSiteConfigBean siteConfigBean = siteConfigService.getSiteConfigBean();
                Integer loginTriedLimit = siteConfigBean.getLoginTriedLimit();
                int currentTried = btuUserBean.getLoginTried() + 1;

                // update login tried
                userService.addLoginTriedByUsername( btuUserBean.getUsername() );
                LOG.warn("User " + btuUserBean.getUsername() + " login tried: " + currentTried + "/" + loginTriedLimit + ".");

                // lock user, if login tried limit reaches
                if ( currentTried > loginTriedLimit){
                    userService.lockUserByUsername( btuUserBean.getUsername() );
                    LOG.warn("User " + btuUserBean.getUsername() + " reaches " + loginTriedLimit + " login tried limit. User locked.");
                    throw new LockedException("User is locked!");
                }
            }

            throw e;
        } catch (LockedException e) {
            LOG.warn("User " + authentication.getPrincipal().toString() + " is locked!");
            throw e;
        }

    }
}
