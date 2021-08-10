package com.hkt.btu.sd.facade.data;

import com.hkt.btu.common.facade.data.DataInterface;

import java.util.List;


public class SdUpdateSymptomFormData implements DataInterface {
    private String symptomCode;
    private String symptomGroupCode;
    private String symptomDescription;
    private String oldSymptomCode;
    private List<String> serviceTypeList;
    private String voiceLineTest;
    private String allowAppt;

    public String getSymptomCode() {
        return symptomCode;
    }

    public void setSymptomCode(String symptomCode) {
        this.symptomCode = symptomCode;
    }

    public String getSymptomGroupCode() {
        return symptomGroupCode;
    }

    public void setSymptomGroupCode(String symptomGroupCode) {
        this.symptomGroupCode = symptomGroupCode;
    }

    public String getSymptomDescription() {
        return symptomDescription;
    }

    public void setSymptomDescription(String symptomDescription) {
        this.symptomDescription = symptomDescription;
    }

    public String getOldSymptomCode() {
        return oldSymptomCode;
    }

    public void setOldSymptomCode(String oldSymptomCode) {
        this.oldSymptomCode = oldSymptomCode;
    }

    public List<String> getServiceTypeList() {
        return serviceTypeList;
    }

    public void setServiceTypeList(List<String> serviceTypeList) {
        this.serviceTypeList = serviceTypeList;
    }

    public String getVoiceLineTest() {
        return voiceLineTest;
    }

    public void setVoiceLineTest(String voiceLineTest) {
        this.voiceLineTest = voiceLineTest;
    }

    public String getAllowAppt() {
        return allowAppt;
    }

    public void setAllowAppt(String allowAppt) {
        this.allowAppt = allowAppt;
    }
}
