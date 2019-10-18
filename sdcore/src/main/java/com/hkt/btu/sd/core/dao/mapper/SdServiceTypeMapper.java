package com.hkt.btu.sd.core.dao.mapper;

import com.hkt.btu.sd.core.dao.entity.SdServiceTypeEntity;
import com.hkt.btu.sd.core.dao.entity.SdServiceTypeOfferMappingEntity;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SdServiceTypeMapper {

    List<SdServiceTypeEntity> getServiceTypeList();

    List<SdServiceTypeOfferMappingEntity> getServiceTypeOfferMapping();

    void removeAll();

    void insertServiceTypeOfferMapping(@Param("serviceTypeCode") String serviceTypeCode,@Param("offerName") String offerName);
}
