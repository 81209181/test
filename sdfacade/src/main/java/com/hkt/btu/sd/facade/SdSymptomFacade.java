package com.hkt.btu.sd.facade;


import com.hkt.btu.common.facade.data.PageData;
import com.hkt.btu.sd.core.service.bean.SdSymptomGroupBean;
import com.hkt.btu.sd.facade.data.EditResultData;
import com.hkt.btu.sd.facade.data.SdSymptomData;
import com.hkt.btu.sd.facade.data.SdSymptomWorkingPartyMappingData;
import com.hkt.btu.sd.facade.data.SdUpdateSymptomFormData;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface SdSymptomFacade {

    List<SdSymptomData> getSymptomGroupList();

    String createSymptom(String symptomGroupCode, String symptomDescription, List<String> serviceTypeList,
                         String voiceLineTest, String apptMode);

    PageData<SdSymptomData> searchSymptomList(Pageable pageable, String symptomGroupCode, String symptomDescription,
                                              String strDirList, String strSortList);

    SdSymptomData getSymptomBySymptomCode(String symptomCode);

    String editSymptomMapping(SdUpdateSymptomFormData symptomFormData);

    EditResultData getSymptomMapping(String symptomCode);

    List<SdSymptomData> getAllSymptomList();

    boolean ifSymptomDescExist(SdSymptomData symptomData);

    String createSymptomGroup(String symptomGroupCode, String symptomGroupName, List<String> roleList);

    Optional<SdSymptomGroupBean> getSymptomGroup(String symptomGroupCode);

    String updateSymptomGroup(String symptomGroupCode, String symptomGroupName, List<String> roleList);

    String delSymptomGroup(String symptomGroupCode);

    List<SdSymptomWorkingPartyMappingData> getSymptomWorkingPartyMappingList();

    String createSymptomWorkingPartyMapping(String symptomCode, String workingParty, String serviceTypeCode);

    String updateSymptomWorkingPartyMapping(String symptomCode, String workingParty, String serviceTypeCode);

    SdSymptomWorkingPartyMappingData getSymptomWorkingPartyMapping(String symptomCode);

    String delSymptomWorkingPartyMapping(String symptomCode);
}
