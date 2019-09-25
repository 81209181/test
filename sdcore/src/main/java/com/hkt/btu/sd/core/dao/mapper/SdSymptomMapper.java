package com.hkt.btu.sd.core.dao.mapper;

import com.hkt.btu.sd.core.dao.entity.SdServiceTypeEntity;
import com.hkt.btu.sd.core.dao.entity.SdSymptomEntity;
import com.hkt.btu.sd.core.dao.entity.SdSymptomMappingEntity;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SdSymptomMapper {

    List<SdSymptomEntity> getSymptomGroupList();

    void createSymptom(@Param("symptomCode") String symptomCode,
                          @Param("symptomGroupCode") String symptomGroupCode,
                          @Param("symptomDescription") String symptomDescription,
                          @Param("createby") String createby);

    List<SdServiceTypeEntity> getServiceTypeList();

    void createSymptomMapping(@Param("serviceTypeCode") String serviceTypeCode,
                             @Param("symptomGroupCode") String symptomGroupCode,
                             @Param("createby") String createby);

    SdSymptomMappingEntity getSymptomMapping(String serviceTypeCode, String symptomGroupCode);

    void deleteSymptomMapping(@Param("serviceTypeCode") String serviceTypeCode,
                              @Param("symptomGroupCode") String symptomGroupCode);

    SdSymptomEntity getSymptomBySymptomCode(@Param("symptomCode") String symptomCode);
}
