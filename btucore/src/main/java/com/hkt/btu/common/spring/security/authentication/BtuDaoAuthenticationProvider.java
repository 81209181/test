package com.hkt.btu.common.spring.security.authentication;

import com.hkt.btu.common.core.service.BtuUserService;
import com.hkt.btu.common.core.service.bean.BtuUserBean;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetailsService;

import javax.annotation.Resource;

public class BtuDaoAuthenticationProvider extends DaoAuthenticationProvider {

    private static final Logger LOG = LogManager.getLogger(BtuDaoAuthenticationProvider.class);

    @Resource(name = "userService")
    BtuUserService btuUserService;

    @Resource
    LdapAuthenticationProvider ldapAuthenticationProvider;

    @Resource(name = "customDbDaoAuthenticationProvider")
    DbDaoAuthenticationProvider dbDaoAuthenticationProvider;

    @Resource(name = "customBtuUserDetailsService")
    @Qualifier("userDetailsService")
    @Override
    public void setUserDetailsService(UserDetailsService userDetailService) {
        super.setUserDetailsService(userDetailService);
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        var loginName = authentication.getName();
        BtuUserBean userBean = null;
        try {
            userBean = btuUserService.getUserBeanByUsername(loginName);
        } catch (Exception e) {
            LOG.error("User Not Found!");
        }
        if (userBean == null) {
            throw new BadCredentialsException("User Not Found!");
        } else {
            if (loginName.contains("@")) {
                return dbDaoAuthenticationProvider.authenticate(authentication);
            } else {
                return ldapAuthenticationProvider.btuAuth(authentication, userBean, userBean.getLdapDomain());
            }
        }
    }
}
