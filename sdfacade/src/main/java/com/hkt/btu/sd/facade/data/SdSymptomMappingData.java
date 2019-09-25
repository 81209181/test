package com.hkt.btu.sd.facade.data;

import com.hkt.btu.common.facade.data.DataInterface;

import java.time.LocalDateTime;
import java.util.List;

public class SdSymptomMappingData implements DataInterface {

    private String serviceTypeCode;
    private String symptomCode;
    private List<String> serviceTypeList;

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

    public List<String> getServiceTypeList() {
        return serviceTypeList;
    }

    public void setServiceTypeList(List<String> serviceTypeList) {
        this.serviceTypeList = serviceTypeList;
    }
}
