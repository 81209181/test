package com.hkt.btu.noc.core.service;


import com.hkt.btu.noc.core.service.bean.NocOperationHistBean;

import java.util.List;

public interface NocOperationHistService {
    // access request
    List<NocOperationHistBean> getAccessRequestOptHistList(Integer accessRequestId);
    void createAccessRequestOptHistStatusChange(Integer accessRequestId, String fromStatus, String toStatus, Integer userId);


}
