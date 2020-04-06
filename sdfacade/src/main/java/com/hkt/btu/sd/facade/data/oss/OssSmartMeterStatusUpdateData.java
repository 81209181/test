package com.hkt.btu.sd.facade.data.oss;

import com.hkt.btu.common.facade.data.DataInterface;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

public class OssSmartMeterStatusUpdateData implements DataInterface {

    private Integer poleID;
    private Integer ticketId;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime arrivalTime;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime completeTime;

    private String action;


    public Integer getPoleID() {
        return poleID;
    }

    public void setPoleID(Integer poleID) {
        this.poleID = poleID;
    }

    public Integer getTicketId() {
        return ticketId;
    }

    public void setTicketId(Integer ticketId) {
        this.ticketId = ticketId;
    }

    public LocalDateTime getArrivalTime() {
        return arrivalTime;
    }

    public void setArrivalTime(LocalDateTime arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    public LocalDateTime getCompleteTime() {
        return completeTime;
    }

    public void setCompleteTime(LocalDateTime completeTime) {
        this.completeTime = completeTime;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }
}
