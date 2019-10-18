package com.hkt.btu.sd.facade.data;

import com.hkt.btu.common.facade.data.DataInterface;

public class ResponseTicketData implements DataInterface {

    private boolean success;
    private Object data;

    public static ResponseTicketData of(boolean success, Object data) {
        return new ResponseTicketData(success, data);
    }

    private ResponseTicketData(boolean success, Object data) {
        this.success = success;
        this.data = data;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
