package com.hkt.btu.sd.facade.impl;

import com.hkt.btu.common.facade.data.BtuSimpleResponseData;
import com.hkt.btu.common.facade.data.PageData;
import com.hkt.btu.sd.core.service.bean.SdServiceTypeBean;
import com.hkt.btu.sd.core.service.bean.SdTicketServiceBean;
import com.hkt.btu.sd.core.service.constant.TicketStatusEnum;
import com.hkt.btu.sd.facade.OssApiFacade;
import com.hkt.btu.sd.facade.SdSmartMeterFacade;
import com.hkt.btu.sd.facade.SdTicketFacade;
import com.hkt.btu.sd.facade.constant.OssTicketActionEnum;
import com.hkt.btu.sd.facade.constant.OssWorkingPartyEnum;
import com.hkt.btu.sd.facade.constant.ServiceSearchEnum;
import com.hkt.btu.sd.facade.data.*;
import com.hkt.btu.sd.facade.data.cloud.Attachment;
import com.hkt.btu.sd.facade.data.cloud.Attribute;
import com.hkt.btu.sd.facade.data.oss.OssCaseData;
import com.hkt.btu.sd.facade.data.oss.OssSmartMeterData;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

public class SdSmartMeterFacadeImpl implements SdSmartMeterFacade {
    private static final Logger LOG = LogManager.getLogger(SdTicketFacadeImpl.class);
    private final static DateTimeFormatter DEFAULT_DATE_TIME_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Resource(name = "ticketFacade")
    SdTicketFacade ticketFacade;
    @Resource(name = "ossApiFacade")
    OssApiFacade ossApiFacade;

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

        // check active work ticket of the pole ID
        Pageable pageable = PageRequest.of(0, 1);
        PageData<SdTicketMasData> pagedWorkTicketData = searchTicketList(
                pageable,
                poleId, null, null, null,
                null, TicketStatusEnum.WORKING.getStatusCode());
        if(pagedWorkTicketData.getTotalElements() > 0){
            SdTicketMasData activeTicketMasData = pagedWorkTicketData.getContent().get(0);
            String activeTicketMasId = String.valueOf(activeTicketMasData.getTicketMasId());

            LOG.info("Found existing smart meter work ticket. (ticketMasId={}, poleId={})", activeTicketMasId, poleId);
            return BtuSimpleResponseData.of(true, activeTicketMasId, null);
        }
        LOG.info("Found no existing smart meter work ticket. (poleId={})", poleId);

        // check existence of pole id
        OssSmartMeterData ossSmartMeterData = ossApiFacade.queryMeterInfo(String.valueOf(poleId));
        if( ossSmartMeterData==null || StringUtils.isEmpty(ossSmartMeterData.getPoleId())){
            String warnMsg = "Meter profile not found in OSS. (poleId=" + poleId + ")";
            LOG.warn(warnMsg);
            return BtuSimpleResponseData.of(false, null, warnMsg);
        }
        LOG.info("Checked smart meter profile with OSS. (poleId={})", poleId);

        // get mapped symptom
        String symptomCode = translateToSymptom(workingPartyList);
        if(StringUtils.isEmpty(symptomCode)){
            String warnMsg = String.format("Cannot map symptom for input. (workingPartyList=%s)",
                    StringUtils.join(workingPartyList, ','));
            LOG.warn(warnMsg);
            return BtuSimpleResponseData.of(false, null, warnMsg);
        }

        // check active ticket of the pole ID
        Integer ticketMasId;
        PageData<SdTicketMasData> pagedActiveTicketData = searchTicketList(
                pageable,
                poleId, null, null, null,
                null, TicketStatusEnum.OPEN.getStatusCode());
        if(pagedActiveTicketData.getTotalElements() > 0){
            SdTicketMasData activeTicketMasData = pagedActiveTicketData.getContent().get(0);
            ticketMasId = activeTicketMasData.getTicketMasId();
            LOG.info("Found existing smart meter active ticket. (ticketMasId={}, poleId={})",
                    String.valueOf(ticketMasId), poleId);
        }else{
            // SD: create query ticket
            SdQueryTicketRequestData queryTicketRequestData = buildTicketServiceData(String.valueOf(poleId), SdServiceTypeBean.SERVICE_TYPE.SMART_METER);
            try{
                ticketMasId = ticketFacade.createQueryTicket(queryTicketRequestData);
                LOG.info("Created new smart meter query ticket. (ticketMasId={}, poleId={})", ticketMasId, poleId);
            } catch (RuntimeException e){
                LOG.error(e.getMessage(), e);
                String warnMsg = "Cannot create new ticket. (poleId=" + poleId + ")";
                LOG.warn(warnMsg);
                return BtuSimpleResponseData.of(false, null, warnMsg);
            }
        }

