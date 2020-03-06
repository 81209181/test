package com.hkt.btu.sd.core.service.populator;

import com.hkt.btu.common.core.service.populator.AbstractBeanPopulator;
import com.hkt.btu.sd.core.dao.entity.SdAuditTrailStatisticEntity;
import com.hkt.btu.sd.core.service.bean.SdAuditTrailStatisticBean;
import org.apache.commons.lang3.StringUtils;

public class SdStatisticBeanPopulator extends AbstractBeanPopulator {

    public void populate(SdAuditTrailStatisticBean bean, SdAuditTrailStatisticEntity entity) {
        if (entity.getTotal() >= 0) {
            bean.setTotal(entity.getTotal());
        }
        if (StringUtils.isNotEmpty(entity.getStatisticDate())) {
            bean.setStatisticDate(entity.getStatisticDate());
        }
    }
}
