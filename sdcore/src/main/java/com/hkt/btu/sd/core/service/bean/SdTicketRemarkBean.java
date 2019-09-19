package com.hkt.btu.sd.core.service.bean;

import com.hkt.btu.common.core.service.bean.BaseBean;

public class SdTicketRemarkBean extends BaseBean {

    private int ticketMasId ;
    private String remarksType;

    public int getTicketMasId() {
        return ticketMasId;
    }

    public void setTicketMasId(int ticketMasId) {
        this.ticketMasId = ticketMasId;
    }

    public String getRemarksType() {
        return remarksType;
    }

    public void setRemarksType(String remarksType) {
        this.remarksType = remarksType;
    }
}
