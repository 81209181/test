package com.hkt.btu.sd.core.service.populator;

import com.hkt.btu.common.core.dao.entity.BtuAutoRetryEntity;
import com.hkt.btu.common.core.service.bean.BtuAutoRetryBean;
import com.hkt.btu.common.core.service.constant.BtuAutoRetryStatusEnum;
import com.hkt.btu.common.core.service.populator.AbstractBeanPopulator;

public class SdAutoRetryBeanPopulator extends AbstractBeanPopulator<BtuAutoRetryBean> {

    public void populate(BtuAutoRetryEntity source, BtuAutoRetryBean target) {
        target.setRetryId(source.getRetryId());
        target.setClazzName(source.getClazzName());
        target.setMethodName(source.getMethodName());
        target.setMethodParam(source.getMethodParam());
        target.setTryCount(source.getTryCount());
        target.setMinWaitSecond(source.getMinWaitSecond());
        target.setNextTargetTime(source.getNextTargetTime());

        target.setCreateby(source.getCreateby());
        target.setCreatedate(source.getCreatedate());
        target.setModifyby(source.getModifyby());
        target.setModifydate(source.getModifydate());

        String statusCode = source.getStatus();
        target.setStatus(BtuAutoRetryStatusEnum.getEnum(statusCode).getDesc());
    }
}
