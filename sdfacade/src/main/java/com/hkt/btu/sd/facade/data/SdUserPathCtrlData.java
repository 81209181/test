package com.hkt.btu.sd.facade.data;

import com.hkt.btu.common.facade.data.DataInterface;

import java.time.LocalDateTime;

public class SdUserPathCtrlData implements DataInterface {

    private String roleId;
    private Integer pathCtrlId;
    private String path;
    private String status;
    private String description;
    private LocalDateTime createdate;
    private String createby;
    private LocalDateTime modifydate;
    private String modifyby;
    private String remarks;


    public String getRoleId() {
        return roleId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }

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

    public LocalDateTime getCreatedate() {
        return createdate;
    }

    public void setCreatedate(LocalDateTime createdate) {
        this.createdate = createdate;
    }

    public String getCreateby() {
        return createby;
    }

    public void setCreateby(String createby) {
        this.createby = createby;
    }

    public LocalDateTime getModifydate() {
        return modifydate;
    }

    public void setModifydate(LocalDateTime modifydate) {
        this.modifydate = modifydate;
    }

    public String getModifyby() {
        return modifyby;
    }

    public void setModifyby(String modifyby) {
        this.modifyby = modifyby;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

}
