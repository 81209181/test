package com.hkt.btu.sd.core.service.impl;

import com.hkt.btu.common.core.exception.UserNotFoundException;
import com.hkt.btu.sd.core.dao.entity.SdAccessRequestEntity;
import com.hkt.btu.sd.core.dao.entity.SdConfigParamEntity;
import com.hkt.btu.sd.core.dao.entity.SdUserEntity;
import com.hkt.btu.sd.core.dao.mapper.SdAccessRequestMapper;
import com.hkt.btu.sd.core.dao.populator.SdAccessRequestEntityPopulator;
import com.hkt.btu.sd.core.exception.*;
import com.hkt.btu.sd.core.service.*;
import com.hkt.btu.sd.core.service.bean.*;
import com.hkt.btu.sd.core.service.populator.SdAccessRequestBeanPopulator;
import com.hkt.btu.sd.core.service.populator.SdCompanyBeanPopulator;
import com.hkt.btu.sd.core.service.populator.SdUserBeanPopulator;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import javax.mail.MessagingException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;

public class SdAccessRequestServiceImpl implements SdAccessRequestService {
    private static final Logger LOG = LogManager.getLogger(SdAccessRequestServiceImpl.class);


    @Resource(name = "userService")
    SdUserService sdUserService;
    @Resource(name = "companyService")
    SdCompanyService sdCompanyService;
    @Resource(name = "accessRequestVisitorService")
    SdAccessRequestVisitorService sdAccessRequestVisitorService;
    @Resource(name = "accessRequestEquipService")
    SdAccessRequestEquipService sdAccessRequestEquipService;
    @Resource(name = "operationHistService")
    SdOperationHistService sdOperationHistService;
    @Resource(name = "emailService")
    SdEmailService sdEmailService;
    @Resource(name = "configParamService")
    SdConfigParamService sdConfigParamService;

    @Resource
    SdAccessRequestMapper sdAccessRequestMapper;

    @Resource(name = "accessRequestEntityPopulator")
    SdAccessRequestEntityPopulator sdAccessRequestEntityPopulator;
    @Resource(name = "accessRequestBeanPopulator")
    SdAccessRequestBeanPopulator sdAccessRequestBeanPopulator;


    @Resource(name = "userBeanPopulator")
    SdUserBeanPopulator sdUserBeanPopulator;
    @Resource(name = "companyBeanPopulator")
    SdCompanyBeanPopulator sdCompanyBeanPopulator;


    @Override
    @Transactional
    public Integer createAccessRequest(SdAccessRequestBean sdAccessRequestBean)
            throws UserNotFoundException, CompanyNotFoundException, InvalidInputException, LocationNotFoundException {
        // check visit time
        final LocalDateTime TODAY_START = LocalDateTime.now().truncatedTo(ChronoUnit.DAYS);
        LocalDateTime visitFrom = sdAccessRequestBean.getVisitDateFrom();
        LocalDateTime visitTo = sdAccessRequestBean.getVisitDateTo();
        if( TODAY_START.isAfter(visitFrom) || TODAY_START.isAfter(visitTo) ){
            throw new InvalidInputException("Visit time is in the past.");
        }else if( visitTo.isBefore(visitFrom) ){
            throw new InvalidInputException("Visit end time is before visit start time.");
        }

        // check location
        List<SdAccessRequestLocBean> locBeanList = getAccessRequestLocList();
        String inputLoc = sdAccessRequestBean.getVisitLocation();
        SdAccessRequestLocBean inputLocBean = locBeanList.stream()
                .filter(locBean -> StringUtils.equals(locBean.getLocId(), inputLoc))
                .findAny()
                .orElse(null);
        if( inputLocBean==null ){
            throw new LocationNotFoundException("Unknown visit location.");
        }

        // get requester info
        SdUserBean requesterUserBean = (SdUserBean) sdUserService.getCurrentUserBean();
        SdCompanyBean requesterCompanyBean = sdCompanyService.getCompanyById(requesterUserBean.getCompanyId());

        // fill-in visitor info for non-admin
        boolean canOnlySubmitSelfVisit = canOnlySubmitSelfVisit();
        if(canOnlySubmitSelfVisit){
            SdAccessRequestVisitorBean selfVisitorBean = sdAccessRequestVisitorService.getSelfVisitVisitorBean(requesterUserBean, requesterCompanyBean);
            List<SdAccessRequestVisitorBean> visitorBeanList = sdAccessRequestBean.getRequestVisitorBeanList();
            SdAccessRequestVisitorBean inputVisitorBean = CollectionUtils.isEmpty(visitorBeanList) ? null : visitorBeanList.get(0);
            if( visitorBeanList.size()!=1){
                throw new InvalidInputException("This user can only apply for self visit.");
            } else if ( ! sdAccessRequestVisitorService.isSameVisitorProfile(selfVisitorBean, inputVisitorBean) ){
                throw new InvalidInputException("Un-matching data between requestor and visitor for self visit.");
            }
        }

        // prepare access request entity
        SdAccessRequestEntity sdAccessRequestEntity = new SdAccessRequestEntity();
        sdAccessRequestEntityPopulator.populateCreateEntity(requesterUserBean, sdAccessRequestEntity);
        sdAccessRequestEntityPopulator.populateCreateEntity(requesterCompanyBean, sdAccessRequestEntity);
        sdAccessRequestEntityPopulator.populateCreateEntity(sdAccessRequestBean, sdAccessRequestEntity);
        sdAccessRequestEntity.setStatus(SdAccessRequestEntity.STATUS.APPROVED);

        // insert access request
        sdAccessRequestMapper.insertAccessRequest(sdAccessRequestEntity);
        // get new access request id
        Integer newAccessRequestId = sdAccessRequestEntity.getAccessRequestId();

        // insert visitor
        sdAccessRequestVisitorService.createVisitorForAccessRequest(sdAccessRequestBean.getRequestVisitorBeanList(), newAccessRequestId, requesterUserBean);
        // insert equip
        sdAccessRequestEquipService.createEquipForAccessRequest(sdAccessRequestBean.getRequestEquipBeanList(), newAccessRequestId, requesterUserBean);

        // insert hist
        sdOperationHistService.createAccessRequestOptHistStatusChange(
                newAccessRequestId, null, sdAccessRequestEntity.getStatus(), requesterUserBean.getUserId());

        return newAccessRequestId;
    }

