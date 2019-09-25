package com.hkt.btu.sd.facade;


import com.hkt.btu.sd.facade.data.SdServiceTypeData;
import com.hkt.btu.sd.facade.data.SdSymptomData;

import java.util.List;

public interface SdSymptomFacade {

    List<SdSymptomData> getSymptomGroupList();

    String createSymptom(String symptomCode,String symptomGroupCode,String symptomDescription);

    List<SdServiceTypeData> getServiceTypeList();

    String createSymptomMapping(String serviceTypeCode, String symptomGroupCode);

    String editSymptomMapping(String oldServiceTypeCode, String oldSymptomGroupCode, String serviceTypeCode, String symptomGroupCode);
}
