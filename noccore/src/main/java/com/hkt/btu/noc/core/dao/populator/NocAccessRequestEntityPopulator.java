package com.hkt.btu.noc.core.dao.populator;

import com.hkt.btu.common.core.dao.populator.EntityPopulator;
import com.hkt.btu.noc.core.dao.entity.NocAccessRequestEntity;
import com.hkt.btu.noc.core.service.NocSensitiveDataService;
import com.hkt.btu.noc.core.service.bean.NocAccessRequestBean;
import com.hkt.btu.noc.core.service.bean.NocCompanyBean;
import com.hkt.btu.noc.core.service.bean.NocUserBean;

import javax.annotation.Resource;

public class NocAccessRequestEntityPopulator implements EntityPopulator<NocAccessRequestEntity> {

    @Resource(name = "sensitiveDataService")
    NocSensitiveDataService nocSensitiveDataService;

    public void populateCreateEntity(NocUserBean requesterUserBean, NocAccessRequestEntity target) {
        target.setRequesterId(requesterUserBean.getUserId());
        target.setCreateby(requesterUserBean.getUserId());
        target.setModifyby(requesterUserBean.getUserId());

        target.setRequesterName(requesterUserBean.getName());
        target.setMobile( nocSensitiveDataService.encryptFromStringSafe(requesterUserBean.getMobile()) );
        target.setEmail( nocSensitiveDataService.encryptFromStringSafe(requesterUserBean.getEmail()) );
    }

    public void populateCreateEntity(NocCompanyBean requesterCompanyBean, NocAccessRequestEntity target) {
        target.setCompanyId(requesterCompanyBean.getCompanyId());
        target.setCompanyName(requesterCompanyBean.getName());
    }

    @SuppressWarnings("Duplicates")
    public void populateCreateEntity(NocAccessRequestBean nocAccessRequestBean, NocAccessRequestEntity target) {
        target.setVisitReason(nocAccessRequestBean.getVisitReason());
        target.setVisitLocation(nocAccessRequestBean.getVisitLocation());
        target.setVisitRackNum(nocAccessRequestBean.getVisitRackNum());
        target.setVisitDateFrom(nocAccessRequestBean.getVisitDateFrom());
        target.setVisitDateTo(nocAccessRequestBean.getVisitDateTo());
    }
}
