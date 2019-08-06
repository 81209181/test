package com.hkt.btu.sd.core.service.impl;

import com.hkt.btu.sd.core.dao.entity.SdAccessRequestEquipEntity;
import com.hkt.btu.sd.core.dao.mapper.SdAccessRequestEquipMapper;
import com.hkt.btu.sd.core.dao.populator.SdAccessRequestEquipEntityPopulator;
import com.hkt.btu.sd.core.exception.AuthorityNotFoundException;
import com.hkt.btu.sd.core.exception.InvalidInputException;
import com.hkt.btu.sd.core.service.SdAccessRequestEquipService;
import com.hkt.btu.sd.core.service.SdUserService;
import com.hkt.btu.sd.core.service.bean.SdAccessRequestEquipBean;
import com.hkt.btu.sd.core.service.bean.SdUserBean;
import com.hkt.btu.sd.core.service.populator.SdAccessRequestEquipBeanPopulator;
import org.apache.commons.lang3.StringUtils;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class SdAccessRequestEquipServiceImpl implements SdAccessRequestEquipService {

    @Resource
    SdAccessRequestEquipMapper sdAccessRequestEquipMapper;

    @Resource(name = "userService")
    SdUserService sdUserService;

    @Resource(name = "accessRequestEquipEntityPopulator")
    SdAccessRequestEquipEntityPopulator sdAccessRequestEquipEntityPopulator;
    @Resource(name = "accessRequestEquipBeanPopulator")
    SdAccessRequestEquipBeanPopulator sdAccessRequestEquipBeanPopulator;

    @Override
    public List<SdAccessRequestEquipBean> getEquipListByAccessRequestId(Integer accessRequestId) throws AuthorityNotFoundException {
        // determine company id restriction
        Integer companyId = sdUserService.getCompanyIdRestriction();
        // determine user id restriction
        Integer userId = sdUserService.getUserIdRestriction();

        // get
        List<SdAccessRequestEquipEntity> equipEntityList = sdAccessRequestEquipMapper.getAccessRequestEquipsByAccessRequestId(accessRequestId, companyId, userId);
        if(CollectionUtils.isEmpty(equipEntityList)){
            return new ArrayList<>();
        }

        // populate
        List<SdAccessRequestEquipBean> equipBeanList = new ArrayList<>();
        for(SdAccessRequestEquipEntity equipEntity : equipEntityList){
            SdAccessRequestEquipBean equipBean = new SdAccessRequestEquipBean();
            sdAccessRequestEquipBeanPopulator.populate(equipEntity, equipBean);
            equipBeanList.add(equipBean);
        }

        return equipBeanList;
    }

    @Override
    @Transactional
    public void createEquipForAccessRequest(List<SdAccessRequestEquipBean> visitorBeanList, Integer newAccessRequestId, SdUserBean requesterUserBean) {
        // prepare entity
        List<SdAccessRequestEquipEntity> equipEntityList = new LinkedList<>();
        removeEmptyEquipments(visitorBeanList);

        for(SdAccessRequestEquipBean equipBean : visitorBeanList){
            checkValidEquip(equipBean);

            SdAccessRequestEquipEntity equipEntity = new SdAccessRequestEquipEntity();
            sdAccessRequestEquipEntityPopulator.populate(equipBean, equipEntity);
            sdAccessRequestEquipEntityPopulator.populate(requesterUserBean, equipEntity);
            equipEntity.setAccessRequestId(newAccessRequestId);
            equipEntityList.add(equipEntity);
        }

        if(!CollectionUtils.isEmpty(equipEntityList)){
            sdAccessRequestEquipMapper.insertEquips(equipEntityList);
        }
    }


    private void removeEmptyEquipments(List<SdAccessRequestEquipBean> requestEquipBeanList){
        requestEquipBeanList.removeIf(
                equipBean -> equipBean == null || (
                        StringUtils.isEmpty(equipBean.getEqModel() )
                )
        );
    }

    private void checkValidEquip(SdAccessRequestEquipBean equipBean) throws InvalidInputException {
        if(StringUtils.isEmpty(equipBean.getEqModel())){
            throw new InvalidInputException("Missing equipment model.");
        }

        if(StringUtils.isEmpty(equipBean.getAction())){
            throw new InvalidInputException("Equipment Model " + equipBean.getEqModel() + ": Please choose In/Out.");
        }
    }
}
