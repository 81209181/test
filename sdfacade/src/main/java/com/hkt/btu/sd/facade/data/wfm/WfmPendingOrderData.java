package com.hkt.btu.sd.facade.data.wfm;

public class WfmPendingOrderData {

    private Long orderId;
    private Long fulfillmentId;
    private String orderType;
    private String fulfillmentType;
    private String srdStartDateTime;
    private String srdEndDateTime;
    private String appointmentDate;
    private String appointmentStartDateTime;
    private String appointmentEndDateTime;

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public Long getFulfillmentId() {
        return fulfillmentId;
    }

    public void setFulfillmentId(Long fulfillmentId) {
        this.fulfillmentId = fulfillmentId;
    }

    public String getOrderType() {
        return orderType;
    }

    public void setOrderType(String orderType) {
        this.orderType = orderType;
    }

    public String getFulfillmentType() {
        return fulfillmentType;
    }

    public void setFulfillmentType(String fulfillmentType) {
        this.fulfillmentType = fulfillmentType;
    }

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

    public String getSrdStartDateTime() {
        return srdStartDateTime;
    }

    public void setSrdStartDateTime(String srdStartDateTime) {
        this.srdStartDateTime = srdStartDateTime;
    }

    public String getSrdEndDateTime() {
        return srdEndDateTime;
    }

    public void setSrdEndDateTime(String srdEndDateTime) {
        this.srdEndDateTime = srdEndDateTime;
    }
}
