package com.hkt.btu.sd.core.service.bean;

import com.hkt.btu.common.core.service.bean.BtuUserRolePathCtrlBean;

import java.time.LocalDateTime;

public class SdUserRolePathCtrlBean extends BtuUserRolePathCtrlBean {

    public static final String OPERATOR_LOGON_PATH = "/ticket/service-identity";

    public static final String DEFAULT_LOGON_PATH = "/user/";

    private String description;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
