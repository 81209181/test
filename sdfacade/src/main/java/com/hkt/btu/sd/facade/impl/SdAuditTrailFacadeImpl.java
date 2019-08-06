package com.hkt.btu.sd.facade.impl;

import com.hkt.btu.sd.core.service.SdAuditTrailService;
import com.hkt.btu.sd.facade.SdAuditTrailFacade;
import com.hkt.btu.sd.facade.data.SdAccessRequestData;
import com.hkt.btu.sd.facade.data.SdAccessRequestVisitorData;
import com.hkt.btu.sd.facade.data.SdUserData;
import org.apache.commons.collections4.CollectionUtils;

import javax.annotation.Resource;
import java.util.List;


public class SdAuditTrailFacadeImpl implements SdAuditTrailFacade {

    @Resource(name = "auditTrailService")
    SdAuditTrailService sdAuditTrailService;


    @Override
    public void insertViewUserAuditTrail(SdUserData sdUserData) {
        sdAuditTrailService.insertViewUserAuditTrail(sdUserData.getUserId());
    }

    @Override
    public void insertViewRequesterAuditTrail(SdAccessRequestData sdAccessRequestData) {
        sdAuditTrailService.insertViewRequesterAuditTrail(sdAccessRequestData.getRequesterId());
    }

    @Override
    public void insertViewRequestVisitorAuditTrail(List<SdAccessRequestVisitorData> visitorDataList) {
        if(CollectionUtils.isNotEmpty(visitorDataList)) {
            for (SdAccessRequestVisitorData visitorData : visitorDataList) {
                sdAuditTrailService.insertViewRequestVisitorAuditTrail(visitorData.getVisitorAccessId());
            }
        }
    }
}
