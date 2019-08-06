package com.hkt.btu.sd.core.service.bean;

import com.hkt.btu.common.core.service.bean.BaseBean;

import java.time.LocalDateTime;

public class SdCronJobInstBean extends BaseBean {
    private String keyGroup;
    private String keyName;
    private String jobClass;

    // running instance in job scheduler
    private String currentCronExp;
    private LocalDateTime lastRunTime;
    private LocalDateTime nextRunTime;
    private Boolean isPaused;



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

    public String getCurrentCronExp() {
        return currentCronExp;
    }

    public void setCurrentCronExp(String currentCronExp) {
        this.currentCronExp = currentCronExp;
    }

    public LocalDateTime getLastRunTime() {
        return lastRunTime;
    }

    public void setLastRunTime(LocalDateTime lastRunTime) {
        this.lastRunTime = lastRunTime;
    }

    public LocalDateTime getNextRunTime() {
        return nextRunTime;
    }

    public void setNextRunTime(LocalDateTime nextRunTime) {
        this.nextRunTime = nextRunTime;
    }

    public Boolean getPaused() {
        return isPaused;
    }

    public void setPaused(Boolean paused) {
        isPaused = paused;
    }
}