        // SD: add symptom to ticket
        SdRequestTicketServiceData sdRequestTicketServiceData = buildTicketServiceData(
                ticketMasId, String.valueOf(poleId), reportTime, symptomCode);
        List<SdRequestTicketServiceData> ticketServiceList = List.of(sdRequestTicketServiceData);
        try {
            ticketFacade.updateServiceInfo(ticketServiceList);
            LOG.info("Updated symptom to ticket. (ticketMasId={}, poleId={}, symptomCode={})", ticketMasId, poleId, symptomCode);
        } catch (RuntimeException e){
            LOG.error(e.getMessage(), e);
            String warnMsg = "Meter profile not found in OSS. (poleId=" + poleId + ")";
            LOG.warn(warnMsg);
            return BtuSimpleResponseData.of(false, null, warnMsg);
        }

        // SD-->WFM: auto-pass to wfm
        try {
            ticketFacade.createJob4Wfm(ticketMasId, false);
            LOG.info("Created job in WFM. (ticketMasId={}, poleId={})", ticketMasId, poleId);
        } catch (RuntimeException e){
            LOG.error(e.getMessage(), e);
            String warnMsg = "Cannot create job in WFM. (ticketMasId=" + ticketMasId + ", poleId=" + poleId + ")";
            LOG.warn(warnMsg);
            return BtuSimpleResponseData.of(false, null, warnMsg);
        }

