package com.hkt.btu.sd.core.service.bean;

import com.hkt.btu.common.core.service.bean.BtuUserRolePathCtrlBean;

import java.time.LocalDateTime;

public class SdUserRolePathCtrlBean extends BtuUserRolePathCtrlBean {

    private String description;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
