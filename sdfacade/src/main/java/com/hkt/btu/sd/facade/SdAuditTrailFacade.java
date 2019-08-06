package com.hkt.btu.sd.facade;


import com.hkt.btu.sd.facade.data.SdAccessRequestData;
import com.hkt.btu.sd.facade.data.SdAccessRequestVisitorData;
import com.hkt.btu.sd.facade.data.SdUserData;

import java.util.List;

public interface SdAuditTrailFacade {
    void insertViewUserAuditTrail(SdUserData sdUserData);
    void insertViewRequesterAuditTrail(SdAccessRequestData sdAccessRequestData);
    void insertViewRequestVisitorAuditTrail(List<SdAccessRequestVisitorData> visitorDataList);
}