        return BtuSimpleResponseData.of(true, String.valueOf(ticketMasId), null);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public BtuSimpleResponseData createTicket4GMB(OssCaseData ossCaseData) {
        String serviceType = ossCaseData.getServiceType();
        String identityId = ossCaseData.getIdentityId();
        LocalDateTime reportTime = ossCaseData.getTriggerTime();
        List<String> workingPartyList = ossCaseData.getWorkingPartyList();
        List<SdTicketContactData> contactInfo = ossCaseData.getContactInfo();
        List<Attribute> attributes = ossCaseData.getAttributes();
        List<Attachment> attachments = ossCaseData.getAttachments();

        if(identityId==null){
            LOG.warn("Null identity ID.");
            return BtuSimpleResponseData.of(false, null, "Null identity ID.");
        }else if(reportTime==null){
            LOG.warn("Null report time.");
            return BtuSimpleResponseData.of(false, null, "Null report time.");
        }else if(CollectionUtils.isEmpty(workingPartyList)){
            LOG.warn("Empty working party list.");
            return BtuSimpleResponseData.of(false, null, "Empty working party list.");
        }

        // check active work ticket of the pole ID
        Pageable pageable = PageRequest.of(0, 1);
        PageData<SdTicketMasData> pagedWorkTicketData = new PageData<>();
        if (serviceType.equals(SdServiceTypeBean.SERVICE_TYPE.GMB)) {
            pagedWorkTicketData = searchTicketList(
                    pageable, null, identityId, null, null,
                    null, TicketStatusEnum.WORKING.getStatusCode());
        } else if (serviceType.equals(SdServiceTypeBean.SERVICE_TYPE.SMART_METER)) {
            pagedWorkTicketData = searchTicketList(
                    pageable, Integer.parseInt(identityId), null, null, null,
                    null, TicketStatusEnum.WORKING.getStatusCode());
        }
        if(pagedWorkTicketData.getTotalElements() > 0){
            SdTicketMasData activeTicketMasData = pagedWorkTicketData.getContent().get(0);
            String activeTicketMasId = String.valueOf(activeTicketMasData.getTicketMasId());

            LOG.info("Found existing smart meter work ticket. (ticketMasId={}, identityId={})", activeTicketMasId, identityId);
            return BtuSimpleResponseData.of(true, activeTicketMasId, null);
        }
        LOG.info("Found no existing smart meter work ticket. (identityId={})", identityId);

        // check existence of pole id
        OssSmartMeterData ossSmartMeterData = ossApiFacade.queryMeterInfo(identityId);
        if( ossSmartMeterData==null || StringUtils.isEmpty(ossSmartMeterData.getPoleId())){
            String warnMsg = "Meter profile not found in OSS. (identityId=" + identityId + ")";
            LOG.warn(warnMsg);
            return BtuSimpleResponseData.of(false, null, warnMsg);
        }
        LOG.info("Checked smart meter profile with OSS. (identityId={})", identityId);

        // get mapped symptom
        String symptomCode = translateToSymptom(workingPartyList);
        if(StringUtils.isEmpty(symptomCode)){
            String warnMsg = String.format("Cannot map symptom for input. (workingPartyList=%s)",
                    StringUtils.join(workingPartyList, ','));
            LOG.warn(warnMsg);
            return BtuSimpleResponseData.of(false, null, warnMsg);
        }

        // check active ticket of the pole ID
        Integer ticketMasId;
        PageData<SdTicketMasData> pagedActiveTicketData = new PageData<>();
        if (serviceType.equals(SdServiceTypeBean.SERVICE_TYPE.GMB)) {
            pagedActiveTicketData = searchTicketList(
                    pageable, null, identityId, null, null,
                    null, TicketStatusEnum.OPEN.getStatusCode());
        } else if (serviceType.equals(SdServiceTypeBean.SERVICE_TYPE.SMART_METER)) {
            pagedActiveTicketData = searchTicketList(
                    pageable, Integer.parseInt(identityId), null, null, null,
                    null, TicketStatusEnum.OPEN.getStatusCode());
        }
        if(pagedActiveTicketData.getTotalElements() > 0){
            SdTicketMasData activeTicketMasData = pagedActiveTicketData.getContent().get(0);
            ticketMasId = activeTicketMasData.getTicketMasId();
            LOG.info("Found existing smart meter active ticket. (ticketMasId={}, identityId={})",
                    String.valueOf(ticketMasId), identityId);
        }else{
            // SD: create query ticket
            SdQueryTicketRequestData queryTicketRequestData = buildTicketServiceData(identityId, serviceType);
            try{
                ticketMasId = ticketFacade.createQueryTicket(queryTicketRequestData);
                LOG.info("Created new smart meter query ticket. (ticketMasId={}, identityId={})", ticketMasId, identityId);
            } catch (RuntimeException e){
                LOG.error(e.getMessage(), e);
                String warnMsg = "Cannot create new ticket. (identityId=" + identityId + ")";
                LOG.warn(warnMsg);
                return BtuSimpleResponseData.of(false, null, warnMsg);
            }
        }

        // SD: add symptom to ticket
        SdRequestTicketServiceData sdRequestTicketServiceData = buildTicketServiceData(
                ticketMasId, identityId, reportTime, symptomCode);
        List<SdRequestTicketServiceData> ticketServiceList = List.of(sdRequestTicketServiceData);
        try {
            ticketFacade.updateServiceInfo(ticketServiceList);
            LOG.info("Updated symptom to ticket. (ticketMasId={}, identityId={}, symptomCode={})", ticketMasId, identityId, symptomCode);
        } catch (RuntimeException e){
            LOG.error(e.getMessage(), e);
            String warnMsg = "Meter profile not found in OSS. (identityId=" + identityId + ")";
            LOG.warn(warnMsg);
            return BtuSimpleResponseData.of(false, null, warnMsg);
        }

        // SD: add contact to ticket
        if (CollectionUtils.isNotEmpty(contactInfo)) {
            contactInfo.stream().forEach(sdTicketContactData -> sdTicketContactData.setTicketMasId(ticketMasId));
            ticketFacade.updateContactInfo(contactInfo);
        }

        // SD: upload file to ticket
        if (CollectionUtils.isNotEmpty(attachments)) {
            ticketFacade.insertUploadFile(ticketMasId, attachments);
        }

        // SD-->WFM: auto-pass to wfm
        try {
            ticketFacade.createJob4Wfm(ticketMasId, false);
            LOG.info("Created job in WFM. (ticketMasId={}, identityId={})", ticketMasId, identityId);
        } catch (RuntimeException e){
            LOG.error(e.getMessage(), e);
            String warnMsg = "Cannot create job in WFM. (ticketMasId=" + ticketMasId + ", identityId=" + identityId + ")";
            LOG.warn(warnMsg);
            return BtuSimpleResponseData.of(false, null, warnMsg);
        }

        return BtuSimpleResponseData.of(true, String.valueOf(ticketMasId), null);
    }

