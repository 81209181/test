package com.hkt.btu.sd.core.dao.mapper;

import com.hkt.btu.sd.core.dao.entity.SdCloseCodeEntity;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SdCloseCodeMapper {

    List<SdCloseCodeEntity> getCloseCodeListByServiceType(@Param("serviceType") String serviceType);
}
