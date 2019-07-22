package com.hkt.btu.noc.core.service.impl;

import com.hkt.btu.noc.core.dao.entity.NocAccessRequestEntity;
import com.hkt.btu.noc.core.dao.entity.NocConfigParamEntity;
import com.hkt.btu.noc.core.dao.entity.NocUserEntity;
import com.hkt.btu.noc.core.dao.mapper.NocAccessRequestMapper;
import com.hkt.btu.noc.core.dao.populator.NocAccessRequestEntityPopulator;
import com.hkt.btu.noc.core.exception.*;
import com.hkt.btu.noc.core.service.*;
import com.hkt.btu.noc.core.service.bean.*;
import com.hkt.btu.noc.core.service.populator.NocAccessRequestBeanPopulator;
import com.hkt.btu.noc.core.service.populator.NocCompanyBeanPopulator;
import com.hkt.btu.noc.core.service.populator.NocUserBeanPopulator;
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

public class NocAccessRequestServiceImpl implements NocAccessRequestService {
    private static final Logger LOG = LogManager.getLogger(NocAccessRequestServiceImpl.class);


    @Resource(name = "userService")
    NocUserService nocUserService;
    @Resource(name = "companyService")
    NocCompanyService nocCompanyService;
    @Resource(name = "accessRequestVisitorService")
    NocAccessRequestVisitorService nocAccessRequestVisitorService;
    @Resource(name = "accessRequestEquipService")
    NocAccessRequestEquipService nocAccessRequestEquipService;
    @Resource(name = "operationHistService")
    NocOperationHistService nocOperationHistService;
    @Resource(name = "emailService")
    NocEmailService nocEmailService;
    @Resource(name = "configParamService")
    NocConfigParamService nocConfigParamService;

    @Resource
    NocAccessRequestMapper nocAccessRequestMapper;

    @Resource(name = "accessRequestEntityPopulator")
    NocAccessRequestEntityPopulator nocAccessRequestEntityPopulator;
    @Resource(name = "accessRequestBeanPopulator")
    NocAccessRequestBeanPopulator nocAccessRequestBeanPopulator;


    @Resource(name = "userBeanPopulator")
    NocUserBeanPopulator nocUserBeanPopulator;
    @Resource(name = "companyBeanPopulator")
    NocCompanyBeanPopulator nocCompanyBeanPopulator;


    @Override
    @Transactional
    public Integer createAccessRequest(NocAccessRequestBean nocAccessRequestBean)
            throws UserNotFoundException, CompanyNotFoundException, InvalidInputException, LocationNotFoundException {
        // check visit time
        final LocalDateTime TODAY_START = LocalDateTime.now().truncatedTo(ChronoUnit.DAYS);
        LocalDateTime visitFrom = nocAccessRequestBean.getVisitDateFrom();
        LocalDateTime visitTo = nocAccessRequestBean.getVisitDateTo();
        if( TODAY_START.isAfter(visitFrom) || TODAY_START.isAfter(visitTo) ){
            throw new InvalidInputException("Visit time is in the past.");
        }else if( visitTo.isBefore(visitFrom) ){
            throw new InvalidInputException("Visit end time is before visit start time.");
        }

        // check location
        List<NocAccessRequestLocBean> locBeanList = getAccessRequestLocList();
        String inputLoc = nocAccessRequestBean.getVisitLocation();
        NocAccessRequestLocBean inputLocBean = locBeanList.stream()
                .filter(locBean -> StringUtils.equals(locBean.getLocId(), inputLoc))
                .findAny()
                .orElse(null);
        if( inputLocBean==null ){
            throw new LocationNotFoundException("Unknown visit location.");
        }

        // get requester info
        NocUserBean requesterUserBean = (NocUserBean) nocUserService.getCurrentUserBean();
        NocCompanyBean requesterCompanyBean = nocCompanyService.getCompanyById(requesterUserBean.getCompanyId());

        // fill-in visitor info for non-admin
        boolean canOnlySubmitSelfVisit = canOnlySubmitSelfVisit();
        if(canOnlySubmitSelfVisit){
            NocAccessRequestVisitorBean selfVisitorBean = nocAccessRequestVisitorService.getSelfVisitVisitorBean(requesterUserBean, requesterCompanyBean);
            List<NocAccessRequestVisitorBean> visitorBeanList = nocAccessRequestBean.getRequestVisitorBeanList();
            NocAccessRequestVisitorBean inputVisitorBean = CollectionUtils.isEmpty(visitorBeanList) ? null : visitorBeanList.get(0);
            if( visitorBeanList.size()!=1){
                throw new InvalidInputException("This user can only apply for self visit.");
            } else if ( ! nocAccessRequestVisitorService.isSameVisitorProfile(selfVisitorBean, inputVisitorBean) ){
                throw new InvalidInputException("Un-matching data between requestor and visitor for self visit.");
            }
        }

        // prepare access request entity
        NocAccessRequestEntity nocAccessRequestEntity = new NocAccessRequestEntity();
        nocAccessRequestEntityPopulator.populateCreateEntity(requesterUserBean, nocAccessRequestEntity);
        nocAccessRequestEntityPopulator.populateCreateEntity(requesterCompanyBean, nocAccessRequestEntity);
        nocAccessRequestEntityPopulator.populateCreateEntity(nocAccessRequestBean, nocAccessRequestEntity);
        nocAccessRequestEntity.setStatus(NocAccessRequestEntity.STATUS.APPROVED);

        // insert access request
        nocAccessRequestMapper.insertAccessRequest(nocAccessRequestEntity);
        // get new access request id
        Integer newAccessRequestId = nocAccessRequestEntity.getAccessRequestId();

        // insert visitor
        nocAccessRequestVisitorService.createVisitorForAccessRequest(nocAccessRequestBean.getRequestVisitorBeanList(), newAccessRequestId, requesterUserBean);
        // insert equip
        nocAccessRequestEquipService.createEquipForAccessRequest(nocAccessRequestBean.getRequestEquipBeanList(), newAccessRequestId, requesterUserBean);

        // insert hist
        nocOperationHistService.createAccessRequestOptHistStatusChange(
                newAccessRequestId, null, nocAccessRequestEntity.getStatus(), requesterUserBean.getUserId());

        return newAccessRequestId;
    }