    @Override
    public void notifyCloseMeterTicket(Integer ticketMasId) {
        // get close date
        SdTicketMasData ticketMasData = ticketFacade.getTicketMas(ticketMasId);
        String completeDate = ticketMasData==null ? LocalDateTime.now().format(DEFAULT_DATE_TIME_FORMAT) : ticketMasData.getCompleteDate().format(DEFAULT_DATE_TIME_FORMAT);
        String arrivalDate = ( ticketMasData==null || ticketMasData.getArrivalDate()==null )? null : ticketMasData.getArrivalDate().format(DEFAULT_DATE_TIME_FORMAT);

        // get pole id
        List<SdTicketServiceData> serviceInfo = ticketFacade.getServiceInfo(ticketMasId);
        if (CollectionUtils.isEmpty(serviceInfo)) {
            LOG.warn("Cannot notify OSS without service details. (ticketMasId={})", ticketMasId);
            return;
        }

        // notify OSS per pole
        for(SdTicketServiceData serviceData : serviceInfo) {
            String poleIdStr = serviceData.getServiceCode();
            Integer poleId = StringUtils.isEmpty(poleIdStr) || StringUtils.startsWith(poleIdStr, SdTicketServiceBean.DUMMY_POLE_ID_PREFIX) ?
                    null : Integer.parseInt(poleIdStr);
            if (poleId == null) {
                LOG.warn("Cannot notify OSS with pole ID={}.", poleIdStr);
                continue;
            }

            if(arrivalDate!=null) {
                ossApiFacade.notifyTicketStatus(poleId, ticketMasId, arrivalDate, OssTicketActionEnum.ARRIVAL.getCode());
            }
            ossApiFacade.notifyTicketStatus(poleId, ticketMasId, completeDate, OssTicketActionEnum.CLOSE.getCode());
        }
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
    public PageData<SdTicketMasData> searchTicketList(
            Pageable pageable, Integer poleId, String plateId, String createDateFromStr, String createDateToStr,
            String ticketType, String status) {
        String serviceType = poleId == null ? SdServiceTypeBean.SERVICE_TYPE.GMB : SdServiceTypeBean.SERVICE_TYPE.SMART_METER;
        String serviceNumber = poleId == null ? plateId : String.valueOf(poleId);
        createDateFromStr = createDateFromStr==null ? StringUtils.EMPTY : createDateFromStr;
        createDateToStr = createDateToStr==null ? StringUtils.EMPTY : createDateToStr;
        ticketType = ticketType==null ? StringUtils.EMPTY : ticketType;
        status = status==null ? StringUtils.EMPTY : status;

        Map<String, String> searchFormData = Map.of(
                "serviceType", serviceType,
                "serviceNumber", serviceNumber,
                "createDateFrom", createDateFromStr,
                "createDateTo", createDateToStr,
                "ticketType", ticketType,
                "status", status
        );

        return ticketFacade.searchTicketList(pageable, searchFormData);
    }

    @Override
    public String translateToSymptom(List<String> workingPartyList) {
        boolean hasPnd = false;
        boolean hasField = false;
        for(String workingParty : workingPartyList){
            if(OssWorkingPartyEnum.PND.getCode().equals(workingParty)){
                hasPnd = true;
            } else if (OssWorkingPartyEnum.FIELD.getCode().equals(workingParty)){
                hasField = true;
            } else {
                return null;
            }
        }


        if(hasPnd && hasField) {
            return "CL007";
        } else if(hasPnd) {
            return "CL006";
        } else if (hasField) {
            return "CL005";
        } else {
            return null;
        }
    }

    private SdRequestTicketServiceData buildTicketServiceData(Integer ticketMasId, String identityId, LocalDateTime reportTime, String symptomCode){
        SdRequestTicketServiceData sdRequestTicketServiceData = new SdRequestTicketServiceData();
        sdRequestTicketServiceData.setTicketMasId(ticketMasId);
        sdRequestTicketServiceData.setServiceCode(identityId);
        sdRequestTicketServiceData.setReportTime(reportTime);
        sdRequestTicketServiceData.setFaults(List.of(symptomCode));
        return sdRequestTicketServiceData;
    }

    private SdQueryTicketRequestData buildTicketServiceData(String identityId, String serviceType){
        SdQueryTicketRequestData queryTicketRequestData = new SdQueryTicketRequestData();
        queryTicketRequestData.setSearchKey(ServiceSearchEnum.POLE_ID.getKey());
        queryTicketRequestData.setSearchValue(identityId);
        queryTicketRequestData.setServiceType(serviceType);
        queryTicketRequestData.setServiceNo(identityId);
        return queryTicketRequestData;
    }


}
