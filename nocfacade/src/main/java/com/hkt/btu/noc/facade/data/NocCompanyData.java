package com.hkt.btu.noc.facade.data;

import com.hkt.btu.common.facade.data.DataInterface;

public class NocCompanyData implements DataInterface {
    private Integer companyId;
    private String name;
    private String remarks;


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

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }
}
