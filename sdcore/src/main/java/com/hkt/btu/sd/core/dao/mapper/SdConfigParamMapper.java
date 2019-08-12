package com.hkt.btu.sd.core.dao.mapper;

import com.hkt.btu.sd.core.dao.entity.SdConfigParamEntity;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SdConfigParamMapper {

    List<SdConfigParamEntity> getAllConfigParam();

    SdConfigParamEntity getValue(@Param("configGroup") String configGroup, @Param("configKey") String configKey);

    List<SdConfigParamEntity> getValuesByConfigGroup(@Param("configGroup") String configGroup);

    int updateValue(@Param("configGroup") String configGroup, @Param("configKey") String configKey,
                     @Param("configValue") String configValue, @Param("configValueType") String configValueType,
                     @Param("modifyBy") String modifyBy);

    void deleteConfig(@Param("configGroup") String configGroup, @Param("configKey") String configKey);

    List<String> getConfigGroupList();
}
