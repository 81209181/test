package com.hkt.btu.noc.core.dao.mapper;

import com.hkt.btu.noc.core.dao.entity.NocConfigParamEntity;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NocConfigParamMapper {

    List<NocConfigParamEntity> getAllConfigParam();

    NocConfigParamEntity getValue(@Param("configGroup") String configGroup, @Param("configKey") String configKey);
    List<NocConfigParamEntity> getValuesByConfigGroup(@Param("configGroup") String configGroup);

    void updateValue(@Param("configGroup") String configGroup, @Param("configKey") String configKey,
                     @Param("configValue") String configValue, @Param("configValueType") String configValueType,
                     @Param("modifyBy") Integer modifyBy);

    void deleteConfig(@Param("configGroup") String configGroup, @Param("configKey") String configKey);
}
