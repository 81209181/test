package com.hkt.btu.sd.core.service.constant;

import com.hkt.btu.sd.core.dao.entity.SdTicketMasEntity;
import com.hkt.btu.sd.core.service.bean.SdTicketMasBean;
import org.apache.commons.lang3.StringUtils;

public enum TicketStatusEnum {
    OPEN (SdTicketMasEntity.STATUS.OPEN, SdTicketMasBean.STATUS_DESC.OPEN),
    WORKING (SdTicketMasEntity.STATUS.WORKING, SdTicketMasBean.STATUS_DESC.WORKING),
    CLOSE (SdTicketMasEntity.STATUS.CLOSE, SdTicketMasBean.STATUS_DESC.CLOSE),
    COMPLETE (SdTicketMasEntity.STATUS.COMPLETE, SdTicketMasBean.STATUS_DESC.COMPLETE)
    ;

    TicketStatusEnum (String statusCode, String statusDesc){
        this.statusCode = statusCode;
        this.statusDesc = statusDesc;
    }

    public static TicketStatusEnum getEnum(String statusCode) {
        for(TicketStatusEnum serviceSearchEnum : values()){
            if(StringUtils.equals(statusCode, serviceSearchEnum.statusCode)){
                return serviceSearchEnum;
            }
        }
        return null;
    }

    private String statusCode;
    private String statusDesc;

    public String getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }

    public String getStatusDesc() {
        return statusDesc;
    }

    public void setStatusDesc(String statusDesc) {
        this.statusDesc = statusDesc;
    }
}
