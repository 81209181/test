package com.hkt.btu.sd.facade.data.wfm;

import com.hkt.btu.common.facade.data.DataInterface;

import java.sql.Timestamp;

public class WfmJobData implements DataInterface {
    // Job info
    private int jobId = 0;
    private String deptId = "";
    private String sysId = "";
    private String status = "";
    private String assignTech = "";
    private Timestamp actionTimestamp;
    private Timestamp srTimestamp;
    private Timestamp apptTimestamp;
    private Timestamp apptSTime;
    private Timestamp apptETime;
    private Timestamp lastUpTimestampTimestamp;
    private String fieldInd = "";
    private String lastJobInd = "";
    private String alertFieldRemark = "";
    private String workgroup = "";
    private String resolver = "";


    public int getJobId() {
        return jobId;
    }

    public void setJobId(int jobId) {
        this.jobId = jobId;
    }

    public String getDeptId() {
        return deptId;
    }

    public void setDeptId(String deptId) {
        this.deptId = deptId;
    }

    public String getSysId() {
        return sysId;
    }

    public void setSysId(String sysId) {
        this.sysId = sysId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getAssignTech() {
        return assignTech;
    }

    public void setAssignTech(String assignTech) {
        this.assignTech = assignTech;
    }

    public Timestamp getActionTimestamp() {
        return actionTimestamp;
    }

    public void setActionTimestamp(Timestamp actionTimestamp) {
        this.actionTimestamp = actionTimestamp;
    }

    public Timestamp getSrTimestamp() {
        return srTimestamp;
    }

    public void setSrTimestamp(Timestamp srTimestamp) {
        this.srTimestamp = srTimestamp;
    }

    public Timestamp getApptTimestamp() {
        return apptTimestamp;
    }

    public void setApptTimestamp(Timestamp apptTimestamp) {
        this.apptTimestamp = apptTimestamp;
    }

    public Timestamp getApptSTime() {
        return apptSTime;
    }

    public void setApptSTime(Timestamp apptSTime) {
        this.apptSTime = apptSTime;
    }

    public Timestamp getApptETime() {
        return apptETime;
    }

    public void setApptETime(Timestamp apptETime) {
        this.apptETime = apptETime;
    }

    public Timestamp getLastUpTimestampTimestamp() {
        return lastUpTimestampTimestamp;
    }

    public void setLastUpTimestampTimestamp(Timestamp lastUpTimestampTimestamp) {
        this.lastUpTimestampTimestamp = lastUpTimestampTimestamp;
    }

    public String getFieldInd() {
        return fieldInd;
    }

    public void setFieldInd(String fieldInd) {
        this.fieldInd = fieldInd;
    }

    public String getLastJobInd() {
        return lastJobInd;
    }

    public void setLastJobInd(String lastJobInd) {
        this.lastJobInd = lastJobInd;
    }

    public String getAlertFieldRemark() {
        return alertFieldRemark;
    }

    public void setAlertFieldRemark(String alertFieldRemark) {
        this.alertFieldRemark = alertFieldRemark;
    }

    public String getWorkgroup() {
        return workgroup;
    }

    public void setWorkgroup(String workgroup) {
        this.workgroup = workgroup;
    }

    public String getResolver() {
        return resolver;
    }

    public void setResolver(String resolver) {
        this.resolver = resolver;
    }
}