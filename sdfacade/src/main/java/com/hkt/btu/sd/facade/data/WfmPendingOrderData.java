package com.hkt.btu.sd.facade.data;

import com.hkt.btu.common.facade.data.DataInterface;

public class WfmPendingOrderData implements DataInterface {

    private String pendingOrder;
    private String errorMsg;

    private String orderId;
    private String fulfillmentId;
    private String orderType;
    private String fulfillmentType;
    private String srd;
    private String appointmentDate;
    private String appointmentStartDateTime;
    private String appointmentEndDateTime;

    public String getPendingOrder() {
        return pendingOrder;
    }

    public void setPendingOrder(String pendingOrder) {
        this.pendingOrder = pendingOrder;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

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

    public String getSrd() {
        return srd;
    }

    public void setSrd(String srd) {
        this.srd = srd;
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
}
