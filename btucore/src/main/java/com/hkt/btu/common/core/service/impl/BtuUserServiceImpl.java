package com.hkt.btu.common.core.service.impl;

import com.hkt.btu.common.core.exception.BtuMissingImplException;
import com.hkt.btu.common.core.exception.UserNotFoundException;
import com.hkt.btu.common.core.service.BtuLdapService;
import com.hkt.btu.common.core.service.BtuSensitiveDataService;
import com.hkt.btu.common.core.service.BtuUserService;
import com.hkt.btu.common.core.service.bean.BtuLdapBean;
import com.hkt.btu.common.core.service.bean.BtuUserBean;
import com.hkt.btu.common.core.service.bean.BtuUserRolePathCtrlBean;
import com.hkt.btu.common.spring.security.core.userdetails.BtuUser;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.annotation.Resource;
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

    @Resource(name = "sensitiveDataService")
    BtuSensitiveDataService sensitiveDataService;


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

    @Override
    public String getCurrentUserId() {
        BtuUserBean userBean = getCurrentUserBean();
        return userBean==null ? null : userBean.getUserId();
    }


    public BtuUserBean getUserBeanByUsername(String username) {
        LOG.error("DEMO ONLY IMPLEMENTATION! Please override and implement by DI.");
        throw new BtuMissingImplException();

//        if (StringUtils.equals(username, "admin")){
//            BtuUserBean btuUserBean = new BtuUserBean();
//            btuUserBean.setUsername(username);
//            btuUserBean.setPassword( btuPasswordEncoder.encode("admin") );
//
//            Set<GrantedAuthority> grantedAuthSet = new HashSet<>();
//            grantedAuthSet.add(new SimpleGrantedAuthority("ADMIN"));
//            grantedAuthSet.add(new SimpleGrantedAuthority("USER"));
//            btuUserBean.setAuthorities(grantedAuthSet);
//
//            return btuUserBean;
//        } else if (StringUtils.equals(username, "user1")){
//
//            BtuUserBean btuUserBean = new BtuUserBean();
//            btuUserBean.setUsername(username);
//            btuUserBean.setPassword( btuPasswordEncoder.encode("user1") );
//
//            Set<GrantedAuthority> grantedAuthSet = new HashSet<>();
//            grantedAuthSet.add(new SimpleGrantedAuthority("USER"));
//            btuUserBean.setAuthorities(grantedAuthSet);
//
//            return btuUserBean;
//        }
//        return null;
    }

    @Override
    public List<BtuUserBean> getApiUser() {
        LOG.error("DEMO ONLY IMPLEMENTATION! Please override and implement by DI.");
        throw new BtuMissingImplException();
    }

    public void resetLoginTriedByUsername(String username) {
        LOG.error("DEMO ONLY IMPLEMENTATION! Please override and implement by DI.");
        throw new BtuMissingImplException();
    }

    public void addLoginTriedByUsername(String username) {
        LOG.error("DEMO ONLY IMPLEMENTATION! Please override and implement by DI.");
        throw new BtuMissingImplException();
    }

    public void lockUserByUsername(String username) {
        LOG.error("DEMO ONLY IMPLEMENTATION! Please override and implement by DI.");
        throw new BtuMissingImplException();
    }

    public void activateUserByUsername(String username) {
        LOG.error("DEMO ONLY IMPLEMENTATION! Please override and implement by DI.");
        throw new BtuMissingImplException();
    }
    

	@Override
	public BtuUserBean verifyLdapUser(BtuUser user, BtuUserBean userDetailBean) {
		try {
			return verifyLdapUser(user, userDetailBean, userDetailBean.getUserId());
		} catch (Exception e) {
			throw e;
		}
	}

    @Override
    public BtuUserBean verifyLdapUser(BtuUser user, BtuUserBean userDetailBean, String userId){
        try {
            BtuLdapBean ldapInfo = ldapService.getBtuLdapBean(userDetailBean.getLdapDomain());
            String ldapPassword = sensitiveDataService.decryptToStringSafe(user.getEncryptLdapPassword());
            return ldapService.searchUser(
                    ldapInfo, userDetailBean.getUserId(), ldapPassword, userId);
        } catch (Exception e) {
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
    public String getUserLogonPage() {
        return BtuUserRolePathCtrlBean.DEFAULT_LOGON_PATH;
    }

    @Override
    public boolean hasAnyAuthority(String... targetAuthorities) {
        BtuUserBean currentUserBean = getCurrentUserBean();
        Set<GrantedAuthority> authorities = currentUserBean==null ? null : currentUserBean.getAuthorities();
        if(CollectionUtils.isEmpty(authorities)){
            LOG.warn("Authorities of user not found.");
            return false;
        }

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

    @Override
    public void updateLdapInfo(String userId, String username, String ldapEmail) {
        LOG.error("DEMO ONLY IMPLEMENTATION! Please override and implement by DI.");
        throw new BtuMissingImplException();
    }

    protected String encodePassword(CharSequence plaintext) {
        return btuPasswordEncoder.encode(plaintext);
    }

    protected boolean matchPassword(String rawPassword, String encodedPassword){
        return btuPasswordEncoder.matches(rawPassword, encodedPassword);
    }

}
