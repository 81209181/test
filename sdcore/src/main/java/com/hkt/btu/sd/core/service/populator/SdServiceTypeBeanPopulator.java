package com.hkt.btu.sd.core.service.populator;

import com.hkt.btu.common.core.service.populator.AbstractBeanPopulator;
import com.hkt.btu.sd.core.dao.entity.SdServiceTypeEntity;
import com.hkt.btu.sd.core.service.bean.SdServiceTypeBean;

public class SdServiceTypeBeanPopulator extends AbstractBeanPopulator<SdServiceTypeBean> {

    public void populate(SdServiceTypeEntity source, SdServiceTypeBean target) {
        target.setServiceTypeCode(source.getServiceTypeCode());
        target.setServiceTypeName(source.getServiceTypeName());
    }
}
