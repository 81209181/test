package com.hkt.btu.noc.core.dao.entity;

import com.hkt.btu.common.core.dao.entity.BaseEntity;

public class NocCompanyEntity extends BaseEntity {

    public static class STATUS{
        public static final String ACTIVE = "A";
        public static final String DISABLE = "D";
    }


    private Integer companyId;
    private String name;
    private String status;


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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
