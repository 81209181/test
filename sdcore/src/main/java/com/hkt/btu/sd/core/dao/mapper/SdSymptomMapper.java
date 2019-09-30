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

    SdSymptomEntity getSymptomBySymptomCode(@Param("symptomCode") String symptomCode);

    List<SdSymptomEntity> getAllSymptom();

    void deleteSymptomMapping(@Param("symptomCode")String symptomCode);

    void createSymptomMapping(@Param("serviceTypeCode") String serviceTypeCode,
                              @Param("symptomCode") String symptomCode,
                              @Param("createby") String createby);

    List<SdSymptomMappingEntity> getSymptomMapping(@Param("symptomCode") String symptomCode);
}