package com.hkt.btu.common.spring.security.core.userdetails;


import com.hkt.btu.common.core.service.BtuSiteConfigService;
import com.hkt.btu.common.core.service.BtuUserService;
import com.hkt.btu.common.core.service.bean.BtuSiteConfigBean;
import com.hkt.btu.common.core.service.bean.BtuUserBean;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import javax.annotation.Resource;
import java.time.LocalDateTime;


public class BtuUserDetailsServiceImpl implements UserDetailsService {
    private static final Logger LOG = LogManager.getLogger(BtuUserDetailsServiceImpl.class);

    @Resource(name = "userService")
    BtuUserService userService;

    @Resource (name = "siteConfigService")
    BtuSiteConfigService siteConfigService;



    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        final LocalDateTime NOW = LocalDateTime.now();

        // get user
        BtuUserBean userBean = userService.getUserBeanByUsername(username);

        if (userBean == null) {
            LOG.warn("User " + username + " not found");
            throw new UsernameNotFoundException("User " + username + " not found");
        }

        // check account enable
        boolean enabled = userService.isEnabled(userBean);

        // check account password expired
        BtuSiteConfigBean btuSiteConfigBean = siteConfigService.getSiteConfigBean();
        Integer passwordLifespanInDay = btuSiteConfigBean.getPasswordLifespanInDay();
        LocalDateTime passwordModifydate = userBean.getPasswordModifydate();
        LocalDateTime passwordExpiryDate = passwordModifydate==null ? null : passwordModifydate.plusDays(passwordLifespanInDay);
        boolean credentialsNonExpired = passwordExpiryDate!=null && NOW.isBefore(passwordExpiryDate);

        // check account locked
        boolean accountNonLocked = userService.isNonLocked(userBean);

        // create spring security user
        return BtuUser.of(
                userBean.getUsername(),
                userBean.getPassword(),
                enabled,
                true,
                credentialsNonExpired,
                accountNonLocked,
                userBean.getAuthorities(),
                userBean);
    }

}
