package com.hkt.btu.common.core.service.populator;

import com.hkt.btu.common.core.dao.entity.BtuAutoRetryEntity;
import com.hkt.btu.common.core.service.bean.BtuAutoRetryBean;
import com.hkt.btu.common.core.service.constant.BtuAutoRetryStatusEnum;

public class BtuAutoRetryBeanPopulator extends AbstractBeanPopulator<BtuAutoRetryBean> {

    public void populate(BtuAutoRetryEntity source, BtuAutoRetryBean target) {
        target.setRetryId(source.getRetryId());
        target.setBeanName(source.getBeanName());
        target.setMethodName(source.getMethodName());
        target.setMethodParam(source.getMethodParam());
        target.setTryCount(source.getTryCount());
        target.setMinWaitSecond(source.getMinWaitSecond());
        target.setNextTargetTime(source.getNextTargetTime());

        target.setCreateby(source.getCreateby());
        target.setCreatedate(source.getCreatedate());
        target.setModifyby(source.getModifyby());
        target.setModifydate(source.getModifydate());

        BtuAutoRetryStatusEnum statusEnum = BtuAutoRetryStatusEnum.getEnum(source.getStatus());
        String statusCode = statusEnum==null ? null : statusEnum.getStatusCode();
        target.setStatus(statusCode);
    }
}