    public void sendAccessRequestCfmEmail(Integer newAccessRequestId) throws MessagingException, AccessRequestNotFoundException{
        NocAccessRequestBean nocAccessRequestBean = getAccessRequestBasicInfoByRequestId(newAccessRequestId);
        if(nocAccessRequestBean==null){
            throw new AccessRequestNotFoundException();
        }else if(nocAccessRequestBean.getRequester()==null){
            throw new AccessRequestNotFoundException("Null access requester.");
        }else if(nocAccessRequestBean.getVisitDateFrom()==null){
            throw new AccessRequestNotFoundException("Null visit start time.");
        }else if(nocAccessRequestBean.getVisitDateTo()==null){
            throw new AccessRequestNotFoundException("Null visit end time.");
        }

        String requestorName = nocAccessRequestBean.getRequester().getName();
        String requestorUserId = nocAccessRequestBean.getRequester().getUserId().toString();
        String requestorCompany = nocAccessRequestBean.getRequesterCompany().getName();
        String ticketNum = nocAccessRequestBean.getHashedRequestId()==null ?
                null : String.format("%07d", nocAccessRequestBean.getHashedRequestId());
        String visitLoc = nocAccessRequestBean.getVisitLocation();
        String visitDate = nocAccessRequestBean.getVisitDateFrom().toLocalDate().toString();
        String visitTime = String.format( "%s - %s",
                nocAccessRequestBean.getVisitDateFrom().toLocalTime().toString(),
                nocAccessRequestBean.getVisitDateTo().toLocalTime().toString() );
        String visitorCount = nocAccessRequestBean.getVisitorCount().toString();

        // try send email
        String emailErrorMsg = "";
        try {
            String recipient = nocAccessRequestBean.getRequester().getEmail();
            String recipientName = nocAccessRequestBean.getRequester().getName();

            Map<String, Object> dataMap = new HashMap<>();
            dataMap.put(NocEmailBean.ACCESS_REQUEST_CFM_CUST_EMAIL.TICKET_NUM, ticketNum);
            dataMap.put(NocEmailBean.ACCESS_REQUEST_CFM_CUST_EMAIL.REQUESTOR_NAME, requestorName);
            dataMap.put(NocEmailBean.ACCESS_REQUEST_CFM_CUST_EMAIL.VISIT_LOC, visitLoc);
            dataMap.put(NocEmailBean.ACCESS_REQUEST_CFM_CUST_EMAIL.VISIT_DATE, visitDate);
            dataMap.put(NocEmailBean.ACCESS_REQUEST_CFM_CUST_EMAIL.VISIT_TIME, visitTime);
            dataMap.put(NocEmailBean.ACCESS_REQUEST_CFM_CUST_EMAIL.VISITOR_COUNT, visitorCount);
            dataMap.put(NocEmailBean.EMAIL_BASIC_RECIPIENT_NAME, recipientName);

            nocEmailService.send(NocEmailBean.ACCESS_REQUEST_CFM_CUST_EMAIL.TEMPLATE_ID, recipient, dataMap);
        } catch (MessagingException e){
            LOG.error(e.getMessage(), e);
            emailErrorMsg += "Fail to send confirmation email to customer. ";
        }
        try {
            String recipient = nocConfigParamService.getString(NocConfigParamEntity.VISIT_LOC_EMAIL.CONFIG_GROUP, visitLoc);
            if(StringUtils.isEmpty(recipient)){
                // backup email if visit location email not found
                recipient = nocConfigParamService.getString(NocConfigParamEntity.NFM.CONFIG_GROUP, NocConfigParamEntity.NFM.CONFIG_KEY_EMAIL);
            }

            Map<String, Object> dataMap = new HashMap<>();
            dataMap.put(NocEmailBean.ACCESS_REQUEST_CFM_NFM_EMAIL.TICKET_NUM, ticketNum);
            dataMap.put(NocEmailBean.ACCESS_REQUEST_CFM_NFM_EMAIL.REQUESTOR_NAME, requestorName);
            dataMap.put(NocEmailBean.ACCESS_REQUEST_CFM_NFM_EMAIL.REQUESTOR_USER_ID, requestorUserId);
            dataMap.put(NocEmailBean.ACCESS_REQUEST_CFM_NFM_EMAIL.REQUESTOR_COMPANY, requestorCompany);
            dataMap.put(NocEmailBean.ACCESS_REQUEST_CFM_NFM_EMAIL.VISIT_LOC, visitLoc);
            dataMap.put(NocEmailBean.ACCESS_REQUEST_CFM_NFM_EMAIL.VISIT_DATE, visitDate);
            dataMap.put(NocEmailBean.ACCESS_REQUEST_CFM_NFM_EMAIL.VISIT_TIME, visitTime);
            dataMap.put(NocEmailBean.ACCESS_REQUEST_CFM_NFM_EMAIL.VISITOR_COUNT, visitorCount);

            nocEmailService.send(NocEmailBean.ACCESS_REQUEST_CFM_NFM_EMAIL.TEMPLATE_ID, recipient, dataMap);
        } catch (MessagingException e){
            LOG.error(e.getMessage(), e);
            emailErrorMsg += "Fail to send notification email to NFM. ";
        }
        if(! StringUtils.isEmpty(emailErrorMsg)){
            throw new MessagingException(emailErrorMsg);
        }
    }

