package com.hkt.btu.sd.facade.impl;

import com.hkt.btu.common.facade.data.BtuSimpleResponseData;
import com.hkt.btu.common.facade.data.PageData;
import com.hkt.btu.sd.core.service.bean.SdServiceTypeBean;
import com.hkt.btu.sd.facade.SdSmartMeterFacade;
import com.hkt.btu.sd.facade.SdTicketFacade;
import com.hkt.btu.sd.facade.data.SdTicketData;
import com.hkt.btu.sd.facade.data.SdTicketMasData;
import com.hkt.btu.sd.facade.data.SdTicketServiceData;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.data.domain.Pageable;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

public class SdSmartMeterFacadeImpl implements SdSmartMeterFacade {
    private static final Logger LOG = LogManager.getLogger(SdTicketFacadeImpl.class);

    @Resource(name = "ticketFacade")
    SdTicketFacade ticketFacade;

    @Override
    public BtuSimpleResponseData createTicket(Integer poleId, LocalDateTime reportTime, List<String> workingPartyList) {
        if(poleId==null){
            LOG.warn("Null pole ID.");
            return BtuSimpleResponseData.of(false, null, "Null pole ID.");
        }else if(reportTime==null){
            LOG.warn("Null report time.");
            return BtuSimpleResponseData.of(false, null, "Null report time.");
        }else if(CollectionUtils.isEmpty(workingPartyList)){
            LOG.warn("Empty working party list.");
            return BtuSimpleResponseData.of(false, null, "Empty working party list.");
        }

        //todo: SERVDESK-348
        // check active ticket

        // create query ticket

        // add symptom?

        // auto-pass to wfm

        return BtuSimpleResponseData.of(true, "1234", null);
    }

    @Override
    public SdTicketData getTicketInfo(Integer ticketMasId) {
        SdTicketData ticketData = ticketFacade.getTicketInfo(ticketMasId);
        List<SdTicketServiceData> serviceDataList = ticketData==null ? null : ticketData.getServiceInfo();
        SdTicketServiceData serviceData = serviceDataList==null ? null : serviceDataList.get(0);
        String serviceType = serviceData==null ? null : serviceData.getServiceType();
        if(StringUtils.equals(serviceType, SdServiceTypeBean.SERVICE_TYPE.SMART_METER)){
            return ticketData;
        }else {
            LOG.warn("Ticket service type is not Smart Meter. (ticketMasId={}, serviceType={})", ticketMasId, serviceType);
            return null;
        }
    }

    @Override
    public PageData<SdTicketMasData> searchTicketList(Pageable pageable, Integer poleId, String createDateFromStr, String createDateToStr) {
        if(poleId==null){
            LOG.warn("Null pole ID.");
            return null;
        }

        createDateFromStr = createDateFromStr==null ? StringUtils.EMPTY : createDateFromStr;
        createDateToStr = createDateToStr==null ? StringUtils.EMPTY : createDateToStr;

        Map<String, String> searchFormData = Map.of(
                "serviceType", SdServiceTypeBean.SERVICE_TYPE.SMART_METER,
                "serviceNumber", String.valueOf(poleId),
                "createDateFrom", createDateFromStr,
                "createDateTo", createDateToStr
        );

        return ticketFacade.searchTicketList(pageable, searchFormData);
    }
}
