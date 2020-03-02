package com.hkt.btu.sd.core.service;

import com.hkt.btu.sd.core.service.bean.SdAuditTrailStatisticBean;

import java.util.List;

public interface SdStatisticService {

    List<SdAuditTrailStatisticBean> getLoginCountLast90Days();

    List<SdAuditTrailStatisticBean> getLoginCountLastTwoWeeks();
}
