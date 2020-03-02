package com.hkt.btu.sd.facade.impl;

import com.hkt.btu.common.core.exception.InvalidInputException;
import com.hkt.btu.common.core.service.bean.BtuApiProfileBean;
import com.hkt.btu.common.facade.AbstractRestfulApiFacade;
import com.hkt.btu.common.facade.data.BtuPageData;
import com.hkt.btu.sd.core.service.SdApiService;
import com.hkt.btu.sd.facade.OssApiFacade;
import com.hkt.btu.sd.facade.data.oss.*;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Resource;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class OssApiFacadeImpl extends AbstractRestfulApiFacade implements OssApiFacade {
    private static final Logger LOG = LogManager.getLogger(OssApiFacadeImpl.class);

    @Resource(name = "apiService")
    SdApiService apiService;

    @Override
    protected BtuApiProfileBean getTargetApiProfile() {
        return apiService.getOssApiProfileBean();
    }

    @Override
    public OssSmartMeterData queryMeterInfo(Integer poleId) {
        String apiPath = "/govpm-web/api/servicedesk/meterInfo";
        Map<String, String> queryParamMap = Map.of(
                "poleID", String.valueOf(poleId) );
        OssSmartMeterWrapData wrapData = getData(apiPath, OssSmartMeterWrapData.class, queryParamMap);
        return wrapData==null ? null : wrapData.getMeter();

//        OssSmartMeterData testingData = new OssSmartMeterData();
//        testingData.setPoleId("10001");
//        testingData.setModel("GIT");
//        testingData.setExchange("LHK");
//        testingData.setSb("229119");
//        testingData.setRegion("Kowloon");
//        testingData.setDistrict("Sham Shui Po");
//        testingData.setStreet("Yuet Lun Street");
//        testingData.setStreetSection("Manhattan Hill");
//        testingData.setLatitude(22.2388);
//        testingData.setLongitude(114.194);
//        return testingData;
    }

    @Override
    public String notifyTicketStatus(Integer poleId, Integer ticketId, LocalDateTime time, String action) {
        LOG.info("Notifying ticket status... (poleId={}, ticket={}, status={})", poleId, ticketId, action);
        String apiPath = "/govpm-web/api/servicedesk/notifyTicketStatus";

        // prepare post body
        OssSmartMeterStatusUpdateData statusUpdateData = new OssSmartMeterStatusUpdateData();
        statusUpdateData.setPoleId(poleId);
        statusUpdateData.setTicketId(ticketId);
        statusUpdateData.setTime(time);
        statusUpdateData.setAction(action);
        Entity<OssSmartMeterStatusUpdateData> postBodyEntity = Entity.entity(statusUpdateData, MediaType.APPLICATION_JSON_TYPE);

        Response response = postEntity(apiPath, postBodyEntity);
        return response.toString();
    }

    @Override
    public BtuPageData<OssSmartMeterEventData> queryMeterEvents(Integer page, Integer pageSize, Integer poleId, LocalDateTime fromTime, LocalDateTime toTime) {
        // check input
        if(page==null){
            String errorMsg = "Null page.";
            LOG.error(errorMsg);
            throw new InvalidInputException(errorMsg);
        } else if (pageSize==null){
            String errorMsg = "Null pageSize.";
            LOG.error(errorMsg);
            throw new InvalidInputException(errorMsg);
        } else if (poleId==null){
            String errorMsg = "Null poleId.";
            LOG.error(errorMsg);
            throw new InvalidInputException(errorMsg);
        } else if (fromTime==null){
            String errorMsg = "Null fromTime.";
            LOG.error(errorMsg);
            throw new InvalidInputException(errorMsg);
        }

        String apiPath = "/govpm-web/api/servicedesk/meterEvents";
        pageSize = pageSize>100 ? 10 : pageSize;

        String fromTimeStr = fromTime.format(DateTimeFormatter.ISO_DATE_TIME);
        String toTimeStr = toTime==null ? StringUtils.EMPTY : toTime.format(DateTimeFormatter.ISO_DATE_TIME);

        Map<String, String> queryParamMap = Map.of(
                "poleID", String.valueOf(poleId),
                "fromTime", fromTimeStr,
                "toTime", toTimeStr,
                "page", String.valueOf(page),
                "pageSize", String.valueOf(pageSize));

//        OssSmartMeterEventWrapData pagedEventData = getData(
//                apiPath, OssSmartMeterEventWrapData.class, queryParamMap);
//        return pagedEventData.getEvents();

        OssSmartMeterEventData eventData = new OssSmartMeterEventData();
        eventData.setEventId(1234);
        eventData.setEventCode("TEST");
        eventData.setEventDesc("Testing 1.");
        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        eventData.setEventTime(df.format(LocalDateTime.of(2020, 1, 1, 1, 1, 2)));

        OssSmartMeterEventData eventData2 = new OssSmartMeterEventData();
        eventData2.setEventId(2345);
        eventData2.setEventCode("TEST-2");
        eventData2.setEventDesc("Testing 2.");
        eventData2.setEventTime(df.format(LocalDateTime.of(2020, 2, 2, 2, 2, 2)));

        List<OssSmartMeterEventData> dataList = new LinkedList<>();
        dataList.add(eventData);
        dataList.add(eventData2);

        OssSmartMeterEventWrapData pagedEventData = new OssSmartMeterEventWrapData();
        BtuPageData<OssSmartMeterEventData> events = new BtuPageData<>();
        events.setContent(dataList);
        events.setNumber(page);
        events.setSize(pageSize);
        events.setTotalElements((pageSize.longValue()));
        pagedEventData.setEvents(events);
        return pagedEventData.getEvents();

//        Pageable pageable = PageRequest.of(page, pageSize);
//        return new PageData<>(dataList, pageable, 1);
    }
}
