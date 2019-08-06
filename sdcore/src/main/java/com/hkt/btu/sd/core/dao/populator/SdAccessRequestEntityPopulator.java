package com.hkt.btu.sd.core.dao.populator;

import com.hkt.btu.common.core.dao.populator.EntityPopulator;
import com.hkt.btu.sd.core.dao.entity.SdAccessRequestEntity;
import com.hkt.btu.sd.core.service.SdSensitiveDataService;
import com.hkt.btu.sd.core.service.bean.SdAccessRequestBean;
import com.hkt.btu.sd.core.service.bean.SdCompanyBean;
import com.hkt.btu.sd.core.service.bean.SdUserBean;

import javax.annotation.Resource;

public class SdAccessRequestEntityPopulator implements EntityPopulator<SdAccessRequestEntity> {

    @Resource(name = "sensitiveDataService")
    SdSensitiveDataService sdSensitiveDataService;

    public void populateCreateEntity(SdUserBean requesterUserBean, SdAccessRequestEntity target) {
        target.setRequesterId(requesterUserBean.getUserId());
        target.setCreateby(requesterUserBean.getUserId());
        target.setModifyby(requesterUserBean.getUserId());

        target.setRequesterName(requesterUserBean.getName());
        target.setMobile( sdSensitiveDataService.encryptFromStringSafe(requesterUserBean.getMobile()) );
        target.setEmail( sdSensitiveDataService.encryptFromStringSafe(requesterUserBean.getEmail()) );
    }

    public void populateCreateEntity(SdCompanyBean requesterCompanyBean, SdAccessRequestEntity target) {
        target.setCompanyId(requesterCompanyBean.getCompanyId());
        target.setCompanyName(requesterCompanyBean.getName());
    }

    @SuppressWarnings("Duplicates")
    public void populateCreateEntity(SdAccessRequestBean sdAccessRequestBean, SdAccessRequestEntity target) {
        target.setVisitReason(sdAccessRequestBean.getVisitReason());
        target.setVisitLocation(sdAccessRequestBean.getVisitLocation());
        target.setVisitRackNum(sdAccessRequestBean.getVisitRackNum());
        target.setVisitDateFrom(sdAccessRequestBean.getVisitDateFrom());
        target.setVisitDateTo(sdAccessRequestBean.getVisitDateTo());
    }
}
