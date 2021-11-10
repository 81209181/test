package com.hkt.btu.common.core.service;

import com.hkt.btu.common.core.service.bean.BtuUserBean;
import com.hkt.btu.common.spring.security.core.userdetails.BtuUser;

import java.util.List;

public interface BtuUserService {

    BtuUser getCurrentUser();
    BtuUserBean getCurrentUserBean();
    String getCurrentUserId();
    BtuUserBean getUserBeanByUsername(String username);

    List<BtuUserBean> getApiUser();

    void resetLoginTriedByUsername(String username);
    void addLoginTriedByUsername(String username);
    void lockUserByUsername(String username);
    void activateUserByUsername(String username);
    BtuUserBean verifyLdapUser(BtuUser user,BtuUserBean userDetailBean, String userId);
    boolean isEnabled(BtuUserBean userDetailBean);
    boolean isNonLocked(BtuUserBean userDetailBean);
    boolean isActive(BtuUserBean userDetailBean);

    String getUserLogonPage();
    boolean hasAnyAuthority(String... targetAuthorities);

    void updateLdapInfo(String loginUser, String username, String userEmail);
	BtuUserBean verifyLdapUser(BtuUser user, BtuUserBean userDetailBean);
}
