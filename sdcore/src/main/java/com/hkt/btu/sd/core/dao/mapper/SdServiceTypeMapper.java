package com.hkt.btu.sd.core.dao.mapper;

import com.hkt.btu.sd.core.dao.entity.SdServiceTypeEntity;
import com.hkt.btu.sd.core.dao.entity.SdServiceTypeOfferMappingEntity;
import org.apache.ibatis.annotations.Param;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SdServiceTypeMapper {

    List<SdServiceTypeEntity> getServiceTypeList();

    List<SdServiceTypeOfferMappingEntity> getServiceTypeOfferMapping();

    void removeAll();

    void insertServiceTypeOfferMapping(@Param("serviceTypeCode") String serviceTypeCode, @Param("offerName") String offerName, @Param("createby") String createby);

    void updateServiceTypeMappingByCodeAndOfferName(@Param("oldServiceTypeCode") String oldServiceTypeCode, @Param("serviceTypeCode") String serviceTypeCode,
                                                    @Param("oldOfferName") String oldOfferName, @Param("offerName") String offerName, @Param("modifyby") String modifyby);

    SdServiceTypeOfferMappingEntity getServiceTypeOfferMappingByCodeAndOfferName(@Param("serviceTypeCode") String oldServiceTypeCode,
                                                                                 @Param("offerName") String oldOfferName);

    void deleteServiceTypeOfferMapping(@Param("serviceTypeCode") String serviceTypeCode, @Param("offerName") String offerName);

    List<SdServiceTypeEntity> getServiceTypeByRoleId(@Param("userRoleId") List<String> userRoleId);
}
