package com.hkt.btu.sd.facade.data;

import com.hkt.btu.common.facade.data.DataInterface;

import java.util.List;

public class SdDnGroupData implements DataInterface {
    private String asCluster;
    private String adminPortalId;
    private String cliNumber;
    private String recordingProvisionMethod;
    private String dod;
    private Integer cac;
    private List<SdDnPlanData> userPlanList;

    public String getAsCluster() {
        return asCluster;
    }

    public void setAsCluster(String asCluster) {
        this.asCluster = asCluster;
    }

    public String getAdminPortalId() {
        return adminPortalId;
    }

    public void setAdminPortalId(String adminPortalId) {
        this.adminPortalId = adminPortalId;
    }

    public String getCliNumber() {
        return cliNumber;
    }

    public void setCliNumber(String cliNumber) {
        this.cliNumber = cliNumber;
    }

    public String getRecordingProvisionMethod() {
        return recordingProvisionMethod;
    }

    public void setRecordingProvisionMethod(String recordingProvisionMethod) {
        this.recordingProvisionMethod = recordingProvisionMethod;
    }

    public String getDod() {
        return dod;
    }

    public void setDod(String dod) {
        this.dod = dod;
    }

    public Integer getCac() {
        return cac;
    }

    public void setCac(Integer cac) {
        this.cac = cac;
    }

    public List<SdDnPlanData> getUserPlanList() {
        return userPlanList;
    }

    public void setUserPlanList(List<SdDnPlanData> userPlanList) {
        this.userPlanList = userPlanList;
    }
}
