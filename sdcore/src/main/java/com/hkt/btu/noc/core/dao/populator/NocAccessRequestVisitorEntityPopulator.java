package com.hkt.btu.noc.core.dao.populator;

import com.hkt.btu.common.core.dao.populator.EntityPopulator;
import com.hkt.btu.noc.core.dao.entity.NocAccessRequestVisitorEntity;
import com.hkt.btu.noc.core.service.NocSensitiveDataService;
import com.hkt.btu.noc.core.service.bean.NocAccessRequestVisitorBean;
import com.hkt.btu.noc.core.service.bean.NocUserBean;

import javax.annotation.Resource;

public class NocAccessRequestVisitorEntityPopulator implements EntityPopulator<NocAccessRequestVisitorEntity> {

    @Resource(name = "sensitiveDataService")
    NocSensitiveDataService nocSensitiveDataService;

    public void populate(NocAccessRequestVisitorBean bean, NocAccessRequestVisitorEntity entity) {
        entity.setVisitorName(bean.getName());
        entity.setCompanyName(bean.getCompanyName());
        entity.setStaffId( nocSensitiveDataService.encryptFromStringSafe(bean.getStaffId()) );
        entity.setMobile( nocSensitiveDataService.encryptFromStringSafe(bean.getMobile()) );
    }

    public void populate(NocUserBean requesterBean, NocAccessRequestVisitorEntity entity) {
        entity.setCreateby(requesterBean.getUserId());
        entity.setModifyby(requesterBean.getUserId());
    }
}
