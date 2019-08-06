package com.hkt.btu.sd.core.service;


import com.hkt.btu.sd.core.service.bean.SdOperationHistBean;

import java.util.List;

public interface SdOperationHistService {
    // access request
    List<SdOperationHistBean> getAccessRequestOptHistList(Integer accessRequestId);
    void createAccessRequestOptHistStatusChange(Integer accessRequestId, String fromStatus, String toStatus, Integer userId);


}
