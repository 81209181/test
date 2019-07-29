package com.hkt.btu.noc.core.service.impl;

import com.hkt.btu.noc.core.dao.entity.NocOperationHistEntity;
import com.hkt.btu.noc.core.dao.mapper.NocOperationHistMapper;
import com.hkt.btu.noc.core.exception.AuthorityNotFoundException;
import com.hkt.btu.noc.core.service.NocAccessRequestService;
import com.hkt.btu.noc.core.service.NocOperationHistService;
import com.hkt.btu.noc.core.service.bean.NocAccessRequestBean;
import com.hkt.btu.noc.core.service.bean.NocOperationHistBean;
import com.hkt.btu.noc.core.service.populator.NocOperationHistBeanPopulator;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

public class NocOperationHistServiceImpl implements NocOperationHistService {
    private static final Logger LOG = LogManager.getLogger(NocOperationHistServiceImpl.class);

    @Resource(name = "accessRequestService")
    NocAccessRequestService nocAccessRequestService;

    @Resource
    NocOperationHistMapper nocOperationHistMapper;

    @Resource(name = "operationHistBeanPopulator")
    NocOperationHistBeanPopulator nocOperationHistBeanPopulator;


    @Override
    public List<NocOperationHistBean> getAccessRequestOptHistList(Integer accessRequestId)
            throws AuthorityNotFoundException {
        // check can view request
        NocAccessRequestBean nocAccessRequestBean = nocAccessRequestService.getAccessRequestBasicInfoByRequestId(accessRequestId);
        if(nocAccessRequestBean==null){
            return null;
        }

        List<NocOperationHistEntity> entityList = nocOperationHistMapper.getOptHistListByItemTypeAndId(
                NocOperationHistEntity.ACCESS_REQUEST.ITEM_TYPE, accessRequestId.toString());
        if(CollectionUtils.isEmpty(entityList)){
            return new ArrayList<>();
        }

        List<NocOperationHistBean> beanList = new ArrayList<>();
        for(NocOperationHistEntity entity : entityList){
            NocOperationHistBean bean = new NocOperationHistBean();
            nocOperationHistBeanPopulator.populate(entity, bean);
            nocOperationHistBeanPopulator.populate(entity, bean);
            beanList.add(bean);
        }
        return beanList;
    }

    @Override
    public void createAccessRequestOptHistStatusChange(Integer accessRequestId, String fromStatus, String toStatus, Integer userId) {
        try {
            String detail = String.format(NocOperationHistEntity.ACCESS_REQUEST.DETAIL_PATTERN_STATUS_CHANGE, fromStatus, toStatus);

            NocOperationHistEntity entity = new NocOperationHistEntity();
            entity.setItemType(NocOperationHistEntity.ACCESS_REQUEST.ITEM_TYPE);
            entity.setItemId(accessRequestId.toString());
            entity.setDetail(detail);
            entity.setUserId(userId);

            // insert
            nocOperationHistMapper.insertOperationHist(entity);
        } catch (RuntimeException e){
            LOG.error(e.getMessage(), e);
        }
    }
}
