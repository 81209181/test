package com.hkt.btu.sd.facade.data;

import com.hkt.btu.common.facade.data.DataInterface;

public class SdTicketRemarkData implements DataInterface {

    private Integer ticketMasId;

    private String remarksType;

    private String remarks;

    private boolean isSystem;

    public String getRemarksTypeValue() {
        switch (remarksType) {
            case "Customer":
                return "CUST";
            case "Field":
                return "FIELD";
            case "System":
                return "SYS";
            default:
                return "Not found match contact type";
        }
    }

    public Integer getTicketMasId() {
        return ticketMasId;
    }

    public void setTicketMasId(Integer ticketMasId) {
        this.ticketMasId = ticketMasId;
    }

    public String getRemarksType() {
        return remarksType;
    }

    public void setRemarksType(String remarksType) {
        this.remarksType = remarksType;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public boolean isSystem() {
        return isSystem;
    }

    public void setSystem(boolean isSystem) {
        this.isSystem = isSystem;
    }
}
