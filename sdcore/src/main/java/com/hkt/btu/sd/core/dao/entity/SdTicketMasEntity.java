package com.hkt.btu.sd.core.dao.entity;

import com.hkt.btu.common.core.dao.entity.BaseEntity;

import java.time.LocalDateTime;

public class SdTicketMasEntity extends BaseEntity {

    private int ticketMasId ;

    private String custCode;

    private String ticketType;

    private String status;

    private LocalDateTime appointmentDate;

    private String asap;

    public LocalDateTime getAppointmentDate() {
        return appointmentDate;
    }

    public void setAppointmentDate(LocalDateTime appointmentDate) {
        this.appointmentDate = appointmentDate;
    }

    public String getAsap() {
        return asap;
    }

    public void setAsap(String asap) {
        this.asap = asap;
    }

    public int getTicketMasId() {
        return ticketMasId;
    }

    public void setTicketMasId(int ticketMasId) {
        this.ticketMasId = ticketMasId;
    }

    public String getCustCode() {
        return custCode;
    }

    public void setCustCode(String custCode) {
        this.custCode = custCode;
    }

    public String getTicketType() {
        return ticketType;
    }

    public void setTicketType(String ticketType) {
        this.ticketType = ticketType;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

}
