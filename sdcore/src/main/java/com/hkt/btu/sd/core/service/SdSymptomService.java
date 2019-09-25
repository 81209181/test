package com.hkt.btu.sd.core.service;

import com.hkt.btu.sd.core.service.bean.SdServiceTypeBean;
import com.hkt.btu.sd.core.service.bean.SdSymptomBean;

import java.util.List;

public interface SdSymptomService {

    List<SdSymptomBean> getSymptomGroupList();

    void createSymptom(String symptomCode,String symptomGroupCode,String symptomDescription);

    List<SdServiceTypeBean> getServiceTypeList();

    void createSymptomMapping(String serviceTypeCode, String symptomGroupCode);

    boolean checkSymptomMapping(String serviceTypeCode, String symptomGroupCode);

    void deleteSymptomMapping(String oldServiceTypeCode, String oldSymptomGroupCode);

    boolean checkSymptom(String symptomCode);
}
