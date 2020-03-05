package com.hkt.btu.sd.core.service;

import com.hkt.btu.sd.core.service.bean.SdAuditTrailStatisticBean;
import com.hkt.btu.sd.core.service.bean.SdTicketChartBean;

import java.util.List;

public interface SdStatisticService {

    List<SdAuditTrailStatisticBean> getLoginCountLast90Days();

    List<SdAuditTrailStatisticBean> getLoginCountLastTwoWeeks();

    SdTicketChartBean ticketTypeCountPerOwnerGroup();

    SdTicketChartBean ticketStatusCountPerOwnerGroup();
}
