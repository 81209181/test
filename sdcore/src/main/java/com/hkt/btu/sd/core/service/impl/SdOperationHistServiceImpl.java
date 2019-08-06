package com.hkt.btu.sd.core.service.impl;

import com.hkt.btu.sd.core.dao.entity.SdOperationHistEntity;
import com.hkt.btu.sd.core.dao.mapper.SdOperationHistMapper;
import com.hkt.btu.sd.core.exception.AuthorityNotFoundException;
import com.hkt.btu.sd.core.service.SdAccessRequestService;
import com.hkt.btu.sd.core.service.SdOperationHistService;
import com.hkt.btu.sd.core.service.bean.SdAccessRequestBean;
import com.hkt.btu.sd.core.service.bean.SdOperationHistBean;
import com.hkt.btu.sd.core.service.populator.SdOperationHistBeanPopulator;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

public class SdOperationHistServiceImpl implements SdOperationHistService {
    private static final Logger LOG = LogManager.getLogger(SdOperationHistServiceImpl.class);

    @Resource(name = "accessRequestService")
    SdAccessRequestService sdAccessRequestService;

    @Resource
    SdOperationHistMapper sdOperationHistMapper;

    @Resource(name = "operationHistBeanPopulator")
    SdOperationHistBeanPopulator sdOperationHistBeanPopulator;


    @Override
    public List<SdOperationHistBean> getAccessRequestOptHistList(Integer accessRequestId)
            throws AuthorityNotFoundException {
        // check can view request
        SdAccessRequestBean sdAccessRequestBean = sdAccessRequestService.getAccessRequestBasicInfoByRequestId(accessRequestId);
        if(sdAccessRequestBean==null){
            return null;
        }

        List<SdOperationHistEntity> entityList = sdOperationHistMapper.getOptHistListByItemTypeAndId(
                SdOperationHistEntity.ACCESS_REQUEST.ITEM_TYPE, accessRequestId.toString());
        if(CollectionUtils.isEmpty(entityList)){
            return new ArrayList<>();
        }

        List<SdOperationHistBean> beanList = new ArrayList<>();
        for(SdOperationHistEntity entity : entityList){
            SdOperationHistBean bean = new SdOperationHistBean();
            sdOperationHistBeanPopulator.populate(entity, bean);
            sdOperationHistBeanPopulator.populate(entity, bean);
            beanList.add(bean);
        }
        return beanList;
    }

    @Override
    public void createAccessRequestOptHistStatusChange(Integer accessRequestId, String fromStatus, String toStatus, Integer userId) {
        try {
            String detail = String.format(SdOperationHistEntity.ACCESS_REQUEST.DETAIL_PATTERN_STATUS_CHANGE, fromStatus, toStatus);

            SdOperationHistEntity entity = new SdOperationHistEntity();
            entity.setItemType(SdOperationHistEntity.ACCESS_REQUEST.ITEM_TYPE);
            entity.setItemId(accessRequestId.toString());
            entity.setDetail(detail);
            entity.setUserId(userId);

            // insert
            sdOperationHistMapper.insertOperationHist(entity);
        } catch (RuntimeException e){
            LOG.error(e.getMessage(), e);
        }
    }
}
