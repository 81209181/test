package com.hkt.btu.sd.core.dao.mapper;

import com.hkt.btu.sd.core.dao.entity.SdSortEntity;
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

    SdSymptomEntity getSymptomBySymptomCode(@Param("symptomCode") String symptomCode);

    List<SdSymptomEntity> searchSymptomList(@Param("offset") long offset,
                                            @Param("pageSize") int pageSize,
                                            @Param("symptomGroupCode") String symptomGroupCode,
                                            @Param("symptomDescription") String symptomDescription,
                                            @Param("sortList") List<SdSortEntity> sortList);

    Integer searchSymptomCount(@Param("symptomGroupCode") String symptomGroupCode,
                               @Param("symptomDescription") String symptomDescription);

    void deleteSymptomMapping(@Param("symptomCode")String symptomCode, List<String> serviceTypeList);

    void createSymptomMapping(List<String> serviceTypeList,
                              @Param("symptomCode") String symptomCode,
                              @Param("createby") String createby);

    List<SdSymptomMappingEntity> getSymptomMapping(@Param("symptomCode") String symptomCode);

    Integer updateSymptom(@Param("oldSymptomCode") String oldSymptomCode,
                          @Param("symptomCode") String symptomCode,
                          @Param("symptomGroupCode") String symptomGroupCode,
                          @Param("symptomDescription") String symptomDescription,
                          @Param("createby") String createby);

    List<SdSymptomEntity> getAllSymptomList();
}
