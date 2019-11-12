package com.hkt.btu.sd.facade.data;

import com.hkt.btu.common.facade.data.DataInterface;

public class SdUserRoleData implements DataInterface {

    private String roleId;
    private String roleDesc;
    private String parentRoleId;
    private String status;
    private String abstractFlag;
    private boolean isPrimaryRole; // todo SERVDESK-203: populate this with SdUserBean.isPrimaryRole

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
}
