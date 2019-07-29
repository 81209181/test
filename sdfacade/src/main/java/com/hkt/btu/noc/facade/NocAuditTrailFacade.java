package com.hkt.btu.noc.facade;


import com.hkt.btu.noc.facade.data.NocAccessRequestData;
import com.hkt.btu.noc.facade.data.NocAccessRequestVisitorData;
import com.hkt.btu.noc.facade.data.NocUserData;

import java.util.List;

public interface NocAuditTrailFacade {
    void insertViewUserAuditTrail(NocUserData nocUserData);
    void insertViewRequesterAuditTrail(NocAccessRequestData nocAccessRequestData);
    void insertViewRequestVisitorAuditTrail(List<NocAccessRequestVisitorData> visitorDataList);
}
