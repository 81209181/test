package com.hkt.btu.sd.facade.impl;

import com.hkt.btu.common.core.service.bean.BtuApiProfileBean;
import com.hkt.btu.common.facade.AbstractRestfulApiFacade;
import com.hkt.btu.common.facade.data.PageData;
import com.hkt.btu.sd.facade.OssApiFacade;
import com.hkt.btu.sd.facade.data.oss.OssSmartMeterData;
import com.hkt.btu.sd.facade.data.oss.OssSmartMeterEventData;

import java.time.LocalDateTime;

public class OssApiFacadeImpl extends AbstractRestfulApiFacade implements OssApiFacade {
    @Override
    protected BtuApiProfileBean getTargetApiProfile() {
        return null;
    }

    @Override
    public OssSmartMeterData queryMeterInfo(Integer poleId) {
        OssSmartMeterData testingData = new OssSmartMeterData();
        testingData.setPoleId("10001");
        testingData.setModel("GIT");
        testingData.setExchange("LHK");
        testingData.setSb("229119");
        testingData.setRegion("Kowloon");
        testingData.setStreet("Yuet Lun Street");
        testingData.setStreetSection("Manhattan Hill");
        testingData.setLatitude(22.2388);
        testingData.setLongitude(114.194);
        return testingData;
    }

    @Override
    public String notifyTicketStatus(Integer poleId, Integer ticketId, LocalDateTime time, String action) {
        return null;
    }

    @Override
    public PageData<OssSmartMeterEventData> queryMeterEvents(Integer page, Integer pageSize, Integer poleId, LocalDateTime fromTime, LocalDateTime toTime) {
        return null;
    }
}
