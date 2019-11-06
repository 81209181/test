package com.hkt.btu.sd.facade.data;

import com.hkt.btu.common.facade.data.DataInterface;

public class AppointmentData implements DataInterface {
    private String appointmentDateStr;

    public String getAppointmentDateStr() {
        return appointmentDateStr;
    }

    public void setAppointmentDateStr(String appointmentDateStr) {
        this.appointmentDateStr = appointmentDateStr;
    }
}
