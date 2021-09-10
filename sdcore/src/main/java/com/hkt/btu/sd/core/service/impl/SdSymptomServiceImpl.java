package com.hkt.btu.sd.core.service.impl;

import com.hkt.btu.sd.core.dao.entity.SdServiceTypeEntity;
import com.hkt.btu.sd.core.dao.entity.SdSortEntity;
import com.hkt.btu.sd.core.dao.entity.SdSymptomCodePrefixEntity;
import com.hkt.btu.sd.core.dao.entity.SdSymptomEntity;
import com.hkt.btu.sd.core.dao.entity.SdSymptomGroupEntity;
import com.hkt.btu.sd.core.dao.entity.SdSymptomGroupRoleMappingEntity;
import com.hkt.btu.sd.core.dao.entity.SdSymptomMappingEntity;
import com.hkt.btu.sd.core.dao.entity.SdSymptomWorkingPartyMappingEntity;
import com.hkt.btu.sd.core.dao.mapper.SdSymptomMapper;
import com.hkt.btu.sd.core.service.SdSymptomService;
import com.hkt.btu.sd.core.service.SdUserService;
import com.hkt.btu.sd.core.service.bean.SdSortBean;
import com.hkt.btu.sd.core.service.bean.SdSymptomBean;
import com.hkt.btu.sd.core.service.bean.SdSymptomGroupBean;
import com.hkt.btu.sd.core.service.bean.SdSymptomMappingBean;
import com.hkt.btu.sd.core.service.bean.SdSymptomWorkingPartyMappingBean;
import com.hkt.btu.sd.core.service.populator.SdSymptomBeanPopulator;
import com.hkt.btu.sd.core.service.populator.SdSymptomGroupBeanPopulator;
import com.hkt.btu.sd.core.service.populator.SdSymptomMappingBeanPopulator;
import com.hkt.btu.sd.core.service.populator.SdSymptomWorkingPartyMappingBeanPopulator;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.xmlbeans.impl.piccolo.util.DuplicateKeyException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class SdSymptomServiceImpl implements SdSymptomService {
    private static final Logger LOG = LogManager.getLogger(SdSymptomServiceImpl.class);

    @Resource
    private SdSymptomMapper sdSymptomMapper;

    @Resource(name = "userService")
    SdUserService userService;

    @Resource(name = "symptmBeanPopulator")
    SdSymptomBeanPopulator symptomBeanPopulator;
    @Resource(name = "symptmMappingBeanPopulator")
    SdSymptomMappingBeanPopulator symptomMappingBeanPopulator;

    @Resource(name = "symptomGroupBeanPopulator")
    SdSymptomGroupBeanPopulator symptomGroupBeanPopulator;

    @Resource(name = "symptomWorkingPartyMappingBeanPopulator")
    SdSymptomWorkingPartyMappingBeanPopulator symptomWorkingPartyMappingBeanPopulator;

    @Override
    public List<SdSymptomGroupBean> getSymptomGroupList() {
        return sdSymptomMapper.getSymptomGroupList().stream().map(entity -> {
            SdSymptomGroupBean bean = new SdSymptomGroupBean();
            symptomGroupBeanPopulator.pupulate(entity, bean);
            return bean;
        }).collect(Collectors.toList());
    }

    private String getLastestSymptomCode(String symptomGroupCode){
        String symtomCodePrefix = sdSymptomMapper.getSymptomCodePrefixByGroup(symptomGroupCode).getSymtomCodePrefix();
        List<SdSymptomEntity> symptomEntityList = sdSymptomMapper.getSymptomByGroupCode(symptomGroupCode);
        long size = symptomEntityList.stream().filter(symptomEntity -> symptomEntity.getSymptomCode().startsWith(symtomCodePrefix)).count();
        String symptomCodeNum = String.format("%03d",size+1);
        return symtomCodePrefix + symptomCodeNum;
    }

    @Override
    @Transactional(rollbackFor = DuplicateKeyException.class, propagation = Propagation.REQUIRED)
    public String createSymptom(String symptomGroupCode, String symptomDescription, List<String> serviceTypeList,
                                String voiceLineTest, String apptMode){
        String createby = userService.getCurrentUserUserId();
        String symptomCode = getLastestSymptomCode(symptomGroupCode);
        sdSymptomMapper.createSymptom(symptomCode, symptomGroupCode, symptomDescription, createby, voiceLineTest, apptMode);
        LOG.info(String.format("Created symptom %s - %s", symptomCode, symptomDescription));

        List<String> filteredServiceTypeList = new ArrayList<>();
        if(CollectionUtils.isNotEmpty(serviceTypeList)){
            for (String serviceType : serviceTypeList) {
                if (!StringUtils.equals(SdServiceTypeEntity.SERVICE_TYPE.UNKNOWN, serviceType)) {
                    filteredServiceTypeList.add(serviceType);
                }
            }
        }

        if (!filteredServiceTypeList.isEmpty()){
            sdSymptomMapper.createSymptomMapping(filteredServiceTypeList, symptomCode, createby);
            LOG.info("Created symptomCode:" + symptomCode + ", serviceTypeList:" + filteredServiceTypeList);
        }

        return String.format("Created symptom %s - %s", symptomCode, symptomDescription);
    }

    @Override
    public Page<SdSymptomBean> searchSymptomList(Pageable pageable, String symptomGroupCode, String symptomDescription, List<SdSortBean> sortList) {
        long offset = pageable.getOffset();
        int pageSize = pageable.getPageSize();

        List<SdSortEntity> sortEntityList = sortList.stream().map(bean -> {
            SdSortEntity entity = new SdSortEntity();
            entity.setDir(bean.getDir());
            entity.setColumn(bean.getColumn());
            return entity;
        }).collect(Collectors.toList());

        List<SdSymptomEntity> entityList = sdSymptomMapper.searchSymptomList(offset, pageSize, symptomGroupCode, symptomDescription, sortEntityList);
        Integer totalCount = sdSymptomMapper.searchSymptomCount(symptomGroupCode, symptomDescription);

        return new PageImpl<>(buildSymptomBeanList(entityList), pageable, totalCount);
    }

    @Override
    public SdSymptomBean getSymptomBySymptomCode(String symptomCode) {
        SdSymptomEntity entity = sdSymptomMapper.getSymptomBySymptomCode(symptomCode);
        if (entity == null) {
            return null;
        }
        SdSymptomBean bean = new SdSymptomBean();
        symptomBeanPopulator.populate(entity, bean);
        return bean;
    }

    @Override
    public List<SdSymptomMappingBean> getSymptomMapping(String symptomCode) {
        List<SdSymptomMappingBean> results = new LinkedList<>();

        List<SdSymptomMappingEntity> symptomMappingEntity = sdSymptomMapper.getSymptomMapping(symptomCode);

        if (CollectionUtils.isEmpty(symptomMappingEntity)) {
            return null;
        }
        for (SdSymptomMappingEntity entity : symptomMappingEntity) {
            SdSymptomMappingBean bean = new SdSymptomMappingBean();
            symptomMappingBeanPopulator.populate(entity, bean);
            results.add(bean);
        }
        return results;
    }

    @Override
    public void updateSymptom(String oldSymptomCode, String symptomCode, String symptomGroupCode,
                              String symptomDescription, String voiceLineTest, String apptMode) {
        String createby = userService.getCurrentUserUserId();
        sdSymptomMapper.updateSymptom(oldSymptomCode, symptomCode, symptomGroupCode, symptomDescription, createby, voiceLineTest, apptMode);
        LOG.info(String.format("Updated symptom %s - %s", symptomCode, symptomDescription));
    }

    @Override
    public void editSymptomMapping(String oldSymptomCode, String symptomCode, List<String> serviceTypeList) {
        String createby = userService.getCurrentUserUserId();

        // filter unknown service type
        List<String> filteredServiceTypeList = new ArrayList<>();
        if(CollectionUtils.isNotEmpty(serviceTypeList)){
            for (String serviceType : serviceTypeList) {
                if (!StringUtils.equals(SdServiceTypeEntity.SERVICE_TYPE.UNKNOWN, serviceType)) {
                    filteredServiceTypeList.add(serviceType);
                }
            }
        }

        // get all existing service type list of the symptom
        List<SdSymptomMappingEntity> existSymptomList = sdSymptomMapper.getSymptomMapping(oldSymptomCode);
        if (CollectionUtils.isEmpty(existSymptomList)) {
            if (!CollectionUtils.isEmpty(filteredServiceTypeList)) {
                LOG.info("Created symptomCode:" + symptomCode + ", serviceTypeList:" + filteredServiceTypeList);
                sdSymptomMapper.createSymptomMapping(filteredServiceTypeList, symptomCode, createby);
            }
        } else {
            List<String> existServiceTypeList = new ArrayList<>();
            for (SdSymptomMappingEntity symptomMappingEntity : existSymptomList) {
                existServiceTypeList.add(symptomMappingEntity.getServiceTypeCode());
            }

            // find which service type to delete
            List<String> toDeleteServiceTypeList = new ArrayList<>(existServiceTypeList);
            toDeleteServiceTypeList.removeAll(filteredServiceTypeList);
            LOG.info("Deleted symptomCode:" + symptomCode + ", toDeleteServiceTypeList:" + toDeleteServiceTypeList);

            // find which service type to insert
            List<String> toInsertServiceTypeList = new ArrayList<>(filteredServiceTypeList);
            toInsertServiceTypeList.removeAll(existServiceTypeList);
            LOG.info("Created symptomCode:" + symptomCode + ", toInsertServiceTypeList:" + toInsertServiceTypeList);

            // delete service type
            if (!CollectionUtils.isEmpty(toDeleteServiceTypeList)) {
                sdSymptomMapper.deleteSymptomMapping(oldSymptomCode, toDeleteServiceTypeList);
            }

            // insert service type
            if (!CollectionUtils.isEmpty(toInsertServiceTypeList)) {
                sdSymptomMapper.createSymptomMapping(toInsertServiceTypeList, symptomCode, createby);
            }
        }
    }

    @Override
    public List<SdSymptomBean> getAllSymptomList() {
        List<SdSymptomEntity> entityList = sdSymptomMapper.getAllSymptomList();
        return buildSymptomBeanList(entityList);
    }

    private List<SdSymptomBean> buildSymptomBeanList(List<SdSymptomEntity> entityList) {
        if (CollectionUtils.isEmpty(entityList)) {
            return new LinkedList<>();
        }

        List<SdSymptomBean> beanList = new LinkedList<>();
        for (SdSymptomEntity entity : entityList) {
            SdSymptomBean bean = new SdSymptomBean();
            symptomBeanPopulator.populate(entity, bean);
            beanList.add(bean);
        }
        return beanList;
    }

    @Override
    public boolean ifSymptomDescExist(String symptomDescription, String symptomGroupCode) {
        List<SdSymptomEntity> symptomEntityList = sdSymptomMapper.getSymptomByGroupCode(symptomGroupCode);
        long count = symptomEntityList.stream()
                .filter(symptomEntity -> StringUtils.equals(symptomEntity.getSymptomDescription().trim(), symptomDescription.trim()))
                .count();
        return count > 0;
    }

    @Override
    @Transactional
    public void createSymptomGroup(String symptomGroupCode, String symptomGroupName, String symptomCodePrefix, List<String> roleList) {
        String userId = userService.getCurrentUserUserId();
        sdSymptomMapper.createSymptomGroup(symptomGroupCode, symptomGroupName, userId, userId);
        sdSymptomMapper.createSymptomCodePrefix(symptomGroupCode, symptomCodePrefix, userId, userId);
        if (CollectionUtils.isNotEmpty(roleList)) {
            sdSymptomMapper.createSymptomGroupRoleMapping(symptomGroupCode, roleList, userId, userId);
        }
        LOG.info(String.format("Created a SYMPTOM_GROUP record: %S -- %S", symptomGroupCode, symptomGroupName));
    }

    @Override
    public Optional<SdSymptomGroupBean> getSymptomGroup(String symptomGroupCode){
        SdSymptomGroupBean bean = new SdSymptomGroupBean();
        return Optional.ofNullable(sdSymptomMapper.getSymptomGroup(symptomGroupCode)).map(entity -> {
            symptomGroupBeanPopulator.pupulate(entity,bean);
            return bean;
        });
    }

    @Override
    @Transactional
    public void updateSymptomGroup(String symptomGroupCode, String symptomGroupName, List<String> roleList) {
        String userId = userService.getCurrentUserId();
        sdSymptomMapper.updateSymptomGroup(symptomGroupCode, symptomGroupName, userId);

        List<String> dbRoleList = sdSymptomMapper.getSymptomGroupRoleMappingByCode(symptomGroupCode)
                .stream().map(SdSymptomGroupRoleMappingEntity::getRoleId).collect(Collectors.toList());

        if (CollectionUtils.isNotEmpty(roleList)) {
            List<String> insertRoleList = roleList.stream().filter(role -> !dbRoleList.contains(role)).collect(Collectors.toList());
            if (CollectionUtils.isNotEmpty(insertRoleList)) {
                sdSymptomMapper.createSymptomGroupRoleMapping(symptomGroupCode, insertRoleList, userId, userId);
            }
            LOG.info(String.format("Created %s SYMPTOM_GROUP_MAPPING records %S -- %S", insertRoleList.size(), symptomGroupCode, insertRoleList));
        }

        if (CollectionUtils.isNotEmpty(dbRoleList)) {
            List<String> delRoleList = dbRoleList.stream().filter(role -> !roleList.contains(role)).collect(Collectors.toList());
            if (CollectionUtils.isNotEmpty(delRoleList)) {
                sdSymptomMapper.delSymptomGroupRoleMappingBatch(symptomGroupCode, delRoleList);
            }
            LOG.info(String.format("Deleted %s records from SYMPTOM_GROUP_MAPPING where SYMPTOM_GROUP_CODE = %s and ROLE_ID in (%S)",
                    delRoleList.size(), symptomGroupCode, delRoleList));
        }
    }

    @Override
    @Transactional
    public void delSymptomGroup(String symptomGroupCode) {
        sdSymptomMapper.delSymptomGroup(symptomGroupCode);
        sdSymptomMapper.delSymptomGroupRoleMappingBatch(symptomGroupCode,null);
        sdSymptomMapper.delSymptomCodePrefixByGroupCode(symptomGroupCode);
        LOG.info("Deleted a record from SYMPTOM_GROUP where SYMPTOM_GROUP_CODE = "+symptomGroupCode);
    }

    @Override
    public List<SdSymptomWorkingPartyMappingBean> getSymptomWorkingPartyMappingList(){
        List<SdSymptomWorkingPartyMappingEntity> entityList = sdSymptomMapper.getSymptomWorkingPartyMappingList();

        List<SdSymptomWorkingPartyMappingBean> beanList = new ArrayList<>();
        entityList.forEach(entity -> {
            SdSymptomWorkingPartyMappingBean bean = new SdSymptomWorkingPartyMappingBean();
            symptomWorkingPartyMappingBeanPopulator.populate(entity, bean);
            beanList.add(bean);
        });

        return beanList;
    }

    @Override
    public void createSymptomWorkingPartyMapping(String symptomCode, String workingParty, String serviceTypeCode) {
        String userId = userService.getCurrentUserUserId();
        sdSymptomMapper.createSymptomWorkingPartyMapping(symptomCode, workingParty, serviceTypeCode, userId, userId);
        LOG.info(String.format("Created a SYMPTOM_WORKINGPARTY_MAPPING record: %S -- %S -- %S", symptomCode, workingParty, serviceTypeCode));
    }

    @Override
    public void updateSymptomWorkingPartyMapping(String symptomCode, String workingParty, String serviceTypeCode){
        String userId = userService.getCurrentUserUserId();
        sdSymptomMapper.updateSymptomWorkingPartyMapping(symptomCode, workingParty, serviceTypeCode, userId);
        LOG.info(String.format("Updated SYMPTOM_WORKINGPARTY_MAPPING %S -- %S -- %S", symptomCode, workingParty, serviceTypeCode));
    }

    @Override
    public Optional<SdSymptomWorkingPartyMappingBean> getSymptomWorkingPartyMapping(String symptomCode) {
        SdSymptomWorkingPartyMappingBean bean = new SdSymptomWorkingPartyMappingBean();
        return Optional.ofNullable(sdSymptomMapper.getSymptomWorkingPartyMapping(symptomCode)).map(entity -> {
            symptomWorkingPartyMappingBeanPopulator.populate(entity, bean);
            return bean;
        });
    }

    @Override
    public void delSymptomWorkingPartyMapping(String symptomCode) {
        sdSymptomMapper.delSymptomWorkingPartyMapping(symptomCode);
        LOG.info("Deleted a record from SYMPTOM_WORKINGPARTY_MAPPING where SYMPTOM_CODE = "+symptomCode);
    }
}
