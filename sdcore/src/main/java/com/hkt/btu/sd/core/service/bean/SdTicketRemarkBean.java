package com.hkt.btu.sd.core.service.bean;

import com.hkt.btu.common.core.service.bean.BaseBean;

public class SdTicketRemarkBean extends BaseBean {

    public static class REMARKS_TYPE{
        public static final String CUSTOMER = "Customer";
        public static final String FIELD = "Field";
        public static final String SYSTEM = "System";
    }

    public static class REMARKS{
        public static final String CUSTOMER_CALL_IN = "Customer call-in.";
        public static final String STATUS_TO_OPEN = "Status --> OPEN";
        public static final String STATUS_TO_WORKING = "Status --> WORKING";
        public static final String STATUS_TO_CLOSE = "Status --> CLOSE (reason: %s, %s, by %s)";
//        public static final String VIEW_TICKET = "%s viewed ticket.";
    }

    private int ticketMasId ;
    private String remarksType;

    public int getTicketMasId() {
        return ticketMasId;
    }

    public void setTicketMasId(int ticketMasId) {
        this.ticketMasId = ticketMasId;
    }

    public String getRemarksType() {
        return remarksType;
    }

    public void setRemarksType(String remarksType) {
        this.remarksType = remarksType;
    }
}
