package com.hkt.btu.sd.core.service.bean;

import com.hkt.btu.common.core.service.bean.BaseBean;

public class SdGmbTicketEavBean extends BaseBean {

    private Integer ticketMasId;
    private String psl;
    private String routeNo;

    public Integer getTicketMasId() {
        return ticketMasId;
    }

    public void setTicketMasId(Integer ticketMasId) {
        this.ticketMasId = ticketMasId;
    }

    public String getPsl() {
        return psl;
    }

    public void setPsl(String psl) {
        this.psl = psl;
    }

    public String getRouteNo() {
        return routeNo;
    }

    public void setRouteNo(String routeNo) {
        this.routeNo = routeNo;
    }
}
