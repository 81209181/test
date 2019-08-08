package com.hkt.btu.common.core.service.bean;


public class BtuCronJobProfileBean extends BaseBean{

    private String keyGroup;
    private String keyName;
    private String jobClass;

    private String status;
    private boolean isActive;
    private boolean isMandatory;
    private String cronExp;

    public static BtuCronJobProfileBean getSampleJob() {
        BtuCronJobProfileBean bean = new BtuCronJobProfileBean();
        bean.setJobClass("com.hkt.btu.common.core.job.BtuSampleJob");
        bean.setKeyName("BtuSampleJob");
        bean.setKeyGroup("SYSTEM");
        bean.setCronExp("0 0/5 * * * ?");
        bean.setActive(true);
        bean.setMandatory(true);
        return bean;
    }

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

}
