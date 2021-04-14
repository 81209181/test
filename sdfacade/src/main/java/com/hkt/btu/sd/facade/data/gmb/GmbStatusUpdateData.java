package com.hkt.btu.sd.facade.data.gmb;

import com.hkt.btu.common.facade.data.DataInterface;

public class GmbStatusUpdateData implements DataInterface {

    private String plateNo;
    private Integer ticketID;
    private String time;
    private String action;

    public String getPlateNo() {
        return plateNo;
    }

    public void setPlateNo(String plateNo) {
        this.plateNo = plateNo;
    }

    public Integer getTicketID() {
        return ticketID;
    }

    public void setTicketID(Integer ticketID) {
        this.ticketID = ticketID;
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
