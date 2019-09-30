package com.hkt.btu.sd.core.dao.entity;

import com.hkt.btu.common.core.dao.entity.BaseEntity;

public class SdServiceFaultsEntity extends BaseEntity {

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

    @Override
    public String toString() {
        return "SdServiceFaultsEntity{" +
                "ticketFaultsId=" + ticketFaultsId +
                ", ticketDetId=" + ticketDetId +
                ", faults='" + faults + '\'' +
                '}';
    }
}