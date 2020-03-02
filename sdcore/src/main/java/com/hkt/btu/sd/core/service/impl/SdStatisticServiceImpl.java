package com.hkt.btu.sd.core.service.impl;

import com.hkt.btu.sd.core.dao.entity.SdAuditTrailStatisticEntity;
import com.hkt.btu.sd.core.dao.mapper.SdAuditTrailMapper;
import com.hkt.btu.sd.core.service.SdStatisticService;
import com.hkt.btu.sd.core.service.bean.SdAuditTrailStatisticBean;
import com.hkt.btu.sd.core.service.populator.SdStatisticBeanPopulator;
import org.apache.commons.collections4.CollectionUtils;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

public class SdStatisticServiceImpl implements SdStatisticService {

    @Resource
    SdAuditTrailMapper auditTrailMapper;

    @Resource(name = "statisticBeanPopulator")
    SdStatisticBeanPopulator statisticBeanPopulator;

    @Override
    public List<SdAuditTrailStatisticBean> getLoginCountLast90Days() {
        List<SdAuditTrailStatisticEntity> statisticEntityList = auditTrailMapper.getLoginCountLast90Days();
        List<SdAuditTrailStatisticBean> beanList = populate(statisticEntityList);
        return beanList;
    }

    @Override
    public List<SdAuditTrailStatisticBean> getLoginCountLastTwoWeeks() {
        List<SdAuditTrailStatisticEntity> statisticEntityList = auditTrailMapper.getLoginCountLastTwoWeeks();
        List<SdAuditTrailStatisticBean> beanList = populate(statisticEntityList);
        return beanList;
    }

    private List<SdAuditTrailStatisticBean> populate(List<SdAuditTrailStatisticEntity> statisticEntityList) {
        if (CollectionUtils.isEmpty(statisticEntityList)) {
            return null;
        }

        List<SdAuditTrailStatisticBean> statisticBeans = statisticEntityList.stream().map(entity -> {
            SdAuditTrailStatisticBean bean = new SdAuditTrailStatisticBean();
            statisticBeanPopulator.populate(bean, entity);
            return bean;
        }).collect(Collectors.toList());

        return statisticBeans;
    }
}
