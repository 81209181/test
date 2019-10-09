package com.hkt.btu.sd.core.service;

import com.hkt.btu.sd.core.service.bean.SdServiceTypeBean;
import com.hkt.btu.sd.core.service.bean.SdSymptomBean;
import com.hkt.btu.sd.core.service.bean.SdSymptomMappingBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface SdSymptomService {

    List<SdSymptomBean> getSymptomGroupList();

    void createSymptom(String symptomCode,String symptomGroupCode,String symptomDescription);

    List<SdServiceTypeBean> getServiceTypeList();

    Page<SdSymptomBean> searchSymptomList(Pageable pageable, String symptomGroupCode, String symptomDescription);

    SdSymptomBean getSymptomBySymptomCode(String symptomCode);

    List<SdSymptomMappingBean> getSymptomMapping(String symptomCode);

    void editSymptomMapping(String symptomCode, List<String> serviceTypeList);
}
