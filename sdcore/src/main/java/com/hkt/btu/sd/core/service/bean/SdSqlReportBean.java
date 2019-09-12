package com.hkt.btu.sd.core.service.bean;

import com.hkt.btu.common.core.service.bean.BtuSqlReportBean;

public class SdSqlReportBean extends BtuSqlReportBean {

    private String remarks;

    @Override
    public String getRemarks() {
        return remarks;
    }

    @Override
    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }
}
