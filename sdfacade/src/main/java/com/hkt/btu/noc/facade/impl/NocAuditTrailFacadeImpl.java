package com.hkt.btu.noc.facade.impl;

import com.hkt.btu.noc.core.service.NocAuditTrailService;
import com.hkt.btu.noc.facade.NocAuditTrailFacade;
import com.hkt.btu.noc.facade.data.NocAccessRequestData;
import com.hkt.btu.noc.facade.data.NocAccessRequestVisitorData;
import com.hkt.btu.noc.facade.data.NocUserData;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class NocAuditTrailFacadeImpl implements NocAuditTrailFacade {

    @Resource(name = "NocAuditTrailService")
    NocAuditTrailService nocAuditTrailService;


    @Override
    public void insertViewUserAuditTrail(NocUserData nocUserData) {
        nocAuditTrailService.insertViewUserAuditTrail(nocUserData.getUserId());
    }

    @Override
    public void insertViewRequesterAuditTrail(NocAccessRequestData nocAccessRequestData) {
        nocAuditTrailService.insertViewRequesterAuditTrail(nocAccessRequestData.getRequesterId());
    }

    @Override
    public void insertViewRequestVisitorAuditTrail(List<NocAccessRequestVisitorData> visitorDataList) {
        if(CollectionUtils.isNotEmpty(visitorDataList)) {
            for (NocAccessRequestVisitorData visitorData : visitorDataList) {
                nocAuditTrailService.insertViewRequestVisitorAuditTrail(visitorData.getVisitorAccessId());
            }
        }
    }
}