    @Override
    public NocAccessRequestBean getAccessRequestByRequestId(Integer accessRequestId) throws AuthorityNotFoundException {
        if(accessRequestId==null){
            LOG.warn("Null request ID");
            return null;
        }

        // get request basic info
        NocAccessRequestBean nocAccessRequestBean = getAccessRequestBasicInfoByRequestId(accessRequestId);

        // populate visitors
        List<NocAccessRequestVisitorBean> visitorBeanList = nocAccessRequestVisitorService.getVisitorListByAccessRequestId(accessRequestId);
        nocAccessRequestBean.setRequestVisitorBeanList(visitorBeanList);

        // populate equipments
        List<NocAccessRequestEquipBean> equipBeanList = nocAccessRequestEquipService.getEquipListByAccessRequestId(accessRequestId);
        nocAccessRequestBean.setRequestEquipBeanList(equipBeanList);

        return nocAccessRequestBean;
    }

    @Override
    public NocAccessRequestBean getAccessRequestBasicInfoByRequestId(Integer accessRequestId)
            throws AuthorityNotFoundException{
        // determine company id restriction
        Integer companyId = nocUserService.getCompanyIdRestriction();
        // determine user id restriction
        Integer userId = nocUserService.getUserIdRestriction();

        // get data
        NocAccessRequestEntity nocAccessRequestEntity = nocAccessRequestMapper.getAccessRequest(accessRequestId, companyId, userId);
        if(nocAccessRequestEntity==null){
            LOG.warn( String.format("Access request ID %s not found. (companyId=%d, userId=%d)", accessRequestId, companyId, userId) );
            return null;
        }

        // populate request
        NocAccessRequestBean nocAccessRequestBean = new NocAccessRequestBean();
        nocAccessRequestBeanPopulator.populate(nocAccessRequestEntity, nocAccessRequestBean);

        // populate requester info
        NocUserBean requester = new NocUserBean();
        nocUserBeanPopulator.populate(nocAccessRequestEntity, requester);
        nocAccessRequestBean.setRequester(requester);
        NocCompanyBean requesterCompany = new NocCompanyBean();
        nocCompanyBeanPopulator.populate(nocAccessRequestEntity, requesterCompany);
        nocAccessRequestBean.setRequesterCompany(requesterCompany);

        return nocAccessRequestBean;
    }