    public void sendAccessRequestCfmEmail(Integer newAccessRequestId) throws MessagingException, AccessRequestNotFoundException {
        SdAccessRequestBean sdAccessRequestBean = getAccessRequestBasicInfoByRequestId(newAccessRequestId);
        if(sdAccessRequestBean==null){
            throw new AccessRequestNotFoundException();
        }else if(sdAccessRequestBean.getRequester()==null){
            throw new AccessRequestNotFoundException("Null access requester.");
        }else if(sdAccessRequestBean.getVisitDateFrom()==null){
            throw new AccessRequestNotFoundException("Null visit start time.");
        }else if(sdAccessRequestBean.getVisitDateTo()==null){
            throw new AccessRequestNotFoundException("Null visit end time.");
        }

        String requestorName = sdAccessRequestBean.getRequester().getName();
        String requestorUserId = sdAccessRequestBean.getRequester().getUserId().toString();
        String requestorCompany = sdAccessRequestBean.getRequesterCompany().getName();
        String ticketNum = sdAccessRequestBean.getHashedRequestId()==null ?
                null : String.format("%07d", sdAccessRequestBean.getHashedRequestId());
        String visitLoc = sdAccessRequestBean.getVisitLocation();
        String visitDate = sdAccessRequestBean.getVisitDateFrom().toLocalDate().toString();
        String visitTime = String.format( "%s - %s",
                sdAccessRequestBean.getVisitDateFrom().toLocalTime().toString(),
                sdAccessRequestBean.getVisitDateTo().toLocalTime().toString() );
        String visitorCount = sdAccessRequestBean.getVisitorCount().toString();

        // try send email
        String emailErrorMsg = "";
        try {
            String recipient = sdAccessRequestBean.getRequester().getEmail();
            String recipientName = sdAccessRequestBean.getRequester().getName();

            Map<String, Object> dataMap = new HashMap<>();
            dataMap.put(SdEmailBean.ACCESS_REQUEST_CFM_CUST_EMAIL.TICKET_NUM, ticketNum);
            dataMap.put(SdEmailBean.ACCESS_REQUEST_CFM_CUST_EMAIL.REQUESTOR_NAME, requestorName);
            dataMap.put(SdEmailBean.ACCESS_REQUEST_CFM_CUST_EMAIL.VISIT_LOC, visitLoc);
            dataMap.put(SdEmailBean.ACCESS_REQUEST_CFM_CUST_EMAIL.VISIT_DATE, visitDate);
            dataMap.put(SdEmailBean.ACCESS_REQUEST_CFM_CUST_EMAIL.VISIT_TIME, visitTime);
            dataMap.put(SdEmailBean.ACCESS_REQUEST_CFM_CUST_EMAIL.VISITOR_COUNT, visitorCount);
            dataMap.put(SdEmailBean.EMAIL_BASIC_RECIPIENT_NAME, recipientName);

            sdEmailService.send(SdEmailBean.ACCESS_REQUEST_CFM_CUST_EMAIL.TEMPLATE_ID, recipient, dataMap);
        } catch (MessagingException e){
            LOG.error(e.getMessage(), e);
            emailErrorMsg += "Fail to send confirmation email to customer. ";
        }
        try {
            String recipient = sdConfigParamService.getString(SdConfigParamEntity.VISIT_LOC_EMAIL.CONFIG_GROUP, visitLoc);
            if(StringUtils.isEmpty(recipient)){
                // backup email if visit location email not found
                recipient = sdConfigParamService.getString(SdConfigParamEntity.NFM.CONFIG_GROUP, SdConfigParamEntity.NFM.CONFIG_KEY_EMAIL);
            }

            Map<String, Object> dataMap = new HashMap<>();
            dataMap.put(SdEmailBean.ACCESS_REQUEST_CFM_NFM_EMAIL.TICKET_NUM, ticketNum);
            dataMap.put(SdEmailBean.ACCESS_REQUEST_CFM_NFM_EMAIL.REQUESTOR_NAME, requestorName);
            dataMap.put(SdEmailBean.ACCESS_REQUEST_CFM_NFM_EMAIL.REQUESTOR_USER_ID, requestorUserId);
            dataMap.put(SdEmailBean.ACCESS_REQUEST_CFM_NFM_EMAIL.REQUESTOR_COMPANY, requestorCompany);
            dataMap.put(SdEmailBean.ACCESS_REQUEST_CFM_NFM_EMAIL.VISIT_LOC, visitLoc);
            dataMap.put(SdEmailBean.ACCESS_REQUEST_CFM_NFM_EMAIL.VISIT_DATE, visitDate);
            dataMap.put(SdEmailBean.ACCESS_REQUEST_CFM_NFM_EMAIL.VISIT_TIME, visitTime);
            dataMap.put(SdEmailBean.ACCESS_REQUEST_CFM_NFM_EMAIL.VISITOR_COUNT, visitorCount);

            sdEmailService.send(SdEmailBean.ACCESS_REQUEST_CFM_NFM_EMAIL.TEMPLATE_ID, recipient, dataMap);
        } catch (MessagingException e){
            LOG.error(e.getMessage(), e);
            emailErrorMsg += "Fail to send notification email to NFM. ";
        }
        if(! StringUtils.isEmpty(emailErrorMsg)){
            throw new MessagingException(emailErrorMsg);
        }
    }

