package com.hkt.btu.sd.core.service.bean;

import com.hkt.btu.common.core.service.bean.BtuSqlReportBean;

public class SdSqlReportBean extends BtuSqlReportBean {

    private String sql;
    private String exportTo;
    private String emailTo;

    public String getSql() {
        return sql;
    }

    public void setSql(String sql) {
        this.sql = sql;
    }

    public String getExportTo() {
        return exportTo;
    }

    public void setExportTo(String exportTo) {
        this.exportTo = exportTo;
    }

    public String getEmailTo() {
        return emailTo;
    }

    public void setEmailTo(String emailTo) {
        this.emailTo = emailTo;
    }
}
