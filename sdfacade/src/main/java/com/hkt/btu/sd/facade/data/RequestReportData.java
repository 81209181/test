package com.hkt.btu.sd.facade.data;

import com.hkt.btu.common.facade.data.DataInterface;

public class RequestReportData implements DataInterface {

    private String reportName;
    private String exportTo;
    private String emailTo;
    private String sql;

    public String getReportName() {
        return reportName;
    }

    public void setReportName(String reportName) {
        this.reportName = reportName;
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

    public String getSql() {
        return sql;
    }

    public void setSql(String sql) {
        this.sql = sql;
    }
}