    @Override
    public LinkedList<String> getAccessRequestStatusList() {
        LinkedList<String> result = new LinkedList<>();
        result.add(NocAccessRequestEntity.STATUS.APPROVED);
        result.add(NocAccessRequestEntity.STATUS.COMPLETED);
        return result;
    }

    // inventory part should be implemented properly in later phase
    @Override
    public List<NocAccessRequestLocBean> getAccessRequestLocList() {
        List<NocAccessRequestLocBean> result = new LinkedList<>();

        NocAccessRequestLocBean jbyBean = new NocAccessRequestLocBean();
        jbyBean.setLocId(NocAccessRequestEntity.LOC.JBY);
        jbyBean.setName(NocAccessRequestEntity.LOC.JBY_NAME);
        result.add(jbyBean);

        NocAccessRequestLocBean lkt2fBean = new NocAccessRequestLocBean();
        lkt2fBean.setLocId(NocAccessRequestEntity.LOC.LKT_2F);
        lkt2fBean.setName(NocAccessRequestEntity.LOC.LKT_2F_NAME);
        result.add(lkt2fBean);

        NocAccessRequestLocBean lkt5fBean = new NocAccessRequestLocBean();
        lkt5fBean.setLocId(NocAccessRequestEntity.LOC.LKT_5F);
        lkt5fBean.setName(NocAccessRequestEntity.LOC.LKT_5F_NAME);
        result.add(lkt5fBean);

        NocAccessRequestLocBean vtaBean = new NocAccessRequestLocBean();
        vtaBean.setLocId(NocAccessRequestEntity.LOC.VTA);
        vtaBean.setName(NocAccessRequestEntity.LOC.VTA_NAME);
        result.add(vtaBean);

        NocAccessRequestLocBean mcx3fBean = new NocAccessRequestLocBean();
        mcx3fBean.setLocId(NocAccessRequestEntity.LOC.MCX_3F);
        mcx3fBean.setName(NocAccessRequestEntity.LOC.MCX_3F_NAME);
        result.add(mcx3fBean);

        NocAccessRequestLocBean mcx6fBean = new NocAccessRequestLocBean();
        mcx6fBean.setLocId(NocAccessRequestEntity.LOC.MCX_6F);
        mcx6fBean.setName(NocAccessRequestEntity.LOC.MCX_6F_NAME);
        result.add(mcx6fBean);

        NocAccessRequestLocBean iacBean = new NocAccessRequestLocBean();
        iacBean.setLocId(NocAccessRequestEntity.LOC.IAC);
        iacBean.setName(NocAccessRequestEntity.LOC.IAC_NAME);
        result.add(iacBean);

        result.sort(Comparator.comparing(NocAccessRequestLocBean::getName));
        return result;
    }

