package com.hkt.btu.sd.core.dao.entity;

import com.hkt.btu.common.core.dao.entity.BaseEntity;

import java.util.List;
import java.util.StringJoiner;

public class SdUserRoleEntity extends BaseEntity {

    public final static String SYS_ADMIN = "SYS_ADMIN";
    public final static String TEAM_HEAD_INDICATOR = "TH__";

    public final static String IS_ABSTRACT = "Y";

    private String roleId;
    private String roleDesc;
    private String parentRoleId;
    private String status;
    private String abstractFlag;

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

    public String getAbstractFlag() {
        return abstractFlag;
    }

    public void setAbstractFlag(String abstractFlag) {
        this.abstractFlag = abstractFlag;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", SdUserRoleEntity.class.getSimpleName() + "[", "]")
                .add("roleId='" + roleId + "'")
                .add("roleDesc='" + roleDesc + "'")
                .add("parentRoleId='" + parentRoleId + "'")
                .add("status='" + status + "'")
                .add("abstractFlag='" + abstractFlag + "'")
                .toString();
    }
}
