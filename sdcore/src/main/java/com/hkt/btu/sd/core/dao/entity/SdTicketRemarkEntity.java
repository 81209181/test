package com.hkt.btu.sd.core.dao.entity;

import com.hkt.btu.common.core.dao.entity.BaseEntity;

public class SdTicketRemarkEntity extends BaseEntity {

    public static class REMARKS_TYPE{
        public static final String CUSTOMER = "CUST";
        public static final String FIELD = "FIELD";
        public static final String SYSTEM = "SYS";
    }

    private Integer ticketRemarksId;
    private Integer ticketMasId;
    private String remarksType;

    public Integer getTicketRemarksId() {
        return ticketRemarksId;
    }

    public void setTicketRemarksId(Integer ticketRemarksId) {
        this.ticketRemarksId = ticketRemarksId;
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
}