    @Override
    public SdAccessRequestBean getAccessRequestByRequestId(Integer accessRequestId) throws AuthorityNotFoundException {
        if(accessRequestId==null){
            LOG.warn("Null request ID");
            return null;
        }

        // get request basic info
        SdAccessRequestBean sdAccessRequestBean = getAccessRequestBasicInfoByRequestId(accessRequestId);

        // populate visitors
        List<SdAccessRequestVisitorBean> visitorBeanList = sdAccessRequestVisitorService.getVisitorListByAccessRequestId(accessRequestId);
        sdAccessRequestBean.setRequestVisitorBeanList(visitorBeanList);

        // populate equipments
        List<SdAccessRequestEquipBean> equipBeanList = sdAccessRequestEquipService.getEquipListByAccessRequestId(accessRequestId);
        sdAccessRequestBean.setRequestEquipBeanList(equipBeanList);

        return sdAccessRequestBean;
    }

    @Override
    public SdAccessRequestBean getAccessRequestBasicInfoByRequestId(Integer accessRequestId)
            throws AuthorityNotFoundException{
        // determine company id restriction
        Integer companyId = sdUserService.getCompanyIdRestriction();
        // determine user id restriction
        Integer userId = sdUserService.getUserIdRestriction();

        // get data
        SdAccessRequestEntity sdAccessRequestEntity = sdAccessRequestMapper.getAccessRequest(accessRequestId, companyId, userId);
        if(sdAccessRequestEntity==null){
            LOG.warn( String.format("Access request ID %s not found. (companyId=%d, userId=%d)", accessRequestId, companyId, userId) );
            return null;
        }

        // populate request
        SdAccessRequestBean sdAccessRequestBean = new SdAccessRequestBean();
        sdAccessRequestBeanPopulator.populate(sdAccessRequestEntity, sdAccessRequestBean);

        // populate requester info
        SdUserBean requester = new SdUserBean();
        sdUserBeanPopulator.populate(sdAccessRequestEntity, requester);
        sdAccessRequestBean.setRequester(requester);
        SdCompanyBean requesterCompany = new SdCompanyBean();
        sdCompanyBeanPopulator.populate(sdAccessRequestEntity, requesterCompany);
        sdAccessRequestBean.setRequesterCompany(requesterCompany);

        return sdAccessRequestBean;
    }

