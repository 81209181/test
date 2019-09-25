package com.hkt.btu.sd.facade.data;

import com.hkt.btu.common.facade.data.DataInterface;

import java.util.Map;

public class WfmJobCompleteBeanData implements DataInterface {

    private Integer jobId;
    private String deptId;
    private String remark;
    private String lastUpdDate;
    private String lastUpdId;
    private String completeDateTime;

    protected Map<String, String> parameterMap;

    public Integer getJobId() {
        return jobId;
    }

    public void setJobId(Integer jobId) {
        this.jobId = jobId;
    }

    public String getDeptId() {
        return deptId;
    }

    public void setDeptId(String deptId) {
        this.deptId = deptId;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getLastUpdDate() {
        return lastUpdDate;
    }

    public void setLastUpdDate(String lastUpdDate) {
        this.lastUpdDate = lastUpdDate;
    }

    public String getLastUpdId() {
        return lastUpdId;
    }

    public void setLastUpdId(String lastUpdId) {
        this.lastUpdId = lastUpdId;
    }

    public String getCompleteDateTime() {
        return completeDateTime;
    }

    public void setCompleteDateTime(String completeDateTime) {
        this.completeDateTime = completeDateTime;
    }

    public Map<String, String> getParameterMap() {
        return parameterMap;
    }

    public void setParameterMap(Map<String, String> parameterMap) {
        this.parameterMap = parameterMap;
    }
}
