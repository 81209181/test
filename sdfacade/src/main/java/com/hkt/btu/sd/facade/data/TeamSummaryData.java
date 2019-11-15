package com.hkt.btu.sd.facade.data;

import com.hkt.btu.common.facade.data.DataInterface;

import java.util.List;

public class TeamSummaryData implements DataInterface {

    private List<StatusSummaryData> summaryData;
    private int queryTotal;
    private int jobTotal;

    public List<StatusSummaryData> getSummaryData() {
        return summaryData;
    }

    public void setSummaryData(List<StatusSummaryData> summaryData) {
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
