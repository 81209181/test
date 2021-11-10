package com.hkt.btu.common.core.service.bean;


import org.quartz.JobDataMap;


public class BtuCronJobProfileBean extends BaseBean{

    public static class STATUS{
        public static final String ACTIVE = "Active";
        public static final String DISABLE = "Disable";
        public static final String UNKNOWN = "Unknown";
    }

    private String keyGroup;
    private String keyName;
    private String jobClass;

    private String status;
    private boolean isActive;
    private boolean isMandatory;
    private String cronExp;

    private JobDataMap jobDataMap;

    public String getKeyGroup() {
        return keyGroup;
    }

    public void setKeyGroup(String keyGroup) {
        this.keyGroup = keyGroup;
    }

    public String getKeyName() {
        return keyName;
    }

    public void setKeyName(String keyName) {
        this.keyName = keyName;
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

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public boolean isMandatory() {
        return isMandatory;
    }

    public void setMandatory(boolean mandatory) {
        isMandatory = mandatory;
    }

    public String getCronExp() {
        return cronExp;
    }

    public void setCronExp(String cronExp) {
        this.cronExp = cronExp;
    }

    public JobDataMap getJobDataMap() {
        return jobDataMap;
    }

    public void setJobDataMap(JobDataMap jobDataMap) {
        this.jobDataMap = jobDataMap;
    }
}
