package com.hkt.btu.common.core.service.bean;

public class BtuUserGroupPathCtrlBean extends BaseBean {

    private String antPath;
    private String status;

    private String groupId;



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
