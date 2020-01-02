package com.hkt.btu.sd.core.dao.entity;

public class StatusSummaryEntity {

    private String status = "";
    private int queryCnt;
    private int jobCnt;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getQueryCnt() {
        return queryCnt;
    }

    public void setQueryCnt(int queryCnt) {
        this.queryCnt = queryCnt;
    }

    public int getJobCnt() {
        return jobCnt;
    }

    public void setJobCnt(int jobCnt) {
        this.jobCnt = jobCnt;
    }
}
