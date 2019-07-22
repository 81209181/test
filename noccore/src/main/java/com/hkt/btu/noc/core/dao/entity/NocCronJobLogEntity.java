package com.hkt.btu.noc.core.dao.entity;

import com.hkt.btu.common.core.dao.entity.BaseEntity;

public class NocCronJobLogEntity extends BaseEntity {

    public static class ACTION{
        public static final String ACTIVATE = "ACTIVATE";
        public static final String DEACTIVATE = "DEACTIVATE";

        public static final String PAUSE = "PAUSE";
        public static final String RESUME = "RESUME";
        public static final String TRIGGER = "TRIGGER";

        public static final String SKIP = "SKIP";
        public static final String COMPLETE = "CP";
        public static final String ERROR = "ERROR";
    }


    private String serverIp;

    private String jobGroup;
    private String jobName;
    private String jobClass;

    private String action;


    public String getServerIp() {
        return serverIp;
    }

    public void setServerIp(String serverIp) {
        this.serverIp = serverIp;
    }

    public String getJobGroup() {
        return jobGroup;
    }

    public void setJobGroup(String jobGroup) {
        this.jobGroup = jobGroup;
    }

    public String getJobName() {
        return jobName;
    }

    public void setJobName(String jobName) {
        this.jobName = jobName;
    }

    public String getJobClass() {
        return jobClass;
    }

    public void setJobClass(String jobClass) {
        this.jobClass = jobClass;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }
}
