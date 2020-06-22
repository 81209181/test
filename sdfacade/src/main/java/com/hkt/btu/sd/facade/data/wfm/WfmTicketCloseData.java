package com.hkt.btu.sd.facade.data.wfm;

import com.hkt.btu.sd.core.service.bean.WfmCompleteInfo;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class WfmTicketCloseData {

    private Integer ticketMasId;
    private String reasonType;
    private String reasonContent;
    private String username;
    private String arrivalTime;
    private List<WfmCompleteInfo> wfmCompleteInfo;

    public Integer getTicketMasId() {
        return ticketMasId;
    }

    public void setTicketMasId(Integer ticketMasId) {
        this.ticketMasId = ticketMasId;
    }

    public String getReasonType() {
        return reasonType;
    }

    public void setReasonType(String reasonType) {
        this.reasonType = reasonType;
    }

    public String getReasonContent() {
        return reasonContent;
    }

    public void setReasonContent(String reasonContent) {
        this.reasonContent = reasonContent;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getArrivalTime() {
        return arrivalTime;
    }

    public void setArrivalTime(String arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    public List<WfmCompleteInfo> getWfmCompleteInfo() {
        return wfmCompleteInfo;
    }

    public void setWfmCompleteInfo(List<WfmCompleteInfo> wfmCompleteInfo) {
        this.wfmCompleteInfo = wfmCompleteInfo;
    }
}