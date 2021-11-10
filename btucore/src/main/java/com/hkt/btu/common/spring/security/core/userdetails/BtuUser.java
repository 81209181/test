package com.hkt.btu.common.spring.security.core.userdetails;

import com.hkt.btu.common.core.service.bean.BtuUserBean;
import com.hkt.btu.sds.spring.security.core.userdetails.SdsUserBeanHolder;
import com.hkt.btu.sds.spring.security.core.userdetails.SdsUserHolder;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

public class BtuUser extends User implements SdsUserBeanHolder, SdsUserHolder {

    private byte[] encryptLdapPassword;
    private BtuUserBean userBean;
    private String loginMethod;

    private BtuUser(String username, String password, boolean enabled, boolean accountNonExpired, boolean credentialsNonExpired,
                    boolean accountNonLocked, Collection<? extends GrantedAuthority> authorities, BtuUserBean btuUserBean) {
        super(username, password, enabled, accountNonExpired, credentialsNonExpired, accountNonLocked, authorities);
        this.userBean = btuUserBean;
    }

    public static BtuUser of(String username, String password, boolean enabled, boolean accountNonExpired, boolean credentialsNonExpired,
                             boolean accountNonLocked, Collection<? extends GrantedAuthority> authorities, BtuUserBean btuUserBean){
        return new BtuUser(username, password, enabled, accountNonExpired, credentialsNonExpired,
                accountNonLocked, authorities, btuUserBean);
    }

    @Override
    public BtuUserBean getUserBean() {
        return userBean;
    }

    public void setUserBean(BtuUserBean userBean) {
        this.userBean = userBean;
    }

    public byte[] getEncryptLdapPassword() {
        return encryptLdapPassword;
    }

    public void setEncryptLdapPassword(byte[] encryptLdapPassword) {
        this.encryptLdapPassword = encryptLdapPassword;
    }

    public String getLoginMethod() {
        return loginMethod;
    }

    public void setLoginMethod(String loginMethod) {
        this.loginMethod = loginMethod;
    }

    @Override
    public BtuUser getUser() {
        return this;
    }
}
