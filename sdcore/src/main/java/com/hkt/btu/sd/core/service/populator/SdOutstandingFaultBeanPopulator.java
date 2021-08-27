package com.hkt.btu.sd.core.service.populator;

import com.hkt.btu.common.core.service.populator.AbstractBeanPopulator;
import com.hkt.btu.sd.core.dao.entity.SdOutstandingFaultEntity;
import com.hkt.btu.sd.core.service.bean.SdOutstandingFaultBean;

public class SdOutstandingFaultBeanPopulator extends AbstractBeanPopulator<SdOutstandingFaultBean> {
    public void populate(SdOutstandingFaultEntity source, SdOutstandingFaultBean target) {
        target.setCount(source.getCount());
        target.setServiceTypeCode(source.getServiceTypeCode());
    }
}