    @Override
    public Page<NocAccessRequestBean> searchAccessRequest (
            Pageable pageable,
            Integer accessRequestId, String companyName, String status,
            String visitLocation, LocalDate visitDateFrom, LocalDate visitDateTo) throws AuthorityNotFoundException {
        long offset = pageable.getOffset();
        int pageSize = pageable.getPageSize();

        // determine company id restriction
        Integer companyId = nocUserService.getCompanyIdRestriction();

        // determine user id restriction
        Integer userId = nocUserService.getUserIdRestriction();


        LOG.info( String.format(
                "Searching company with {companyId: %s, userId: %s, " +
                        "accessRequestId: %s, status: %s, visitLocation: %s, visitDateFrom: %s, visitDateTo: %s}",
                companyId, userId,
                accessRequestId, status, visitLocation, visitDateFrom, visitDateTo) );

        // get request total count
        Integer totalCount = nocAccessRequestMapper.countSearchAccessRequest(
                companyId, userId,
                accessRequestId, companyName, status, visitLocation, visitDateFrom, visitDateTo);
        // get request content
        List<NocAccessRequestEntity> entityList = nocAccessRequestMapper.searchAccessRequest(
                offset, pageSize, companyId, userId,
                accessRequestId, companyName, status, visitLocation, visitDateFrom, visitDateTo);
        if( CollectionUtils.isEmpty(entityList) ){
            return new PageImpl<>(new LinkedList<>(), pageable, totalCount);
        }

        List<NocAccessRequestBean> beanList = new LinkedList<>();
        for (NocAccessRequestEntity entity : entityList){
            NocAccessRequestBean bean = new NocAccessRequestBean();
            nocAccessRequestBeanPopulator.populate(entity, bean);

            NocCompanyBean companyBean = new NocCompanyBean();
            nocCompanyBeanPopulator.populate(entity, companyBean);
            bean.setRequesterCompany(companyBean);

            beanList.add(bean);
        }


        return new PageImpl<>(beanList, pageable, totalCount);
    }


    private int updateStatusAccessRequestToComplete(Integer accessRequestId, String oldStatus, Integer modifyby) {
        // update status
        int counter = nocAccessRequestMapper.updateStatus(accessRequestId, NocAccessRequestEntity.STATUS.COMPLETED, modifyby);

        // insert hist
        nocOperationHistService.createAccessRequestOptHistStatusChange(
                accessRequestId, oldStatus, NocAccessRequestEntity.STATUS.COMPLETED, modifyby);

        return counter;
    }

    @Override
    @Transactional
    public void completeExpiredAccessRequest() {
        // get modifyby
        Integer modifyby = NocUserEntity.SYSTEM.USER_ID;

        // get all expired but non-complete request
        List<NocAccessRequestEntity> entityList = nocAccessRequestMapper.getExpiredAccessRequest(NocAccessRequestEntity.STATUS.COMPLETED);
        if(CollectionUtils.isEmpty(entityList)){
            LOG.info("Auto-completed ZERO access request.");
            return;
        }

        // update each to complete
        int counter = 0;
        for(NocAccessRequestEntity entity : entityList){
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
        boolean isInternalUser = nocUserService.isInternalUser();
        boolean isAdminUser = nocUserService.isAdminUser();
        return ! isInternalUser && ! isAdminUser;
    }

    @Override
    public NocAccessRequestVisitorBean getSelfVisitVisitorBean() {
        return null;
    }

    @Override
    public void checkRequestNowEligibleForCheckInOut(NocAccessRequestBean nocAccessRequestBean) throws InvalidWorkflowException, AccessRequestNotFoundException {
        final LocalDateTime NOW = LocalDateTime.now();

        // check status
        String status = nocAccessRequestBean.getStatus();
        if( ! StringUtils.equals(status, NocAccessRequestEntity.STATUS.APPROVED) ){
            throw new InvalidWorkflowException("Please seek access request approval.");
        }

        // check visit from
        LocalDateTime visitDateFrom = nocAccessRequestBean.getVisitDateFrom();
        if ( visitDateFrom==null ) {
            throw new InvalidWorkflowException("Empty visit start time.");
        } else if( ! NOW.isAfter(visitDateFrom) ){
            throw new InvalidWorkflowException("Request visit time not yet started.");
        }

        // check visit to
        LocalDateTime visitDateTo = nocAccessRequestBean.getVisitDateTo();
        if ( visitDateTo==null ) {
            throw new InvalidWorkflowException("Empty visit end time.");
        } else if( ! NOW.isBefore(visitDateTo) ){
            throw new InvalidWorkflowException("Request visit time ended.");
        }
    }

    @Override
    public NocAccessRequestBean getAccessRequestByVisitorId(Integer visitorAccessId) throws AuthorityNotFoundException {
        NocAccessRequestEntity nocAccessRequestEntity = nocAccessRequestMapper.getAccessRequestByVisitorAccessId(visitorAccessId);
        if( nocAccessRequestEntity==null ){
            throw new AccessRequestNotFoundException("Request of visitor " + visitorAccessId + " not found.");
        }

        NocAccessRequestBean nocAccessRequestBean = new NocAccessRequestBean();
        nocAccessRequestBeanPopulator.populate(nocAccessRequestEntity, nocAccessRequestBean);
        return nocAccessRequestBean;
    }

}
