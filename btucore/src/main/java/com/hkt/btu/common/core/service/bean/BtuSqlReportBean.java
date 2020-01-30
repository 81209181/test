package com.hkt.btu.common.core.service.bean;


public class BtuSqlReportBean extends BaseBean {

    public static final String KEY_GROUP = "REPORT";
    public static final String ACTIVE_STATUS = "A";
    public static final String DEACTIVE_STATUS = "D";
    public static final String REPORT_FOLDER_PATH = "/opt/report/";
    public static final String REPORT_JOBDATAMAP_KEY = "reportMetaData";

    private String reportId;
    private String reportName;
    private String cronExp;
    private String status;
    private String jobClass;
    private String sql;
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

    public String getJobClass() {
        return jobClass;
    }

    public void setJobClass(String jobClass) {
        this.jobClass = jobClass;
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
}
