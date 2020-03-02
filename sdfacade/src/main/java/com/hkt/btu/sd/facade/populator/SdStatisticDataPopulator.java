package com.hkt.btu.sd.facade.populator;

import com.hkt.btu.common.facade.populator.AbstractDataPopulator;
import com.hkt.btu.sd.core.service.bean.SdAuditTrailStatisticBean;
import com.hkt.btu.sd.facade.data.SdAuditTrailStatisticData;
import org.apache.commons.lang3.StringUtils;

public class SdStatisticDataPopulator extends AbstractDataPopulator {

    public void populate(SdAuditTrailStatisticBean bean, SdAuditTrailStatisticData data) {
        if(bean.getTotal() >= 0) {
            data.setTotal(bean.getTotal());
        }

        if (StringUtils.isNotEmpty(bean.getStatisticDate())) {
            data.setStatisticDate(bean.getStatisticDate());
        }
    }
}
