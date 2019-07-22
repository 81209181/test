package com.hkt.btu.noc.core.service.impl;

import com.hkt.btu.noc.core.dao.entity.NocAccessRequestEquipEntity;
import com.hkt.btu.noc.core.dao.mapper.NocAccessRequestEquipMapper;
import com.hkt.btu.noc.core.dao.populator.NocAccessRequestEquipEntityPopulator;
import com.hkt.btu.noc.core.exception.AuthorityNotFoundException;
import com.hkt.btu.noc.core.exception.InvalidInputException;
import com.hkt.btu.noc.core.service.NocAccessRequestEquipService;
import com.hkt.btu.noc.core.service.NocUserService;
import com.hkt.btu.noc.core.service.bean.NocAccessRequestEquipBean;
import com.hkt.btu.noc.core.service.bean.NocUserBean;
import com.hkt.btu.noc.core.service.populator.NocAccessRequestEquipBeanPopulator;
import org.apache.commons.lang3.StringUtils;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class NocAccessRequestEquipServiceImpl implements NocAccessRequestEquipService {

    @Resource
    NocAccessRequestEquipMapper nocAccessRequestEquipMapper;

    @Resource(name = "userService")
    NocUserService nocUserService;

    @Resource(name = "accessRequestEquipEntityPopulator")
    NocAccessRequestEquipEntityPopulator nocAccessRequestEquipEntityPopulator;
    @Resource(name = "accessRequestEquipBeanPopulator")
    NocAccessRequestEquipBeanPopulator nocAccessRequestEquipBeanPopulator;

    @Override
    public List<NocAccessRequestEquipBean> getEquipListByAccessRequestId(Integer accessRequestId) throws AuthorityNotFoundException {
        // determine company id restriction
        Integer companyId = nocUserService.getCompanyIdRestriction();
        // determine user id restriction
        Integer userId = nocUserService.getUserIdRestriction();

        // get
        List<NocAccessRequestEquipEntity> equipEntityList = nocAccessRequestEquipMapper.getAccessRequestEquipsByAccessRequestId(accessRequestId, companyId, userId);
        if(CollectionUtils.isEmpty(equipEntityList)){
            return new ArrayList<>();
        }

        // populate
        List<NocAccessRequestEquipBean> equipBeanList = new ArrayList<>();
        for(NocAccessRequestEquipEntity equipEntity : equipEntityList){
            NocAccessRequestEquipBean equipBean = new NocAccessRequestEquipBean();
            nocAccessRequestEquipBeanPopulator.populate(equipEntity, equipBean);
            equipBeanList.add(equipBean);
        }

        return equipBeanList;
    }

    @Override
    @Transactional
    public void createEquipForAccessRequest(List<NocAccessRequestEquipBean> visitorBeanList, Integer newAccessRequestId, NocUserBean requesterUserBean) {
        // prepare entity
        List<NocAccessRequestEquipEntity> equipEntityList = new LinkedList<>();
        removeEmptyEquipments(visitorBeanList);

        for(NocAccessRequestEquipBean equipBean : visitorBeanList){
            checkValidEquip(equipBean);

            NocAccessRequestEquipEntity equipEntity = new NocAccessRequestEquipEntity();
            nocAccessRequestEquipEntityPopulator.populate(equipBean, equipEntity);
            nocAccessRequestEquipEntityPopulator.populate(requesterUserBean, equipEntity);
            equipEntity.setAccessRequestId(newAccessRequestId);
            equipEntityList.add(equipEntity);
        }

        if(!CollectionUtils.isEmpty(equipEntityList)){
            nocAccessRequestEquipMapper.insertEquips(equipEntityList);
        }
    }


    private void removeEmptyEquipments(List<NocAccessRequestEquipBean> requestEquipBeanList){
        requestEquipBeanList.removeIf(
                equipBean -> equipBean == null || (
                        StringUtils.isEmpty(equipBean.getEqModel() )
                )
        );
    }

    private void checkValidEquip(NocAccessRequestEquipBean equipBean) throws InvalidInputException {
        if(StringUtils.isEmpty(equipBean.getEqModel())){
            throw new InvalidInputException("Missing equipment model.");
        }

        if(StringUtils.isEmpty(equipBean.getAction())){
            throw new InvalidInputException("Equipment Model " + equipBean.getEqModel() + ": Please choose In/Out.");
        }
    }
}
