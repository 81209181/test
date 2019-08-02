package com.hkt.btu.common.core.service.bean;

import org.springframework.security.core.GrantedAuthority;

import java.time.LocalDateTime;
import java.util.Set;

public class BtuUserBean extends BaseBean {

    private String username;
    private String status;

    private String password;
    private LocalDateTime passwordModifydate;
    private Integer loginTried;

    private Set<GrantedAuthority> authorities;

    private Integer userId;
    private String name;
    private String email;
    private String mobile;
    private String staffId;

    private Integer companyId;

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getStaffId() {
        return staffId;
    }

    public void setStaffId(String staffId) {
        this.staffId = staffId;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public Integer getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Integer companyId) {
        this.companyId = companyId;
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
