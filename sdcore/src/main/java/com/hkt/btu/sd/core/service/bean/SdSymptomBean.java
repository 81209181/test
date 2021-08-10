package com.hkt.btu.sd.core.service.bean;

import com.hkt.btu.common.core.service.bean.BaseBean;

public class SdSymptomBean extends BaseBean {

    private String symptomCode;
    private String symptomDescription;
    private String symptomGroupCode;
    private String symptomGroupName;
    private String voiceLineTest;
    private String allowAppt;

    public String getSymptomCode() {
        return symptomCode;
    }

    public void setSymptomCode(String symptomCode) {
        this.symptomCode = symptomCode;
    }

    public String getSymptomDescription() {
        return symptomDescription;
    }

    public void setSymptomDescription(String symptomDescription) {
        this.symptomDescription = symptomDescription;
    }

    public String getSymptomGroupCode() {
        return symptomGroupCode;
    }

    public void setSymptomGroupCode(String symptomGroupCode) {
        this.symptomGroupCode = symptomGroupCode;
    }

    public String getSymptomGroupName() {
        return symptomGroupName;
    }

    public void setSymptomGroupName(String symptomGroupName) {
        this.symptomGroupName = symptomGroupName;
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
