package com.hkt.btu.sd.facade.data;

public class WfmSfBeanListData {
    private String subFaultCode;
    private String subFaultId;
    private String symptom;
    private String description;
    private Boolean majorIncident;

    public String getSubFaultCode() {
        return subFaultCode;
    }

    public void setSubFaultCode(String subFaultCode) {
        this.subFaultCode = subFaultCode;
    }

    public String getSubFaultId() {
        return subFaultId;
    }

    public void setSubFaultId(String subFaultId) {
        this.subFaultId = subFaultId;
    }

    public String getSymptom() {
        return symptom;
    }

    public void setSymptom(String symptom) {
        this.symptom = symptom;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean getMajorIncident() {
        return majorIncident;
    }

    public void setMajorIncident(Boolean majorIncident) {
        this.majorIncident = majorIncident;
    }
}
