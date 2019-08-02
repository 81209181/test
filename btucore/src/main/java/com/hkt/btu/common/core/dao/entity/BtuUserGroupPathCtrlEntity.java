package com.hkt.btu.common.core.dao.entity;


public class BtuUserGroupPathCtrlEntity extends BaseEntity {

    public static final String STATUS_ACTIVE = "A";

    private Integer pathCtrlId;
    private String antPath;
    private String status;

    private String groupId;



    public Integer getPathCtrlId() {
        return pathCtrlId;
    }

    public void setPathCtrlId(Integer pathCtrlId) {
        this.pathCtrlId = pathCtrlId;
    }

    public String getAntPath() {
        return antPath;
    }

    public void setAntPath(String antPath) {
        this.antPath = antPath;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }
}
