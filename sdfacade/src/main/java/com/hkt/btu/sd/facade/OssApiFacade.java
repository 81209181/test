package com.hkt.btu.sd.facade;

import com.hkt.btu.common.facade.data.BtuPageData;
import com.hkt.btu.sd.facade.data.oss.OssSmartMeterData;
import com.hkt.btu.sd.facade.data.oss.OssSmartMeterEventData;

import java.time.LocalDateTime;

public interface OssApiFacade {
    OssSmartMeterData  queryMeterInfo(String identityId);
    void notifyTicketStatus(Integer poleId, Integer ticketId, String time, String action);
    BtuPageData<OssSmartMeterEventData> queryMeterEvents(Integer page, Integer pageSize,
                                                         Integer poleId, LocalDateTime fromTime, LocalDateTime toTime);
}
