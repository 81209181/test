package com.hkt.btu.sd.core.dao.mapper;

import com.hkt.btu.sd.core.dao.entity.SdSortEntity;
import com.hkt.btu.sd.core.dao.entity.SdSymptomCodePrefixEntity;
import com.hkt.btu.sd.core.dao.entity.SdSymptomEntity;
import com.hkt.btu.sd.core.dao.entity.SdSymptomGroupEntity;
import com.hkt.btu.sd.core.dao.entity.SdSymptomGroupRoleMappingEntity;
import com.hkt.btu.sd.core.dao.entity.SdSymptomMappingEntity;
import com.hkt.btu.sd.core.dao.entity.SdSymptomWorkingPartyMappingEntity;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SdSymptomMapper {

    List<SdSymptomGroupEntity> getSymptomGroupList();

    void createSymptom(@Param("symptomCode") String symptomCode,
                       @Param("symptomGroupCode") String symptomGroupCode,
                       @Param("symptomDescription") String symptomDescription,
                       @Param("createby") String createby,
                       @Param("voiceLineTest") String voiceLineTest,
                       @Param("apptMode") String apptMode);

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
                          @Param("createby") String createby,
                          @Param("voiceLineTest") String voiceLineTest,
                          @Param("apptMode") String apptMode);

    List<SdSymptomEntity> getAllSymptomList();

    List<SdSymptomWorkingPartyMappingEntity> getSymptomByServiceType(@Param("serviceType") String serviceType,
                                                                     @Param("workingParty") String workingParty);

    List<SdSymptomEntity> getSymptomByGroupCode(@Param("symptomGroupCode") String symptomGroupCode);

    SdSymptomCodePrefixEntity getSymptomCodePrefixByGroup(@Param("symptomGroupCode") String symptomGroupCode);

    void createSymptomGroup(@Param("symptomGroupCode") String symptomGroupCode,
                            @Param("symptomGroupName") String symptomGroupName,
                            @Param("createby") String createby,
                            @Param("modifyby") String modifyby);

    void createSymptomGroupRoleMapping(@Param("symptomGroupCode") String symptomGroupCode,
                                       @Param("roleList") List<String> roleList,
                                       @Param("createby") String createby,
                                       @Param("modifyby") String modifyby);

    List<SdSymptomGroupRoleMappingEntity> getSymptomGroupRoleMappingByCode(@Param("symptomGroupCode") String symptomGroupCode);

    SdSymptomGroupEntity getSymptomGroup(@Param("symptomGroupCode") String symptomGroupCode);

    void updateSymptomGroup(@Param("symptomGroupCode") String symptomGroupCode,
                            @Param("symptomGroupName") String symptomGroupName,
                            @Param("modifyby") String modifyby);

    void delSymptomGroupRoleMappingBatch(@Param("symptomGroupCode") String symptomGroupCode, @Param("roleList") List<String> roleList);

    void delSymptomGroup(@Param("symptomGroupCode") String symptomGroupCode);

    List<SdSymptomWorkingPartyMappingEntity> getSymptomWorkingPartyMappingList();

    void createSymptomWorkingPartyMapping(@Param("symptomCode") String symptomCode,
                                      @Param("workingParty") String workingParty,
                                      @Param("serviceTypeCode") String serviceTypeCode,
                                      @Param("createby") String createby,
                                      @Param("modifyby") String modifyby);

    void updateSymptomWorkingPartyMapping(@Param("symptomCode") String symptomCode,
                                      @Param("workingParty") String workingParty,
                                      @Param("serviceTypeCode") String serviceTypeCode,
                                      @Param("modifyby") String modifyby);

    SdSymptomWorkingPartyMappingEntity getSymptomWorkingPartyMapping(@Param("symptomCode") String symptomCode);

    void delSymptomWorkingPartyMapping(@Param("symptomCode") String symptomCode);

    void createSymptomCodePrefix(@Param("symptomGroupCode") String symptomGroupCode,
                                 @Param("symptomCodePrefix") String symptomCodePrefix,
                                 @Param("createby") String createby,
                                 @Param("modifyby") String modifyby);

    void delSymptomCodePrefixByGroupCode(@Param("symptomGroupCode") String symptomGroupCode);
}
