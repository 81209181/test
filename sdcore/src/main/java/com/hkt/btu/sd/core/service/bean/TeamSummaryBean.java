package com.hkt.btu.sd.core.service.bean;

import com.hkt.btu.common.core.service.bean.BaseBean;

import java.util.List;

public class TeamSummaryBean extends BaseBean {

    private List<StatusSummaryBean> summaryData;
    private int queryTotal;
    private int jobTotal;

    public List<StatusSummaryBean> getSummaryData() {
        return summaryData;
    }

    public void setSummaryData(List<StatusSummaryBean> summaryData) {
        this.summaryData = summaryData;
    }

    public int getQueryTotal() {
        return queryTotal;
    }

    public void setQueryTotal(int queryTotal) {
        this.queryTotal = queryTotal;
    }

    public int getJobTotal() {
        return jobTotal;
    }

    public void setJobTotal(int jobTotal) {
        this.jobTotal = jobTotal;
    }
}
