package com.hkt.btu.sd.core.service.bean;

import com.hkt.btu.common.core.service.bean.BaseBean;

public class SdServiceFaultsBean extends BaseBean {

    private int ticketFaultsId;
    private int ticketDetId;
    private String faults;

    public int getTicketFaultsId() {
        return ticketFaultsId;
    }

    public void setTicketFaultsId(int ticketFaultsId) {
        this.ticketFaultsId = ticketFaultsId;
    }

    public int getTicketDetId() {
        return ticketDetId;
    }

    public void setTicketDetId(int ticketDetId) {
        this.ticketDetId = ticketDetId;
    }

    public String getFaults() {
        return faults;
    }

    public void setFaults(String faults) {
        this.faults = faults;
    }
}
