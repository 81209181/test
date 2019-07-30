package com.hkt.btu.noc.core.service.populator;


import com.hkt.btu.common.core.service.populator.AbstractBeanPopulator;
import com.hkt.btu.noc.core.dao.entity.NocCompanyEntity;
import com.hkt.btu.noc.core.service.bean.NocCompanyBean;
import org.springframework.stereotype.Component;

@Component
public class NocCompanyBeanPopulator extends AbstractBeanPopulator<NocCompanyBean> {
    public void populate(NocCompanyEntity source, NocCompanyBean target) {
        super.populate(source, target);

        target.setCompanyId(source.getCompanyId());
        target.setName(source.getName());
        target.setRemarks(source.getRemarks());
    }
}