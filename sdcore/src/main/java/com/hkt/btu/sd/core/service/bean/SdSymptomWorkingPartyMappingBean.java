package com.hkt.btu.sd.core.service.bean;

import com.hkt.btu.common.core.service.bean.BaseBean;

public class SdSymptomWorkingPartyMappingBean extends BaseBean {

    private String serviceTypeCode;
    private String symptomCode;
    private String workingParty;

    public String getServiceTypeCode() {
        return serviceTypeCode;
    }

    public void setServiceTypeCode(String serviceTypeCode) {
        this.serviceTypeCode = serviceTypeCode;
    }

    public String getSymptomCode() {
        return symptomCode;
    }

    public void setSymptomCode(String symptomCode) {
        this.symptomCode = symptomCode;
    }

    public String getWorkingParty() {
        return workingParty;
    }

    public void setWorkingParty(String workingParty) {
        this.workingParty = workingParty;
    }
}