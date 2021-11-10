package com.hkt.btu.common.core.service.bean;

import java.time.LocalDateTime;

public class BaseBean {

    private LocalDateTime createDate;
    private String createBy;
    private LocalDateTime modifyDate;
    private String modifyBy;
    private String remarks;


    public LocalDateTime getCreateDate() {
        return createDate;
    }

    public void setCreateDate(LocalDateTime createDate) {
        this.createDate = createDate;
    }


    public LocalDateTime getModifyDate() {
        return modifyDate;
    }

    public void setModifyDate(LocalDateTime modifyDate) {
        this.modifyDate = modifyDate;
    }

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    public String getModifyBy() {
        return modifyBy;
    }

    public void setModifyBy(String modifyBy) {
        this.modifyBy = modifyBy;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }
    // compatibility
    @Deprecated
    public LocalDateTime getCreatedate() {
        return createDate;
    }
    @Deprecated
    public void setCreatedate(LocalDateTime createdate) {
        this.createDate = createdate;
    }
    @Deprecated
    public String getCreateby() {
        return createBy;
    }
    @Deprecated
    public void setCreateby(String createby) {
        this.createBy = createby;
    }
    @Deprecated
    public LocalDateTime getModifydate() {
        return modifyDate;
    }
    @Deprecated
    public void setModifydate(LocalDateTime modifydate) {
        this.modifyDate = modifydate;
    }
    @Deprecated
    public String getModifyby() {
        return modifyBy;
    }
    @Deprecated
    public void setModifyby(String modifyby) {
        this.modifyBy = modifyby;
    }
}
