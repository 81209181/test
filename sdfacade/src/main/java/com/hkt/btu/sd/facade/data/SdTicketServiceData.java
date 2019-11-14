package com.hkt.btu.sd.facade.data;

import com.hkt.btu.common.facade.data.DataInterface;

import java.util.List;

public class SdTicketServiceData implements DataInterface {

    private Integer ticketDetId;
    private String serviceType;
    private String serviceCode;
    private List<SdSymptomData> faultsList;
    private String jobId;
    private boolean detailButton;

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

    public boolean isDetailButton() {
        return detailButton;
    }

    public void setDetailButton(boolean detailButton) {
        this.detailButton = detailButton;
    }
}
