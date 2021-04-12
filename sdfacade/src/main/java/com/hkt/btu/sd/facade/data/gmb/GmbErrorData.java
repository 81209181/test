package com.hkt.btu.sd.facade.data.gmb;

import com.hkt.btu.common.facade.data.DataInterface;

import java.time.LocalDateTime;

public class GmbErrorData implements DataInterface {

    private String type;

    private boolean status;

    private LocalDateTime time;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public LocalDateTime getTime() {
        return time;
    }

    public void setTime(LocalDateTime time) {
        this.time = time;
    }
}
