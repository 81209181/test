package com.hkt.btu.sd.core.service.bean;

import com.hkt.btu.common.core.service.bean.BaseBean;
import com.hkt.btu.sd.core.service.constant.TicketStatusEnum;

public class StatusSummaryBean extends BaseBean {

    private TicketStatusEnum status;
    private int queryCnt;
    private int jobCnt;

    public TicketStatusEnum getStatus() {
        return status;
    }

    public void setStatus(TicketStatusEnum status) {
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
