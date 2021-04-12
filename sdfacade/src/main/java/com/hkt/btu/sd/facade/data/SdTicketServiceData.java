package com.hkt.btu.sd.facade.data;

import com.hkt.btu.common.facade.data.DataInterface;

import java.time.LocalDateTime;
import java.util.List;

public class SdTicketServiceData implements DataInterface {

    private Integer ticketDetId;
    private String serviceType;
    private String serviceCode;
    private String serviceTypeDesc;
    private List<SdSymptomData> faultsList;
    private String jobId;
    private LocalDateTime reportTime;

    // button control
    private boolean bnCtrl;
    private boolean voIpCtrl;
    private boolean cloudCtrl;
    private boolean meterCtrl;
    private boolean gmbCtrl;

    public String getJobId() {
        return jobId;
    }

    public void setJobId(String jobId) {
        this.jobId = jobId;
    }

    public Integer getTicketDetId() {
        return ticketDetId;
    }

    public void setTicketDetId(Integer ticketDetId) {
        this.ticketDetId = ticketDetId;
    }

    public String getServiceType() {
        return serviceType;
    }

    public void setServiceType(String serviceType) {
        this.serviceType = serviceType;
    }

    public String getServiceCode() {
        return serviceCode;
    }

    public void setServiceCode(String serviceCode) {
        this.serviceCode = serviceCode;
    }

    public List<SdSymptomData> getFaultsList() {
        return faultsList;
    }

    public void setFaultsList(List<SdSymptomData> faultsList) {
        this.faultsList = faultsList;
    }

    public String getServiceTypeDesc() {
        return serviceTypeDesc;
    }

    public void setServiceTypeDesc(String serviceTypeDesc) {
        this.serviceTypeDesc = serviceTypeDesc;
    }

    public LocalDateTime getReportTime() {
        return reportTime;
    }

    public void setReportTime(LocalDateTime reportTime) {
        this.reportTime = reportTime;
    }

    public boolean isBnCtrl() {
        return bnCtrl;
    }

    public void setBnCtrl(boolean bnCtrl) {
        this.bnCtrl = bnCtrl;
    }

    public boolean isVoIpCtrl() {
        return voIpCtrl;
    }

    public void setVoIpCtrl(boolean voIpCtrl) {
        this.voIpCtrl = voIpCtrl;
    }

    public boolean isCloudCtrl() {
        return cloudCtrl;
    }

    public void setCloudCtrl(boolean cloudCtrl) {
        this.cloudCtrl = cloudCtrl;
    }

    public boolean isMeterCtrl() {
        return meterCtrl;
    }

    public void setMeterCtrl(boolean meterCtrl) {
        this.meterCtrl = meterCtrl;
    }

    public boolean isGmbCtrl() {
        return gmbCtrl;
    }

    public void setGmbCtrl(boolean gmbCtrl) {
        this.gmbCtrl = gmbCtrl;
    }
}
