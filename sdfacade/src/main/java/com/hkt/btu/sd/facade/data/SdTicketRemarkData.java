package com.hkt.btu.sd.facade.data;

import com.hkt.btu.common.facade.data.DataInterface;

import java.time.LocalDateTime;

public class SdTicketRemarkData implements DataInterface {

    private Integer ticketMasId;

    private String remarksType;

    private String remarks;

    private LocalDateTime createdate;

    public static class Type {
        public static final String CUSTOMER = "CUST";
        public static final String FIELD = "FIELD";
        public static final String SYSTEM = "SYS";
    }

    public String getRemarksTypeValue() {
        switch (remarksType) {
            case "Customer":
                return "CUST";
            case "Field":
                return "FIELD";
            case "System":
                return "SYS";
            default:
                return "Not found match remarks type";
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

    public LocalDateTime getCreatedate() {
        return createdate;
    }

    public void setCreatedate(LocalDateTime createdate) {
        this.createdate = createdate;
    }
}
