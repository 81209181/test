package com.hkt.btu.sd.core.service.bean;

import com.hkt.btu.common.core.service.bean.BaseBean;

public class StatusSummaryBean extends BaseBean {

    private String status; // todo: SERVDESK-210 use constant, use TicketStatusEnum
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
