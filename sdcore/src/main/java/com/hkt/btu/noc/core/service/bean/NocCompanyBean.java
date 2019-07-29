package com.hkt.btu.noc.core.service.bean;

import com.hkt.btu.common.core.service.bean.BaseBean;

public class NocCompanyBean extends BaseBean {
    private Integer companyId;
    private String name;

    public Integer getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Integer companyId) {
        this.companyId = companyId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
