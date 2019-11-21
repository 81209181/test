package com.hkt.btu.sd.facade.data.wfm;

import com.hkt.btu.common.facade.data.DataInterface;

public class WfmPendingOrderData implements DataInterface {

    private String orderId;
    private String fulfillmentId;
    private String orderType;
    private String fulfillmentType;
    private String serviceReadyDate;
    private String srdStartDateTime;
    private String srdEndDateTime;
    private String appointmentDate;
    private String appointmentStartDateTime;
    private String appointmentEndDateTime;

    private String errorMsg;

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getFulfillmentId() {
        return fulfillmentId;
    }

    public void setFulfillmentId(String fulfillmentId) {
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

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public String getServiceReadyDate() {
        return serviceReadyDate;
    }

    public void setServiceReadyDate(String serviceReadyDate) {
        this.serviceReadyDate = serviceReadyDate;
    }
}
