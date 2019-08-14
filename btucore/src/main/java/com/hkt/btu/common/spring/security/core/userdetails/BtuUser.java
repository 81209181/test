package com.hkt.btu.common.spring.security.core.userdetails;

import com.hkt.btu.common.core.service.bean.BtuUserBean;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

public class BtuUser extends User {

    private String ldapPassword;
    private BtuUserBean userBean;

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


    public BtuUserBean getUserBean() {
        return userBean;
    }

    public void setUserBean(BtuUserBean userBean) {
        this.userBean = userBean;
    }

    public String getLdapPassword() {
        return ldapPassword;
    }

    public void setLdapPassword(String ldapPassword) {
        this.ldapPassword = ldapPassword;
    }
}
