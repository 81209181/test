package com.hkt.btu.sd.core.service.bean;

import com.hkt.btu.common.core.service.bean.BaseBean;
import com.hkt.btu.sd.core.service.constant.TicketPriorityEnum;
import com.hkt.btu.sd.core.service.constant.TicketStatusEnum;
import com.hkt.btu.sd.core.service.constant.TicketTypeEnum;

import java.time.LocalDateTime;

public class SdTicketMasBean extends BaseBean {

    public static class STATUS_DESC {
        public static final String OPEN = "OPEN";
        public static final String WORKING = "WORKING";
        public static final String CLOSE = "CLOSE";
        public static final String COMPLETE = "COMPLETE";
    }

    private int ticketMasId;
    private String serviceNumber;
    private TicketTypeEnum ticketType;
    private TicketStatusEnum status;
    private TicketPriorityEnum priority;

    private String custCode;
    private LocalDateTime appointmentDate;
    private String asap;
    private int callInCount;
    private String searchKey;
    private String searchValue;
    private LocalDateTime arrivalDate;
    private LocalDateTime completeDate;
    private String serviceType;
    private String owningRole;
    private String custName;

    public String getCustName() {
        return custName;
    }

    public void setCustName(String custName) {
        this.custName = custName;
    }

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

    public TicketTypeEnum getTicketType() {
        return ticketType;
    }

    public void setTicketType(TicketTypeEnum ticketType) {
        this.ticketType = ticketType;
    }

    public TicketStatusEnum getStatus() {
        return status;
    }

    public void setStatus(TicketStatusEnum status) {
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

    public LocalDateTime getArrivalDate() {
        return arrivalDate;
    }

    public void setArrivalDate(LocalDateTime arrivalDate) {
        this.arrivalDate = arrivalDate;
    }

    public LocalDateTime getCompleteDate() {
        return completeDate;
    }

    public void setCompleteDate(LocalDateTime completeDate) {
        this.completeDate = completeDate;
    }

    public String getServiceType() {
        return serviceType;
    }

    public void setServiceType(String serviceType) {
        this.serviceType = serviceType;
    }

    public String getOwningRole() {
        return owningRole;
    }

    public void setOwningRole(String owningRole) {
        this.owningRole = owningRole;
    }

    public String getServiceNumber() {
        return serviceNumber;
    }

    public void setServiceNumber(String serviceNumber) {
        this.serviceNumber = serviceNumber;
    }

    public TicketPriorityEnum getPriority() {
        return priority;
    }

    public void setPriority(TicketPriorityEnum priority) {
        this.priority = priority;
    }
}
