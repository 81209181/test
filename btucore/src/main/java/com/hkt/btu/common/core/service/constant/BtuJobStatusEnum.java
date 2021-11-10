package com.hkt.btu.common.core.service.constant;

import com.hkt.btu.common.core.dao.entity.BtuCronJobEntity;
import com.hkt.btu.common.core.service.bean.BtuCronJobProfileBean;
import org.apache.commons.lang3.StringUtils;

public enum BtuJobStatusEnum {
    ACTIVE(BtuCronJobEntity.STATUS.ACTIVE, BtuCronJobProfileBean.STATUS.ACTIVE),
    DISABLE(BtuCronJobEntity.STATUS.DISABLE, BtuCronJobProfileBean.STATUS.DISABLE),
    UNKNOWN(BtuCronJobEntity.STATUS.UNKNOWN, BtuCronJobProfileBean.STATUS.UNKNOWN);

    private String statusCode;
    private String desc;

    BtuJobStatusEnum(String statusCode, String desc) {
        this.statusCode = statusCode;
        this.desc = desc;
    }

    public static BtuJobStatusEnum getEnum(String statusCode) {
        for(BtuJobStatusEnum e : values()){
            if(StringUtils.equals(statusCode, e.statusCode)){
                return e;
            }
        }
        return UNKNOWN;
    }

    public String getStatusCode() {
        return statusCode;
    }

    public String getDesc() {
        return desc;
    }
}
