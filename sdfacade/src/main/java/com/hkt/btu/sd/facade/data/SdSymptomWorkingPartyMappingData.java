package com.hkt.btu.sd.facade.data;

import com.hkt.btu.common.facade.data.DataInterface;

public class SdSymptomWorkingPartyMappingData implements DataInterface {
    private String symptomCode;
    private String workingParty;
    private String serviceTypeCode;

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

    public String getServiceTypeCode() {
        return serviceTypeCode;
    }

    public void setServiceTypeCode(String serviceTypeCode) {
        this.serviceTypeCode = serviceTypeCode;
    }
}
