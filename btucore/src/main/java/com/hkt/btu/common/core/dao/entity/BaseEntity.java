package com.hkt.btu.common.core.dao.entity;

import java.time.LocalDateTime;

public class BaseEntity {
    private LocalDateTime createDate;
    private String createBy;
    private LocalDateTime modifyDate;
    private String modifyBy;
    private String remarks;

    private Integer totalCount;

    @Deprecated
    public LocalDateTime getCreatedate() {
        return createDate;
    }
    @Deprecated
    public void setCreatedate(LocalDateTime createdate) {
        this.createDate = createdate;
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
    public String getCreateby() {
        return createBy;
    }
    @Deprecated
    public void setCreateby(String createby) {
        this.createBy = createby;
    }
    @Deprecated
    public String getModifyby() {
        return modifyBy;
    }
    @Deprecated
    public void setModifyby(String modifyby) {
        this.modifyBy = modifyby;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    // future update to standard variable naming method
    public LocalDateTime getCreateDate() {
        return createDate;
    }

    public void setCreateDate(LocalDateTime createdate) {
        this.createDate = createdate;
    }


    public LocalDateTime getModifyDate() {
        return modifyDate;
    }

    public void setModifyDate(LocalDateTime modifydate) {
        this.modifyDate = modifydate;
    }

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createby) {
        this.createBy = createby;
    }

    public String getModifyBy() {
        return modifyBy;
    }

    public void setModifyBy(String modifyby) {
        this.modifyBy = modifyby;
    }

    public Integer getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(Integer totalCount) {
        this.totalCount = totalCount;
    }
}
