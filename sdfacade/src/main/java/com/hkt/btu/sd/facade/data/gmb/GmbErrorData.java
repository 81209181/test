package com.hkt.btu.sd.facade.data.gmb;

import com.hkt.btu.common.facade.data.DataInterface;

public class GmbErrorData implements DataInterface {

    private String type;

    private boolean status;

    private String time;

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

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
