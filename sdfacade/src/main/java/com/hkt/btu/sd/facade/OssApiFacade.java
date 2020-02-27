package com.hkt.btu.sd.facade;

import com.hkt.btu.common.facade.data.BtuPageData;
import com.hkt.btu.sd.facade.data.oss.OssSmartMeterData;
import com.hkt.btu.sd.facade.data.oss.OssSmartMeterEventData;

import java.time.LocalDateTime;

public interface OssApiFacade {
    OssSmartMeterData  queryMeterInfo(Integer poleId);
    String notifyTicketStatus(Integer poleId, Integer ticketId, LocalDateTime time, String action);
    BtuPageData<OssSmartMeterEventData> queryMeterEvents(Integer page, Integer pageSize,
                                                      Integer poleId, LocalDateTime fromTime, LocalDateTime toTime);
}
