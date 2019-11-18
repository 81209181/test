package com.hkt.btu.sd.core.service.bean;

import com.hkt.btu.common.core.service.bean.BaseBean;

import java.util.List;

public class SdUserRoleBean extends BaseBean {

    public static class ROLE_ID{
        public static final String OPERATOR = "OPT";
        public static final String ENGINEER = "ENG";
    }

    public final static String ACTIVE_ROLE_STATUS = "A";

    private String roleId;
    private String roleDesc;
    private String parentRoleId;
    private String status;

    private List<SdUserRoleBean> children;

    private boolean isActive;
    private boolean isAbstract;
    private boolean isPrimaryRole;

    public String getRoleId() {
        return roleId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }

    public String getRoleDesc() {
        return roleDesc;
    }

    public void setRoleDesc(String roleDesc) {
        this.roleDesc = roleDesc;
    }

    public String getParentRoleId() {
        return parentRoleId;
    }

    public void setParentRoleId(String parentRoleId) {
        this.parentRoleId = parentRoleId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public boolean isAbstract() {
        return isAbstract;
    }

    public void setAbstract(boolean anAbstract) {
        isAbstract = anAbstract;
    }

    public List<SdUserRoleBean> getChildren() {
        return children;
    }

    public void setChildren(List<SdUserRoleBean> children) {
        this.children = children;
    }

    public boolean isPrimaryRole() {
        return isPrimaryRole;
    }

    public void setPrimaryRole(boolean primaryRole) {
        isPrimaryRole = primaryRole;
    }
}
