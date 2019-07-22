package com.hkt.btu.noc.core.service.bean;

import com.hkt.btu.common.core.service.bean.BaseBean;

public class NocAccessRequestLocBean extends BaseBean {
    private String locId;
    private String name;


    public String getLocId() {
        return locId;
    }

    public void setLocId(String locId) {
        this.locId = locId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
