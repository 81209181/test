package com.hkt.btu.sd.core.dao.entity;

import com.hkt.btu.common.core.dao.entity.BaseEntity;

import java.util.List;

public class SdTicketServiceEntity extends BaseEntity {

    private int ticketDetId;
    private int ticketMasId;
    private String serviceTypeCode;
    private String serviceId;
    private List<SdServiceFaultsEntity> faultList;
    private String jobId;
    private String subsId;

    public String getJobId() {
        return jobId;
    }

    public void setJobId(String jobId) {
        this.jobId = jobId;
    }

    public String getSubsId() {
        return subsId;
    }

    public void setSubsId(String subsId) {
        this.subsId = subsId;
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

    public List<SdServiceFaultsEntity> getFaults() {
        return faultList;
    }

    public void setFaults(List<SdServiceFaultsEntity> faults) {
        this.faultList = faults;
    }

    @Override
    public String toString() {
        return "SdTicketServiceEntity{" +
                "ticketDetId=" + ticketDetId +
                ", ticketMasId=" + ticketMasId +
                ", serviceTypeCode='" + serviceTypeCode + '\'' +
                ", serviceId='" + serviceId + '\'' +
                ", faults=" + faultList +
                '}';
    }
}