    @Override
    public LinkedList<String> getAccessRequestStatusList() {
        LinkedList<String> result = new LinkedList<>();
        result.add(SdAccessRequestEntity.STATUS.APPROVED);
        result.add(SdAccessRequestEntity.STATUS.COMPLETED);
        return result;
    }

    // inventory part should be implemented properly in later phase
    @Override
    public List<SdAccessRequestLocBean> getAccessRequestLocList() {
        List<SdAccessRequestLocBean> result = new LinkedList<>();

        SdAccessRequestLocBean jbyBean = new SdAccessRequestLocBean();
        jbyBean.setLocId(SdAccessRequestEntity.LOC.JBY);
        jbyBean.setName(SdAccessRequestEntity.LOC.JBY_NAME);
        result.add(jbyBean);

        SdAccessRequestLocBean lkt2fBean = new SdAccessRequestLocBean();
        lkt2fBean.setLocId(SdAccessRequestEntity.LOC.LKT_2F);
        lkt2fBean.setName(SdAccessRequestEntity.LOC.LKT_2F_NAME);
        result.add(lkt2fBean);

        SdAccessRequestLocBean lkt5fBean = new SdAccessRequestLocBean();
        lkt5fBean.setLocId(SdAccessRequestEntity.LOC.LKT_5F);
        lkt5fBean.setName(SdAccessRequestEntity.LOC.LKT_5F_NAME);
        result.add(lkt5fBean);

        SdAccessRequestLocBean vtaBean = new SdAccessRequestLocBean();
        vtaBean.setLocId(SdAccessRequestEntity.LOC.VTA);
        vtaBean.setName(SdAccessRequestEntity.LOC.VTA_NAME);
        result.add(vtaBean);

        SdAccessRequestLocBean mcx3fBean = new SdAccessRequestLocBean();
        mcx3fBean.setLocId(SdAccessRequestEntity.LOC.MCX_3F);
        mcx3fBean.setName(SdAccessRequestEntity.LOC.MCX_3F_NAME);
        result.add(mcx3fBean);

        SdAccessRequestLocBean mcx6fBean = new SdAccessRequestLocBean();
        mcx6fBean.setLocId(SdAccessRequestEntity.LOC.MCX_6F);
        mcx6fBean.setName(SdAccessRequestEntity.LOC.MCX_6F_NAME);
        result.add(mcx6fBean);

        SdAccessRequestLocBean iacBean = new SdAccessRequestLocBean();
        iacBean.setLocId(SdAccessRequestEntity.LOC.IAC);
        iacBean.setName(SdAccessRequestEntity.LOC.IAC_NAME);
        result.add(iacBean);

        result.sort(Comparator.comparing(SdAccessRequestLocBean::getName));
        return result;
    }

    @Override
    public Page<SdAccessRequestBean> searchAccessRequest (
            Pageable pageable,
            Integer accessRequestId, String companyName, String status,
            String visitLocation, LocalDate visitDateFrom, LocalDate visitDateTo) throws AuthorityNotFoundException {
        long offset = pageable.getOffset();
        int pageSize = pageable.getPageSize();

        // determine company id restriction
        Integer companyId = sdUserService.getCompanyIdRestriction();

        // determine user id restriction
        Integer userId = sdUserService.getUserIdRestriction();


        LOG.info( String.format(
                "Searching company with {companyId: %s, userId: %s, " +
                        "accessRequestId: %s, status: %s, visitLocation: %s, visitDateFrom: %s, visitDateTo: %s}",
                companyId, userId,
                accessRequestId, status, visitLocation, visitDateFrom, visitDateTo) );

        // get request total count
        Integer totalCount = sdAccessRequestMapper.countSearchAccessRequest(
                companyId, userId,
                accessRequestId, companyName, status, visitLocation, visitDateFrom, visitDateTo);
        // get request content
        List<SdAccessRequestEntity> entityList = sdAccessRequestMapper.searchAccessRequest(
                offset, pageSize, companyId, userId,
                accessRequestId, companyName, status, visitLocation, visitDateFrom, visitDateTo);
        if( CollectionUtils.isEmpty(entityList) ){
            return new PageImpl<>(new LinkedList<>(), pageable, totalCount);
        }

        List<SdAccessRequestBean> beanList = new LinkedList<>();
        for (SdAccessRequestEntity entity : entityList){
            SdAccessRequestBean bean = new SdAccessRequestBean();
            sdAccessRequestBeanPopulator.populate(entity, bean);

            SdCompanyBean companyBean = new SdCompanyBean();
            sdCompanyBeanPopulator.populate(entity, companyBean);
            bean.setRequesterCompany(companyBean);

            beanList.add(bean);
        }


        return new PageImpl<>(beanList, pageable, totalCount);
    }


