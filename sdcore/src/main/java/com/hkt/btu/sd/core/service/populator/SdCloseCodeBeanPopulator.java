package com.hkt.btu.sd.core.service.populator;


import com.hkt.btu.common.core.service.populator.AbstractBeanPopulator;
import com.hkt.btu.sd.core.dao.entity.SdCloseCodeEntity;
import com.hkt.btu.sd.core.service.bean.SdCloseCodeBean;

public class SdCloseCodeBeanPopulator extends AbstractBeanPopulator<SdCloseCodeBean> {

    public void populate(SdCloseCodeEntity source, SdCloseCodeBean target) {
        super.populate(source, target);
        target.setCloseCode(source.getCloseCode());
        target.setCloseCodeDescription(source.getCloseCodeDescription());
    }
}
