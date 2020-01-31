package com.hkt.btu.common.core.dao.entity;


public class BtuReportProfileEntity extends BaseEntity {

    private String reportId;
    private String reportName;
    private String cronExp;
    private String status;
    private String sql;
    private String reportClass;
    private String emailTo;

    public String getReportId() {
        return reportId;
    }

    public void setReportId(String reportId) {
        this.reportId = reportId;
    }

    public String getReportName() {
        return reportName;
    }

    public void setReportName(String reportName) {
        this.reportName = reportName;
    }

    public String getCronExp() {
        return cronExp;
    }

    public void setCronExp(String cronExp) {
        this.cronExp = cronExp;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getSql() {
        return sql;
    }

    public void setSql(String sql) {
        this.sql = sql;
    }

    public String getEmailTo() {
        return emailTo;
    }

    public void setEmailTo(String emailTo) {
        this.emailTo = emailTo;
    }

    public String getReportClass() {
        return reportClass;
    }

    public void setReportClass(String reportClass) {
        this.reportClass = reportClass;
    }
}
