package com.hkt.btu.common.core.dao.entity;

public class BtuCronJobEntity extends BaseEntity {

    public static class STATUS{
        public static final String ACTIVE = "A";
        public static final String DISABLE = "D";
    }

    public static class MANDATORY{
        public static final String YES = "Y";
    }

    private Integer jobId;
    private String jobGroup;
    private String jobName;
    private String jobClass;
    private String cronExp;

    private String status;
    private String mandatory;



    public Integer getJobId() {
        return jobId;
    }

    public void setJobId(Integer jobId) {
        this.jobId = jobId;
    }

    public String getCronExp() {
        return cronExp;
    }

    public void setCronExp(String cronExp) {
        this.cronExp = cronExp;
    }

    public String getJobName() {
        return jobName;
    }

    public void setJobName(String jobName) {
        this.jobName = jobName;
    }

    public String getJobGroup() {
        return jobGroup;
    }

    public void setJobGroup(String jobGroup) {
        this.jobGroup = jobGroup;
    }

    public String getJobClass() {
        return jobClass;
    }

    public void setJobClass(String jobClass) {
        this.jobClass = jobClass;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMandatory() {
        return mandatory;
    }

    public void setMandatory(String mandatory) {
        this.mandatory = mandatory;
    }
}
