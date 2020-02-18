package com.hkt.btu.sd.facade.impl;

import com.hkt.btu.common.core.service.bean.BtuApiProfileBean;
import com.hkt.btu.common.facade.AbstractRestfulApiFacade;
import com.hkt.btu.common.facade.data.PageData;
import com.hkt.btu.sd.facade.OssApiFacade;
import com.hkt.btu.sd.facade.data.oss.OssSmartMeterData;
import com.hkt.btu.sd.facade.data.oss.OssSmartMeterEventData;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;

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
        Pageable pageable = PageRequest.of(page, pageSize);

        OssSmartMeterEventData eventData = new OssSmartMeterEventData();
        eventData.setEventId(1234);
        eventData.setEventCode("TEST");
        eventData.setEventDesc("Testing 1.");
        eventData.setEventTime(LocalDateTime.of(2020, 1, 1, 1, 1, 2));

        OssSmartMeterEventData eventData2 = new OssSmartMeterEventData();
        eventData2.setEventId(2345);
        eventData2.setEventCode("TEST-2");
        eventData2.setEventDesc("Testing 2.");
        eventData2.setEventTime(LocalDateTime.of(2020, 2, 2, 2, 2, 2));

        List <OssSmartMeterEventData> dataList = new LinkedList<>();
        dataList.add(eventData);
        dataList.add(eventData2);

        return new PageData<>(dataList, pageable, 1);
    }
}
