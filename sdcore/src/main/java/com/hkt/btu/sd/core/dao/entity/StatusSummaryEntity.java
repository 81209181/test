package com.hkt.btu.sd.core.dao.entity;

public class StatusSummaryEntity {

    private String status;
    private Integer queryCnt;
    private Integer jobCnt;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getQueryCnt() {
        return queryCnt;
    }

    public void setQueryCnt(Integer queryCnt) {
        this.queryCnt = queryCnt;
    }

    public Integer getJobCnt() {
        return jobCnt;
    }

    public void setJobCnt(Integer jobCnt) {
        this.jobCnt = jobCnt;
    }
}
