package com.hkt.btu.sd.facade;


import com.hkt.btu.common.facade.data.PageData;
import com.hkt.btu.sd.facade.data.EditResultData;
import com.hkt.btu.sd.facade.data.SdServiceTypeData;
import com.hkt.btu.sd.facade.data.SdSymptomData;
import com.hkt.btu.sd.facade.data.UpdateSymptomFormData;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface SdSymptomFacade {

    List<SdSymptomData> getSymptomGroupList();

    String createSymptom(String symptomCode,String symptomGroupCode,String symptomDescription);

    PageData<SdSymptomData> searchSymptomList(Pageable pageable, String symptomGroupCode, String symptomDescription);

    SdSymptomData getSymptomBySymptomCode(String symptomCode);

    String editSymptomMapping(UpdateSymptomFormData symptomFormData);

    EditResultData getSymptomMapping(String symptomCode);
}