    private int updateStatusAccessRequestToComplete(Integer accessRequestId, String oldStatus, Integer modifyby) {
        // update status
        int counter = sdAccessRequestMapper.updateStatus(accessRequestId, SdAccessRequestEntity.STATUS.COMPLETED, modifyby);

        // insert hist
        sdOperationHistService.createAccessRequestOptHistStatusChange(
                accessRequestId, oldStatus, SdAccessRequestEntity.STATUS.COMPLETED, modifyby);

        return counter;
    }

    @Override
    @Transactional
    public void completeExpiredAccessRequest() {
        // get modifyby
        Integer modifyby = SdUserEntity.SYSTEM.USER_ID;

        // get all expired but non-complete request
        List<SdAccessRequestEntity> entityList = sdAccessRequestMapper.getExpiredAccessRequest(SdAccessRequestEntity.STATUS.COMPLETED);
        if(CollectionUtils.isEmpty(entityList)){
            LOG.info("Auto-completed ZERO access request.");
            return;
        }

        // update each to complete
        int counter = 0;
        for(SdAccessRequestEntity entity : entityList){
            try {
                counter += updateStatusAccessRequestToComplete(entity.getAccessRequestId(), entity.getStatus(), modifyby);
            } catch (RuntimeException e){
                LOG.error("Cannot auto-complete access request " + entity.getAccessRequestId() + ".");
                LOG.error(e.getMessage(), e);
            }
        }

        LOG.info("Auto-completed " + counter + " access requests.");
    }

    @Override
    public boolean canOnlySubmitSelfVisit() {
        boolean isInternalUser = sdUserService.isInternalUser();
        boolean isAdminUser = sdUserService.isAdminUser();
        return ! isInternalUser && ! isAdminUser;
    }

    @Override
    public SdAccessRequestVisitorBean getSelfVisitVisitorBean() {
        return null;
    }

    @Override
    public void checkRequestNowEligibleForCheckInOut(SdAccessRequestBean sdAccessRequestBean) throws InvalidWorkflowException, AccessRequestNotFoundException {
        final LocalDateTime NOW = LocalDateTime.now();

        // check status
        String status = sdAccessRequestBean.getStatus();
        if( ! StringUtils.equals(status, SdAccessRequestEntity.STATUS.APPROVED) ){
            throw new InvalidWorkflowException("Please seek access request approval.");
        }

        // check visit from
        LocalDateTime visitDateFrom = sdAccessRequestBean.getVisitDateFrom();
        if ( visitDateFrom==null ) {
            throw new InvalidWorkflowException("Empty visit start time.");
        } else if( ! NOW.isAfter(visitDateFrom) ){
            throw new InvalidWorkflowException("Request visit time not yet started.");
        }

        // check visit to
        LocalDateTime visitDateTo = sdAccessRequestBean.getVisitDateTo();
        if ( visitDateTo==null ) {
            throw new InvalidWorkflowException("Empty visit end time.");
        } else if( ! NOW.isBefore(visitDateTo) ){
            throw new InvalidWorkflowException("Request visit time ended.");
        }
    }

    @Override
    public SdAccessRequestBean getAccessRequestByVisitorId(Integer visitorAccessId) throws AuthorityNotFoundException {
        SdAccessRequestEntity sdAccessRequestEntity = sdAccessRequestMapper.getAccessRequestByVisitorAccessId(visitorAccessId);
        if( sdAccessRequestEntity==null ){
            throw new AccessRequestNotFoundException("Request of visitor " + visitorAccessId + " not found.");
        }

        SdAccessRequestBean sdAccessRequestBean = new SdAccessRequestBean();
        sdAccessRequestBeanPopulator.populate(sdAccessRequestEntity, sdAccessRequestBean);
        return sdAccessRequestBean;
    }

}
