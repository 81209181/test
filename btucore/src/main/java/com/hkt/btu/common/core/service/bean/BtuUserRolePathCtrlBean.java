package com.hkt.btu.common.core.service.bean;

public class BtuUserRolePathCtrlBean extends BaseBean {

    public static final String DEFAULT_LOGON_PATH = "/user/";

    private String path;
    private String status;

    private String roleId;

    private Integer pathCtrlId;

    public Integer getPathCtrlId() {
        return pathCtrlId;
    }

    public void setPathCtrlId(Integer pathCtrlId) {
        this.pathCtrlId = pathCtrlId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getRoleId() {
        return roleId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }
}
