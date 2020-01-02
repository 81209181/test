package com.hkt.btu.sd.facade.data;

import com.hkt.btu.common.facade.data.DataInterface;

public class TeamSummaryData implements DataInterface {

    private int queryTotal;
    private int jobTotal;

    private int openQueryCount;
    private int openJobCount;

    private int workQueryCount;
    private int workJobCount;

    private int completeQueryCount;
    private int completeJobCount;

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

    public int getOpenQueryCount() {
        return openQueryCount;
    }

    public void setOpenQueryCount(int openQueryCount) {
        this.openQueryCount = openQueryCount;
    }

    public int getOpenJobCount() {
        return openJobCount;
    }

    public void setOpenJobCount(int openJobCount) {
        this.openJobCount = openJobCount;
    }

    public int getWorkQueryCount() {
        return workQueryCount;
    }

    public void setWorkQueryCount(int workQueryCount) {
        this.workQueryCount = workQueryCount;
    }

    public int getWorkJobCount() {
        return workJobCount;
    }

    public void setWorkJobCount(int workJobCount) {
        this.workJobCount = workJobCount;
    }

    public int getCompleteQueryCount() {
        return completeQueryCount;
    }

    public void setCompleteQueryCount(int completeQueryCount) {
        this.completeQueryCount = completeQueryCount;
    }

    public int getCompleteJobCount() {
        return completeJobCount;
    }

    public void setCompleteJobCount(int completeJobCount) {
        this.completeJobCount = completeJobCount;
    }
}
