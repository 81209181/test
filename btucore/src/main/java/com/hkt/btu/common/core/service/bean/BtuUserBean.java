package com.hkt.btu.common.core.service.bean;

import org.springframework.security.core.GrantedAuthority;

import java.time.LocalDateTime;
import java.util.Set;

public class BtuUserBean extends BaseBean {

    private String username;
    private String status;

    private String ldapDomain;
    private String password;
    private LocalDateTime passwordModifydate;
    private Integer loginTried;

    private Set<GrantedAuthority> authorities;

    public String getLdapDomain() {
        return ldapDomain;
    }

    public void setLdapDomain(String ldapDomain) {
        this.ldapDomain = ldapDomain;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Set<GrantedAuthority> getAuthorities() {
        return authorities;
    }

    public void setAuthorities(Set<GrantedAuthority> authorities) {
        this.authorities = authorities;
    }

    public LocalDateTime getPasswordModifydate() {
        return passwordModifydate;
    }

    public void setPasswordModifydate(LocalDateTime passwordModifydate) {
        this.passwordModifydate = passwordModifydate;
    }

    public Integer getLoginTried() {
        return loginTried;
    }

    public void setLoginTried(Integer loginTried) {
        this.loginTried = loginTried;
    }
}
