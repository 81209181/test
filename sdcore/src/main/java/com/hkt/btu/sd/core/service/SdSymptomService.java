package com.hkt.btu.sd.core.service;

import com.hkt.btu.sd.core.service.bean.SdSortBean;
import com.hkt.btu.sd.core.service.bean.SdSymptomBean;
import com.hkt.btu.sd.core.service.bean.SdSymptomGroupBean;
import com.hkt.btu.sd.core.service.bean.SdSymptomMappingBean;
import com.hkt.btu.sd.core.service.bean.SdSymptomWorkingPartyMappingBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface SdSymptomService {

    List<SdSymptomBean> getSymptomGroupList();

    String createSymptom(String symptomGroupCode,String symptomDescription,List<String> serviceTypeList,
                         String voiceLineTest, String apptMode);

    Page<SdSymptomBean> searchSymptomList(Pageable pageable, String symptomGroupCode, String symptomDescription,
                                          List<SdSortBean> sortList);

    SdSymptomBean getSymptomBySymptomCode(String symptomCode);

    List<SdSymptomMappingBean> getSymptomMapping(String symptomCode);

    void updateSymptom(String oldSymptomCode, String symptomCode, String symptomGroupCode, String symptomDescription,
                       String voiceLineTest, String apptMode);

    void editSymptomMapping(String oldSymptomCode, String symptomCode, List<String> serviceTypeList);

    List<SdSymptomBean> getAllSymptomList();

    boolean ifSymptomDescExist(String symptomDescription, String symptomGroupCode);

    void createSymptomGroup(String symptomGroupCode, String symptomGroupName, List<String> roleList);

    Optional<SdSymptomGroupBean> getSymptomGroup(String symptomGroupCode);

    @Transactional
    void updateSymptomGroup(String symptomGroupCode, String symptomGroupName, List<String> roleList);

    @Transactional
    void delSymptomGroup(String symptomGroupCode);

    List<SdSymptomWorkingPartyMappingBean> getSymptomWorkingPartyMappingList();

    void createSymptomWorkingPartyMapping(String symptomCode, String workingParty, String serviceTypeCode);

    void updateSymptomWorkingPartyMapping(String symptomCode, String workingParty, String serviceTypeCode);

    Optional<SdSymptomWorkingPartyMappingBean> getSymptomWorkingPartyMapping(String symptomCode);

    void delSymptomWorkingPartyMapping(String symptomCode);
}
