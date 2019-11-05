package com.hkt.btu.sd.facade.data.wfm;

import com.hkt.btu.common.facade.data.DataInterface;

import java.time.format.DateTimeFormatter;

public class WfmAppointmentResData implements DataInterface {
    public static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMddHHmm");

    private String appointmentDate;
    private String appointmentStartDateTime;
    private String appointmentEndDateTime;

    public String getAppointmentDate() {
        return appointmentDate;
    }

    public void setAppointmentDate(String appointmentDate) {
        this.appointmentDate = appointmentDate;
    }

    public String getAppointmentStartDateTime() {
        return appointmentStartDateTime;
    }

    public void setAppointmentStartDateTime(String appointmentStartDateTime) {
        this.appointmentStartDateTime = appointmentStartDateTime;
    }

    public String getAppointmentEndDateTime() {
        return appointmentEndDateTime;
    }

    public void setAppointmentEndDateTime(String appointmentEndDateTime) {
        this.appointmentEndDateTime = appointmentEndDateTime;
    }
}
