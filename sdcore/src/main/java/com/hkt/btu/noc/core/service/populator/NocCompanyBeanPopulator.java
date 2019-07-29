package com.hkt.btu.noc.core.service.populator;


import com.hkt.btu.common.core.service.populator.AbstractBeanPopulator;
import com.hkt.btu.noc.core.dao.entity.NocAccessRequestEntity;
import com.hkt.btu.noc.core.dao.entity.NocCompanyEntity;
import com.hkt.btu.noc.core.service.bean.NocCompanyBean;

public class NocCompanyBeanPopulator extends AbstractBeanPopulator<NocCompanyBean> {
    public void populate(NocCompanyEntity source, NocCompanyBean target) {
        super.populate(source, target);

        target.setCompanyId(source.getCompanyId());
        target.setName(source.getName());
        target.setRemarks(source.getRemarks());
    }


    public void populate(NocAccessRequestEntity source, NocCompanyBean target) {
        target.setName(source.getCompanyName());
    }

}