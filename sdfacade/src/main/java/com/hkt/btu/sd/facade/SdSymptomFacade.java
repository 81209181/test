package com.hkt.btu.sd.facade;


import com.hkt.btu.sd.facade.data.SdSymptomData;

import java.util.List;

public interface SdSymptomFacade {

    List<SdSymptomData> getSymptomGroupList();

    boolean createSymptom(String symptomCode,String symptomGroupCode,String symptomDescription);
}
