package com.hkt.btu.sd.core.service;

import com.hkt.btu.sd.core.service.bean.SdSymptomBean;

import java.util.List;

public interface SdSymptomService {

    List<SdSymptomBean> getSymptomGroupList();

    boolean createSymptom(String symptomCode,String symptomGroupCode,String symptomDescription);
}
