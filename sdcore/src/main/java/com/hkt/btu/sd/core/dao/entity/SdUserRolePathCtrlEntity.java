package com.hkt.btu.sd.core.dao.entity;

import com.hkt.btu.common.core.dao.entity.BaseEntity;

public class SdUserRolePathCtrlEntity extends BaseEntity {

    private static final String STATUS_ACTIVE = "A";

    private Integer pathCtrlId;
    private String path;
    private String status;
    private String description;

    private String roleId;

    public Integer getPathCtrlId() {
        return pathCtrlId;
    }

    public void setPathCtrlId(Integer pathCtrlId) {
        this.pathCtrlId = pathCtrlId;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getRoleId() {
        return roleId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }
}
