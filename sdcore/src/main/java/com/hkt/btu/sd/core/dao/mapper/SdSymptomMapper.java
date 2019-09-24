package com.hkt.btu.sd.core.dao.mapper;

import com.hkt.btu.sd.core.dao.entity.SdSymptomEntity;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SdSymptomMapper {

    List<SdSymptomEntity> getSymptomGroupList();

    int insertSymptom(@Param("symptomCode") String symptomCode,
                          @Param("symptomGroupCode") String symptomGroupCode,
                          @Param("symptomDescription") String symptomDescription,
                          @Param("createby") String createby);
}
