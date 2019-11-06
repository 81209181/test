package com.hkt.btu.sd.core.service.bean;

import com.hkt.btu.common.core.service.bean.BaseBean;

import java.time.LocalDateTime;

public class SdTicketMasBean extends BaseBean {

    public static class STATUS_TYPE {
        public static final String OPEN = "OPEN";
        public static final String WORKING = "WORKING";
        public static final String COMPLETE = "COMPLETE";
    }

    public static class STATUS_TYPE_CODE {
        public static final String OPEN = "O";
        public static final String WORKING = "W";
        public static final String COMPLETE = "CP";
    }

    private int ticketMasId ;

    private String custCode;

    private String ticketType;

    private String status;

    private LocalDateTime appointmentDate;

    private String asap;

    private int callInCount;

    private String searchKey;

    private String searchValue;

    private LocalDateTime completeDate;

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

    public int getCallInCount() {
        return callInCount;
    }

    public void setCallInCount(int callInCount) {
        this.callInCount = callInCount;
    }

    public String getSearchKey() {
        return searchKey;
    }

    public void setSearchKey(String searchKey) {
        this.searchKey = searchKey;
    }

    public String getSearchValue() {
        return searchValue;
    }

    public void setSearchValue(String searchValue) {
        this.searchValue = searchValue;
    }

    public LocalDateTime getCompleteDate() {
        return completeDate;
    }

    public void setCompleteDate(LocalDateTime completeDate) {
        this.completeDate = completeDate;
    }
}
