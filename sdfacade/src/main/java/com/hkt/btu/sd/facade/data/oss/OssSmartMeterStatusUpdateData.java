package com.hkt.btu.sd.facade.data.oss;

import com.hkt.btu.common.facade.data.DataInterface;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

public class OssSmartMeterStatusUpdateData implements DataInterface {

    private Integer poleID;
    private Integer ticketId;
    private String time;
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

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }
}
