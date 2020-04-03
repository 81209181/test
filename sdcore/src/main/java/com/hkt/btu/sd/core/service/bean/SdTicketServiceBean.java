package com.hkt.btu.sd.core.service.bean;

import com.hkt.btu.common.core.service.bean.BaseBean;

import java.time.LocalDateTime;
import java.util.List;

public class SdTicketServiceBean extends BaseBean {

    public static final String DUMMY_POLE_ID_PREFIX = "D";

    private int ticketDetId;
    private int ticketMasId;
    private String serviceTypeCode;
    private String serviceId;
    private List<String> faults;
    private List<SdSymptomBean> faultsList;
    private String jobId;
    private String subsId;
    private LocalDateTime reportTime;

    public String getSubsId() {
        return subsId;
    }

    public void setSubsId(String subsId) {
        this.subsId = subsId;
    }

    public String getJobId() {
        return jobId;
    }

    public void setJobId(String jobId) {
        this.jobId = jobId;
    }

    public int getTicketDetId() {
        return ticketDetId;
    }

    public void setTicketDetId(int ticketDetId) {
        this.ticketDetId = ticketDetId;
    }

    public int getTicketMasId() {
        return ticketMasId;
    }

    public void setTicketMasId(int ticketMasId) {
        this.ticketMasId = ticketMasId;
    }

    public String getServiceTypeCode() {
        return serviceTypeCode;
    }

    public void setServiceTypeCode(String serviceTypeCode) {
        this.serviceTypeCode = serviceTypeCode;
    }

    public String getServiceId() {
        return serviceId;
    }

    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }

    public List<String> getFaults() {
        return faults;
    }

    public void setFaults(List<String> faults) {
        this.faults = faults;
    }

    public List<SdSymptomBean> getFaultsList() {
        return faultsList;
    }

    public void setFaultsList(List<SdSymptomBean> faultsList) {
        this.faultsList = faultsList;
    }

    public LocalDateTime getReportTime() {
        return reportTime;
    }

    public void setReportTime(LocalDateTime reportTime) {
        this.reportTime = reportTime;
    }
}
