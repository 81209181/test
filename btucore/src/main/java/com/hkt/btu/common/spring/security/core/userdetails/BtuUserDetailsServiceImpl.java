package com.hkt.btu.common.spring.security.core.userdetails;


import com.hkt.btu.common.core.service.BtuConfigParamService;
import com.hkt.btu.common.core.service.BtuSiteConfigService;
import com.hkt.btu.common.core.service.BtuUserService;
import com.hkt.btu.common.core.service.bean.BtuSiteConfigBean;
import com.hkt.btu.common.core.service.bean.BtuUserBean;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;


public class BtuUserDetailsServiceImpl implements BtuUserDetailsService  {
    private static final Logger LOG = LogManager.getLogger(BtuUserDetailsServiceImpl.class);

    @Resource(name = "userService")
    BtuUserService userService;

    @Resource(name = "siteConfigService")
    BtuSiteConfigService siteConfigService;

    @Resource(name = "configParamService")
    BtuConfigParamService configParamService;

    @Override
    public UserDetails loadVirtualUserByJobName(String jobName, Collection<? extends GrantedAuthority> authorities) {
        final int MAX_USER_ID_LENGTH = 10;
        final BtuSiteConfigBean siteConfigBean = siteConfigService.getSiteConfigBean();
        final String VIRTUAL_USER_ID = StringUtils.length(jobName) > MAX_USER_ID_LENGTH ?
                StringUtils.substring(jobName, 0 , MAX_USER_ID_LENGTH) : jobName;
        final String VIRTUAL_USER_PWD = UUID.randomUUID().toString();

        // transform
        Set<GrantedAuthority> grantedAuthSet = CollectionUtils.isEmpty(authorities) ? Set.of() : new HashSet<>(authorities);

        // build virtual user
        BtuUserBean userBean = new BtuUserBean();
        userBean.setUserId(VIRTUAL_USER_ID);
        userBean.setPassword(VIRTUAL_USER_PWD);
        userBean.setUsername(jobName);
        userBean.setEmail(siteConfigBean.getSystemSupportEmail());
        userBean.setAuthorities(grantedAuthSet);

        // create spring security user
        return BtuUser.of(
                userBean.getUserId(),
                userBean.getPassword(),
                true,
                true,
                true,
                true,
                userBean.getAuthorities(),
                userBean);
    }


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
        // User maybe not have email.
        boolean credentialsNonExpired = true;
        if (StringUtils.isNotEmpty(userBean.getEmail())) {
            BtuSiteConfigBean btuSiteConfigBean = siteConfigService.getSiteConfigBean();
            Integer passwordLifespanInDay = btuSiteConfigBean.getPasswordLifespanInDay();
            LocalDateTime passwordModifydate = userBean.getPasswordModifydate();
            LocalDateTime passwordExpiryDate = passwordModifydate == null ? null : passwordModifydate.plusDays(passwordLifespanInDay);
            credentialsNonExpired = passwordExpiryDate != null && NOW.isBefore(passwordExpiryDate);
        }
        // check account locked
        boolean accountNonLocked = userService.isNonLocked(userBean);

        // create spring security user
        return BtuUser.of(
                userBean.getUserId(),
                userBean.getPassword(),
                enabled,
                true,
                credentialsNonExpired,
                accountNonLocked,
                userBean.getAuthorities(),
                userBean);
    }
}
