package com.hkt.btu.common.core.dao.mapper;

import com.hkt.btu.common.core.dao.entity.BtuConfigParamEntity;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface BtuConfigParamMapper {
    List<BtuConfigParamEntity> getValuesByConfigGroup(@Param("configGroup") String configGroup);

    BtuConfigParamEntity getValue(@Param("configGroup") String configGroup, @Param("configKey") String configKey);

    List<BtuConfigParamEntity> getAllConfigParam();

    void updateValue(@Param("configGroup") String configGroup, @Param("configKey") String configKey,
                     @Param("configValue") String configValue, @Param("configValueType") String configValueType,
                     @Param("modifyBy") Integer modifyBy);

    void deleteConfig(@Param("configGroup") String configGroup, @Param("configKey") String configKey);
}
