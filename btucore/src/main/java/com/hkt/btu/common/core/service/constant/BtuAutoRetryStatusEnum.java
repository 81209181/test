package com.hkt.btu.common.core.service.constant;

import com.hkt.btu.common.core.dao.entity.BtuAutoRetryEntity;
import com.hkt.btu.common.core.service.bean.BtuAutoRetryBean;
import org.apache.commons.lang3.StringUtils;

public enum BtuAutoRetryStatusEnum {
    ACTIVE(BtuAutoRetryEntity.STATUS.ACTIVE, BtuAutoRetryBean.STATUS.ACTIVE),
    COMPLETED(BtuAutoRetryEntity.STATUS.COMPLETED, BtuAutoRetryBean.STATUS.COMPLETED),
    CANCELLED(BtuAutoRetryEntity.STATUS.CANCELLED, BtuAutoRetryBean.STATUS.CANCELLED);

    private String statusCode;
    private String desc;

    BtuAutoRetryStatusEnum(String statusCode, String desc) {
        this.statusCode = statusCode;
        this.desc = desc;
    }

    public static BtuAutoRetryStatusEnum getEnum(String statusCode) {
        for(BtuAutoRetryStatusEnum e : values()){
            if(StringUtils.equals(statusCode, e.statusCode)){
                return e;
            }
        }
        return null;
    }

    public String getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
