package com.hkt.btu.sd.core.service.populator;


import com.hkt.btu.common.core.service.populator.AbstractBeanPopulator;
import com.hkt.btu.sd.core.dao.entity.SdAccessRequestEntity;
import com.hkt.btu.sd.core.dao.entity.SdCompanyEntity;
import com.hkt.btu.sd.core.service.bean.SdCompanyBean;

public class SdCompanyBeanPopulator extends AbstractBeanPopulator<SdCompanyBean> {
    public void populate(SdCompanyEntity source, SdCompanyBean target) {
        super.populate(source, target);

        target.setCompanyId(source.getCompanyId());
        target.setName(source.getName());
        target.setRemarks(source.getRemarks());
    }


    public void populate(SdAccessRequestEntity source, SdCompanyBean target) {
        target.setName(source.getCompanyName());
    }

}