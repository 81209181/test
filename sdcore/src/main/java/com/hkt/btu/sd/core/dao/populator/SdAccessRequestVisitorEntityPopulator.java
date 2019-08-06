package com.hkt.btu.sd.core.dao.populator;

import com.hkt.btu.common.core.dao.populator.EntityPopulator;
import com.hkt.btu.sd.core.dao.entity.SdAccessRequestVisitorEntity;
import com.hkt.btu.sd.core.service.SdSensitiveDataService;
import com.hkt.btu.sd.core.service.bean.SdAccessRequestVisitorBean;
import com.hkt.btu.sd.core.service.bean.SdUserBean;

import javax.annotation.Resource;

public class SdAccessRequestVisitorEntityPopulator implements EntityPopulator<SdAccessRequestVisitorEntity> {

    @Resource(name = "sensitiveDataService")
    SdSensitiveDataService sdSensitiveDataService;

    public void populate(SdAccessRequestVisitorBean bean, SdAccessRequestVisitorEntity entity) {
        entity.setVisitorName(bean.getName());
        entity.setCompanyName(bean.getCompanyName());
        entity.setStaffId( sdSensitiveDataService.encryptFromStringSafe(bean.getStaffId()) );
        entity.setMobile( sdSensitiveDataService.encryptFromStringSafe(bean.getMobile()) );
    }

    public void populate(SdUserBean requesterBean, SdAccessRequestVisitorEntity entity) {
        entity.setCreateby(requesterBean.getUserId());
        entity.setModifyby(requesterBean.getUserId());
    }
}
