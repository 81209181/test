package com.hkt.btu.sd.core.dao.entity;

import com.hkt.btu.common.core.dao.entity.BaseEntity;

public class SdUserRoleEntity extends BaseEntity {

    public final static String SYS_ADMIN = "SYS_ADMIN";
    public final static String TEAM_HEAD_INDICATOR = "__";
    public final static String ACTIVE_ROLE_STATUS = "A";

    private String roleId;
    private String roleDesc;
    private String parentRoleId;
    private String status;

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
}
