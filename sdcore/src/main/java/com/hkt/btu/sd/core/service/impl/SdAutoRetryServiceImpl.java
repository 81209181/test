package com.hkt.btu.sd.core.service.impl;

import com.hkt.btu.common.core.dao.entity.BtuAutoRetryEntity;
import com.hkt.btu.common.core.service.bean.BtuAutoRetryBean;
import com.hkt.btu.common.core.service.constant.BtuAutoRetryStatusEnum;
import com.hkt.btu.common.core.service.impl.BtuAutoRetryServiceImpl;
import com.hkt.btu.sd.core.dao.mapper.SdAutoRetryMapper;
import com.hkt.btu.sd.core.service.populator.SdAutoRetryBeanPopulator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;

public class SdAutoRetryServiceImpl extends BtuAutoRetryServiceImpl {
    private static final Logger LOG = LogManager.getLogger(SdAutoRetryServiceImpl.class);

    @Resource
    SdAutoRetryMapper sdAutoRetryMapper;

    @Resource(name = "autoRetryBeanPopulator")
    SdAutoRetryBeanPopulator autoRetryBeanPopulator;

    public Integer createAutoRetry(String clazz, String methodName, String methodParam, int minWaitSecond, String createby){
        return sdAutoRetryMapper.createAutoRetry(clazz, methodName, methodParam, minWaitSecond, createby);
    }

    public Integer updateAutoRetry(Integer retryId, String clazz, String methodName, String methodParam,
                                   BtuAutoRetryStatusEnum statusEnum, Integer tryCount, Integer minWaitSecond,
                                   LocalDateTime nextTargetTime, String modifyby){
        return sdAutoRetryMapper.updateAutoRetry(retryId, clazz, methodName, methodParam, statusEnum.getStatusCode(),
                tryCount, minWaitSecond, nextTargetTime, modifyby);
    }

    @Override
    public Page<BtuAutoRetryBean> searchRetryQueue(Pageable pageable, Integer retryId, String clazz, String methodName,
                                                   String methodParam, BtuAutoRetryStatusEnum status,
                                                   Integer tryCountFrom, Integer tryCountTo, Integer minWaitSecondFrom,
                                                   Integer minWaitSecondTo, LocalDateTime nextTargetTimeFrom,
                                                   LocalDateTime nextTargetTimeTo) {
        long offset = pageable.getOffset();
        int pageSize = pageable.getPageSize();

        List<BtuAutoRetryEntity> entityList = sdAutoRetryMapper.searchRetryQueue(offset, pageSize, retryId, clazz, methodName, methodParam,
                status.getStatusCode(), tryCountFrom, tryCountTo, minWaitSecondFrom, minWaitSecondTo, nextTargetTimeFrom, nextTargetTimeTo);
        Integer totalCount = sdAutoRetryMapper.searchRetryCount(retryId, clazz, methodName, methodParam,
                status.getStatusCode(), tryCountFrom, tryCountTo, minWaitSecondFrom, minWaitSecondTo, nextTargetTimeFrom, nextTargetTimeTo);

        List<BtuAutoRetryBean> beanList = new LinkedList<>();
        for (BtuAutoRetryEntity entity : entityList) {
            BtuAutoRetryBean bean = new BtuAutoRetryBean();
            autoRetryBeanPopulator.populate(entity, bean);
            beanList.add(bean);
        }

        return new PageImpl<>(beanList, pageable, totalCount);
    }
}
