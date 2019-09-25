package com.hkt.btu.sd.facade;


import com.hkt.btu.sd.facade.data.EditResultData;
import com.hkt.btu.sd.facade.data.SdServiceTypeData;
import com.hkt.btu.sd.facade.data.SdSymptomData;
import com.hkt.btu.sd.facade.data.SdSymptomMappingData;

import java.util.List;

public interface SdSymptomFacade {

    List<SdSymptomData> getSymptomGroupList();

    String createSymptom(String symptomCode,String symptomGroupCode,String symptomDescription);

    List<SdServiceTypeData> getServiceTypeList();

    List<SdSymptomData> getAllSymptom();

    SdSymptomData getSymptomBySymptomCode(String symptomCode);

    String editSymptomMapping(SdSymptomMappingData symptomMappingData);

    EditResultData getSymptomMapping(String symptomCode);
}
