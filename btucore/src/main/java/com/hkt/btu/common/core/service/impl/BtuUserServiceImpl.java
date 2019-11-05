package com.hkt.btu.common.core.service.impl;

import com.hkt.btu.common.core.exception.UserNotFoundException;
import com.hkt.btu.common.core.service.BtuLdapService;
import com.hkt.btu.common.core.service.BtuUserService;
import com.hkt.btu.common.core.service.bean.BtuLdapBean;
import com.hkt.btu.common.core.service.bean.BtuUserBean;
import com.hkt.btu.common.spring.security.core.userdetails.BtuUser;
import com.hkt.btu.common.spring.security.web.authentication.BtuLoginSuccessHandler;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.annotation.Resource;
import javax.naming.NamingException;
import java.util.*;

/**
 * User Service Implementation for usage in Spring Security
 */
public class BtuUserServiceImpl implements BtuUserService {
    private static final Logger LOG = LogManager.getLogger(BtuUserService.class);

    @Resource(name = "btuPasswordEncoder")
    BCryptPasswordEncoder btuPasswordEncoder;

    @Resource(name = "ldapService")
    BtuLdapService ldapService;


    public BtuUser getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if( authentication!=null && authentication.getPrincipal() instanceof BtuUser){
            return (BtuUser) authentication.getPrincipal();
        }

        LOG.warn("Current user bean not found.");
        return null;
    }


    public BtuUserBean getCurrentUserBean() {
        BtuUser btuUser = this.getCurrentUser();
        if (btuUser==null || btuUser.getUserBean()==null){
            return null;
        }
        return btuUser.getUserBean();
    }


    public BtuUserBean getUserBeanByUsername(String username) {
        LOG.warn("DEMO ONLY IMPLEMENTATION! Please override and implement by DI.");

        if (StringUtils.equals(username, "admin")){
            BtuUserBean btuUserBean = new BtuUserBean();
            btuUserBean.setUsername(username);
            btuUserBean.setPassword( btuPasswordEncoder.encode("admin") );

            Set<GrantedAuthority> grantedAuthSet = new HashSet<>();
            grantedAuthSet.add(new SimpleGrantedAuthority("ADMIN"));
            grantedAuthSet.add(new SimpleGrantedAuthority("USER"));
            btuUserBean.setAuthorities(grantedAuthSet);

            return btuUserBean;
        } else if (StringUtils.equals(username, "user1")){

            BtuUserBean btuUserBean = new BtuUserBean();
            btuUserBean.setUsername(username);
            btuUserBean.setPassword( btuPasswordEncoder.encode("user1") );

            Set<GrantedAuthority> grantedAuthSet = new HashSet<>();
            grantedAuthSet.add(new SimpleGrantedAuthority("USER"));
            btuUserBean.setAuthorities(grantedAuthSet);

            return btuUserBean;
        }

        return null;
    }

    public void resetLoginTriedByUsername(String username) {
    }

    public void addLoginTriedByUsername(String username) {
    }

    public void lockUserByUsername(String username) {
    }

    public void activateUserByUsername(String username) {
    }

    @Override
    public BtuUserBean verifyLdapUser(BtuUser user, BtuUserBean userDetailBean){
        try {
            BtuLdapBean ldapInfo = ldapService.getBtuLdapBean(userDetailBean.getLdapDomain());
            BtuUserBean btuUserBean = ldapService.searchUser(ldapInfo, userDetailBean.getUserId(), user.getLdapPassword(), userDetailBean.getUserId());
            return btuUserBean;
        } catch (NamingException e) {
            LOG.warn("User" + userDetailBean.getUserId() + "not found");
            throw new UserNotFoundException();
        }
    }


    public boolean isEnabled(BtuUserBean btuUserBean) {
        return true;
    }

    public boolean isNonLocked(BtuUserBean btuUserBean) {
        return true;
    }

    public boolean isActive(BtuUserBean btuUserBean){return true;}

    @Override
    public boolean hasAnyAuthority(String... targetAuthorities){
        return true;
    }

    @Override
    public void setLogonPage(BtuLoginSuccessHandler btuLoginSuccessHandler) {
        btuLoginSuccessHandler.setDefaultTargetUrl("/user/");
        btuLoginSuccessHandler.setAlwaysUseDefaultTargetUrl(true);
    }

    protected boolean hasAnyAuthority(Collection<? extends GrantedAuthority> authorities, String... targetAuthorities) {
        // find matching auth
        for(String targetAuth : targetAuthorities){
            for(GrantedAuthority grantedAuthority : authorities){
                if( ! (grantedAuthority instanceof SimpleGrantedAuthority) ){
                    continue;
                }

                SimpleGrantedAuthority existingAuth = (SimpleGrantedAuthority) grantedAuthority;
                if(StringUtils.equals(targetAuth, existingAuth.getAuthority())){
                    return true;
                }
            }
        }

        return false;
    }

    protected String encodePassword(CharSequence plaintext) {
        return btuPasswordEncoder.encode(plaintext);
    }

    protected boolean matchPassword(String rawPassword, String encodedPassword){
        return btuPasswordEncoder.matches(rawPassword, encodedPassword);
    }

}
