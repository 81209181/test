package com.hkt.btu.sd.core.service;

import com.hkt.btu.sd.core.service.bean.SdServiceTypeBean;
import com.hkt.btu.sd.core.service.bean.SdSymptomBean;
import com.hkt.btu.sd.core.service.bean.SdSymptomMappingBean;

import java.util.List;

public interface SdSymptomService {

    List<SdSymptomBean> getSymptomGroupList();

    void createSymptom(String symptomCode,String symptomGroupCode,String symptomDescription);

    List<SdServiceTypeBean> getServiceTypeList();

    List<SdSymptomBean> getAllSymptom();

    SdSymptomBean getSymptomBySymptomCode(String symptomCode);

    void deleteSymptomMapping(String symptomCode);

    void createSymptomMapping(String serviceTypeCode, String symptomCode);

    List<SdSymptomMappingBean> getSymptomMapping(String symptomCode);
}
