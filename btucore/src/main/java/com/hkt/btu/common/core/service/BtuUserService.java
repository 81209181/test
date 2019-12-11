package com.hkt.btu.common.core.service;

import com.hkt.btu.common.core.service.bean.BtuUserBean;
import com.hkt.btu.common.spring.security.core.userdetails.BtuUser;

public interface BtuUserService {

    BtuUser getCurrentUser();
    BtuUserBean getCurrentUserBean();
    BtuUserBean getUserBeanByUsername(String username);

    void resetLoginTriedByUsername(String username);
    void addLoginTriedByUsername(String username);
    void lockUserByUsername(String username);
    void activateUserByUsername(String username);
    BtuUserBean verifyLdapUser(BtuUser user,BtuUserBean userDetailBean);
    boolean isEnabled(BtuUserBean userDetailBean);
    boolean isNonLocked(BtuUserBean userDetailBean);
    boolean isActive(BtuUserBean userDetailBean);

    String getUserLogonPage();
    boolean hasAnyAuthority(String... targetAuthorities);
}
