package com.hkt.btu.sd.facade.data;

import com.hkt.btu.common.facade.data.DataInterface;

public class WfmFaultBeanData implements DataInterface {
    private String faultId;
    private Integer jobId;
    private Long requestId;
    private String lastUpdId;
    private String service;

    public String getFaultId() {
        return faultId;
    }

    public void setFaultId(String faultId) {
        this.faultId = faultId;
    }

    public Integer getJobId() {
        return jobId;
    }

    public void setJobId(Integer jobId) {
        this.jobId = jobId;
    }

    public Long getRequestId() {
        return requestId;
    }

    public void setRequestId(Long requestId) {
        this.requestId = requestId;
    }

    public String getLastUpdId() {
        return lastUpdId;
    }

    public void setLastUpdId(String lastUpdId) {
        this.lastUpdId = lastUpdId;
    }

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }
}
