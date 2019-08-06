package com.hkt.btu.sd.facade.impl;


import com.hkt.btu.common.core.exception.UserNotFoundException;
import com.hkt.btu.common.facade.data.PageData;
import com.hkt.btu.sd.core.exception.*;
import com.hkt.btu.sd.core.service.*;
import com.hkt.btu.sd.core.service.bean.*;
import com.hkt.btu.sd.core.service.populator.SdAccessRequestBeanPopulator;
import com.hkt.btu.sd.facade.SdAccessRequestFacade;
import com.hkt.btu.sd.facade.SdUserFacade;
import com.hkt.btu.sd.facade.data.*;
import com.hkt.btu.sd.facade.populator.*;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import javax.annotation.Resource;
import javax.mail.MessagingException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.LinkedList;
import java.util.List;
import java.util.TreeMap;

public class SdAccessRequestFacadeImpl implements SdAccessRequestFacade {
    private static final Logger LOG = LogManager.getLogger(SdAccessRequestFacadeImpl.class);

    @Resource(name = "userFacade")
    SdUserFacade sdUserFacade;

    @Resource(name = "accessRequestService")
    SdAccessRequestService sdAccessRequestService;
    @Resource(name = "accessRequestVisitorService")
    SdAccessRequestVisitorService sdAccessRequestVisitorService;
    @Resource(name = "accessRequestEquipService")
    SdAccessRequestEquipService sdAccessRequestEquipService;
    @Resource(name = "operationHistService")
    SdOperationHistService sdOperationHistService;
    @Resource(name = "accessRequestHashService")
    SdAccessRequestHashService sdAccessRequestHashService;
    @Resource(name = "userService")
    SdUserService sdUserService;

    @Resource(name = "accessRequestBeanPopulator")
    SdAccessRequestBeanPopulator sdAccessRequestBeanPopulator;

    @Resource(name = "accessRequestDataPopulator")
    SdAccessRequestDataPopulator sdAccessRequestDataPopulator;
    @Resource(name = "accessRequestVisitorDataPopulator")
    SdAccessRequestVisitorDataPopulator sdAccessRequestVisitorDataPopulator;
    @Resource(name = "accessRequestEquipDataPopulator")
    SdAccessRequestEquipDataPopulator sdAccessRequestEquipDataPopulator;
    @Resource(name = "accessRequestLocDataPopulator")
    SdAccessRequestLocDataPopulator sdAccessRequestLocDataPopulator;

    @Resource(name = "operationHistDataPopulator")
    SdOperationHistDataPopulator sdOperationHistDataPopulator;

    @Override
    public boolean canOnlySubmitSelfVisit() {
        return sdAccessRequestService.canOnlySubmitSelfVisit();
    }

    @Override
    public void getPrefillAccessFormData(CreateAccessRequestFormData createAccessData) {
        int addVisitorNum = 5;
        int addEquitNum = 5;

        // fill in requestor info
        SdUserData currentUser = sdUserFacade.getCurrentUser();
        createAccessData.setRequesterName(currentUser.getName());
        createAccessData.setCompanyName(currentUser.getCompanyName());
        createAccessData.setMobile(currentUser.getMobile());
        createAccessData.setEmail(currentUser.getEmail());

        // fill in default time (office hours)
        createAccessData.setVisitDate(LocalDate.now());
        createAccessData.setVisitTimeFrom(LocalTime.of(9, 0));
        createAccessData.setVisitTimeTo(LocalTime.of(18, 0));

        // add blank rows for visitor
        List<SdAccessRequestVisitorData> sdAccessRequestVisitorDataList = new LinkedList<>();
        boolean canOnlySubmitSelfVisit = sdAccessRequestService.canOnlySubmitSelfVisit();
        if( canOnlySubmitSelfVisit ) {
            SdAccessRequestVisitorData visitorData = new SdAccessRequestVisitorData();
            SdAccessRequestVisitorBean visitorBean = sdAccessRequestVisitorService.getSelfVisitVisitorBean();
            sdAccessRequestVisitorDataPopulator.populate(visitorBean, visitorData);
            sdAccessRequestVisitorDataPopulator.populateSensitiveData(visitorBean, visitorData);

            sdAccessRequestVisitorDataList.add(visitorData);
        } else {
            // add blank rows for visitor
            for (int i = 0; i < addVisitorNum; i++) {
                sdAccessRequestVisitorDataList.add(new SdAccessRequestVisitorData());
            }
        }
        createAccessData.setVisitorDataList(sdAccessRequestVisitorDataList);

        // add blank rows for equipment
        List<SdAccessRequestEquipData> equipmentDataList = createAccessData.getEquipDataList()==null ?
                new LinkedList<>() : createAccessData.getEquipDataList();
        for (int i = 0; i < addEquitNum; i++) {
            equipmentDataList.add(new SdAccessRequestEquipData());
        }
        createAccessData.setEquipDataList(equipmentDataList);
    }

    @Override
    public void copyAccessFormData(SdAccessRequestData sdAccessRequestData, CreateAccessRequestFormData createAccessRequestFormData) {
        createAccessRequestFormData.setVisitReason(sdAccessRequestData.getVisitReason());
        createAccessRequestFormData.setVisitLocation(sdAccessRequestData.getVisitLocation());
        createAccessRequestFormData.setVisitRackNum(sdAccessRequestData.getVisitRackNum());

        boolean canOnlySubmitSelfVisit = sdAccessRequestService.canOnlySubmitSelfVisit();
        if( canOnlySubmitSelfVisit ){
            SdAccessRequestVisitorData visitorData = new SdAccessRequestVisitorData();
            SdAccessRequestVisitorBean visitorBean = sdAccessRequestVisitorService.getSelfVisitVisitorBean();
            sdAccessRequestVisitorDataPopulator.populate(visitorBean, visitorData);
            sdAccessRequestVisitorDataPopulator.populateSensitiveData(visitorBean, visitorData);

            List<SdAccessRequestVisitorData> sdAccessRequestVisitorDataList = new LinkedList<>();
            sdAccessRequestVisitorDataList.add(visitorData);
            createAccessRequestFormData.setVisitorDataList(sdAccessRequestVisitorDataList);
        } else {
            createAccessRequestFormData.setVisitorDataList(sdAccessRequestData.getVisitorDataList());
        }

        // set visit date empty (ux requirement, for preventing consecutive copying)
        createAccessRequestFormData.setVisitDate(null);
    }

    @Override
    public SdAccessRequestData getAccessRequestByRequestId(Integer hashedRequestId) {
        Integer accessRequestId = getAccessRequestId(hashedRequestId);
        if(accessRequestId==null){
            LOG.warn("Access request id not found for hashed ID " + hashedRequestId + ".");
            return null;
        }

        SdAccessRequestBean sdAccessRequestBean = sdAccessRequestService.getAccessRequestByRequestId(accessRequestId);
        if(sdAccessRequestBean==null){
            return null;
        }

        // short-long name map
//        HashMap<String, SdAccessRequestLocData> locDataHashMap = getAccessRequestLocMap();

        // populate
        SdAccessRequestData requestData = new SdAccessRequestData();
        sdAccessRequestDataPopulator.populate(sdAccessRequestBean, requestData);
        sdAccessRequestDataPopulator.populateSensitiveData(sdAccessRequestBean, requestData);

        // populate visitor
        List<SdAccessRequestVisitorBean> visitorBeanList = sdAccessRequestBean.getRequestVisitorBeanList();
        List<SdAccessRequestVisitorData> visitorDataList = new LinkedList<>();
        if(CollectionUtils.isNotEmpty(visitorBeanList)){
            for(SdAccessRequestVisitorBean visitorBean : visitorBeanList){
                SdAccessRequestVisitorData visitorData = new SdAccessRequestVisitorData();
                sdAccessRequestVisitorDataPopulator.populate(visitorBean, visitorData);
                sdAccessRequestVisitorDataPopulator.populateSensitiveData(visitorBean, visitorData);
                visitorDataList.add(visitorData);
            }
        }
        requestData.setVisitorDataList(visitorDataList);

        // populate equipment
        List<SdAccessRequestEquipBean> equipBeanList = sdAccessRequestBean.getRequestEquipBeanList();
        List<SdAccessRequestEquipData> equipDataList = new LinkedList<>();
        if(CollectionUtils.isNotEmpty(equipBeanList)){
            for(SdAccessRequestEquipBean equipBean : equipBeanList){
                SdAccessRequestEquipData equipData = new SdAccessRequestEquipData();
                sdAccessRequestEquipDataPopulator.populate(equipBean, equipData);
                equipDataList.add(equipData);
            }
        }
        requestData.setEquipDataList(equipDataList);

        return requestData;
    }

    @Override
    public SdAccessRequestData getAccessRequestBasicInfoByRequestId(Integer hashedRequestId) {
        Integer accessRequestId = getAccessRequestId(hashedRequestId);
        if(accessRequestId==null){
            LOG.warn("Access request id not found for hashed ID " + hashedRequestId + ".");
            return null;
        }

        SdAccessRequestBean sdAccessRequestBean;
        try {
            sdAccessRequestBean = sdAccessRequestService.getAccessRequestBasicInfoByRequestId(accessRequestId);
            if (sdAccessRequestBean == null) {
                return null;
            }
        } catch (AuthorityNotFoundException e){
            LOG.warn(e.getMessage());
            return null;
        }

        // populate
        SdAccessRequestData requestData = new SdAccessRequestData();
        sdAccessRequestDataPopulator.populate(sdAccessRequestBean, requestData);
        sdAccessRequestDataPopulator.populateSensitiveData(sdAccessRequestBean, requestData);
        return requestData;
    }

    @Override
    public List<SdAccessRequestVisitorData> getAccessRequestVisitorListByRequestId(Integer hashedRequestId) {
        Integer accessRequestId = getAccessRequestId(hashedRequestId);
        if(accessRequestId==null){
            LOG.warn("Access request id not found for hashed ID " + hashedRequestId + ".");
            return null;
        }

        // decide check in/out available for request
        boolean isRequestNowEligibleForCheckInOut;
        try {
            SdAccessRequestBean accessRequestBean = sdAccessRequestService.getAccessRequestBasicInfoByRequestId(accessRequestId);
            sdAccessRequestService.checkRequestNowEligibleForCheckInOut(accessRequestBean);
            isRequestNowEligibleForCheckInOut = true;
        } catch (InvalidWorkflowException | AccessRequestNotFoundException e){
            isRequestNowEligibleForCheckInOut = false;
        }
        boolean isInternalUser = sdUserService.isInternalUser();
        boolean canCheckInOut = isRequestNowEligibleForCheckInOut && isInternalUser;

        // get all visitors of request
        List<SdAccessRequestVisitorBean> beanList;
        try {
            beanList = sdAccessRequestVisitorService.getVisitorListByAccessRequestId(accessRequestId);
        } catch (AuthorityNotFoundException e){
            LOG.warn(e.getMessage());
            return null;
        }

        // populate
        List<SdAccessRequestVisitorData> dataList = new LinkedList<>();
        for (SdAccessRequestVisitorBean bean : beanList){
            SdAccessRequestVisitorData data = new SdAccessRequestVisitorData();
            sdAccessRequestVisitorDataPopulator.populate(bean, data);
            sdAccessRequestVisitorDataPopulator.populateSensitiveData(bean, data);
            data.setCanCheckInOut(canCheckInOut);
            dataList.add(data);
        }

        return dataList;
    }

    @SuppressWarnings("Duplicates")
    @Override
    public List<SdAccessRequestEquipData> getAccessRequestEquipListByRequestId(Integer hashedRequestId) {
        Integer accessRequestId = getAccessRequestId(hashedRequestId);
        if(accessRequestId==null){
            LOG.warn("Access request id not found for hashed ID " + hashedRequestId + ".");
            return null;
        }

        List<SdAccessRequestEquipBean> beanList;
        try {
            beanList = sdAccessRequestEquipService.getEquipListByAccessRequestId(accessRequestId);
        } catch (AuthorityNotFoundException e){
            LOG.warn(e.getMessage());
            return null;
        }

        List<SdAccessRequestEquipData> dataList = new LinkedList<>();
        for (SdAccessRequestEquipBean bean : beanList){
            SdAccessRequestEquipData data = new SdAccessRequestEquipData();
            sdAccessRequestEquipDataPopulator.populate(bean, data);
            dataList.add(data);
        }

        return dataList;
    }

    @Override
    public LinkedList<String> getAccessRequestStatusList() {
        return sdAccessRequestService.getAccessRequestStatusList();
    }

    @Override
    public TreeMap<String, SdAccessRequestLocData> getAccessRequestLocMap() {
        List<SdAccessRequestLocBean> beanList = sdAccessRequestService.getAccessRequestLocList();
        if(CollectionUtils.isEmpty(beanList)){
            return new TreeMap<>();
        }

        TreeMap<String, SdAccessRequestLocData> result = new TreeMap<>();
        for (SdAccessRequestLocBean bean : beanList){
            SdAccessRequestLocData data = new SdAccessRequestLocData();
            sdAccessRequestLocDataPopulator.populate(bean, data);
            result.put(data.getLocId(), data);
        }

        return result;
    }

    @Override
    public PageData<SdAccessRequestData> searchAccessRequest(Pageable pageable, Integer hashedRequestId,
                                                              String companyName, String status, String visitLocation,
                                                              LocalDate visitDateFrom, LocalDate visitDateTo) {
        Integer accessRequestId = getAccessRequestId(hashedRequestId);

        companyName = StringUtils.trimToNull(companyName);
        status = StringUtils.trimToNull(status);
        visitLocation = StringUtils.trimToNull(visitLocation);

        Page<SdAccessRequestBean> pageBean = sdAccessRequestService.searchAccessRequest(pageable,
                accessRequestId, companyName, status, visitLocation, visitDateFrom, visitDateTo);

        // populate content
        List<SdAccessRequestBean> beanList = pageBean.getContent();
        List<SdAccessRequestData> dataList = new LinkedList<>();
        if(!CollectionUtils.isEmpty(beanList)){
            for(SdAccessRequestBean bean : beanList){
                SdAccessRequestData data = new SdAccessRequestData();
                sdAccessRequestDataPopulator.populate(bean, data);
                dataList.add(data);
            }
        }

        return new PageData<>(dataList, pageBean.getPageable(), pageBean.getTotalElements());
    }

    @Override
    public PageData<SdAccessRequestVisitorData> searchAccessRequestVisitor(
            Pageable pageable,
            String visitorName, String companyName,
            String visitLocation, LocalDate visitDateFrom, LocalDate visitDateTo) {

        visitorName = StringUtils.trimToNull(visitorName);
        companyName = StringUtils.trimToNull(companyName);
        visitLocation = StringUtils.trimToNull(visitLocation);

        Page<SdAccessRequestVisitorBean> pageBean = sdAccessRequestVisitorService.searchAccessRequest( pageable,
                visitorName, companyName, visitLocation, visitDateFrom, visitDateTo );

        // populate content
        List<SdAccessRequestVisitorBean> beanList = pageBean.getContent();
        List<SdAccessRequestVisitorData> dataList = new LinkedList<>();
        if(!CollectionUtils.isEmpty(beanList)){
            for(SdAccessRequestVisitorBean bean : beanList){
                SdAccessRequestVisitorData data = new SdAccessRequestVisitorData();
                sdAccessRequestVisitorDataPopulator.populate(bean, data);
                dataList.add(data);
            }
        }

        return new PageData<>(dataList, pageBean.getPageable(), pageBean.getTotalElements());
    }

    @Override
    public List<SdAccessRequestData> getPendingAccessRequest(String targetLocation) {
        Pageable pageable = PageRequest.of(0, 500);
        String status = "A";

        Page<SdAccessRequestData> pageData = searchAccessRequest(pageable,
                null, null, status, targetLocation, null, null);
        return pageData.getContent();
    }

    @Override
    public List<SdAccessRequestData> getRecentCompletedAccessRequest(String targetLocation) {
        Pageable pageable = PageRequest.of(0, 500);
        String status = "C";
        LocalDate visitDateTo = LocalDate.now();
        LocalDate visitDateFrom = visitDateTo.minusDays(30);

        Page<SdAccessRequestData> pageData = searchAccessRequest(pageable,
                null, null, status, targetLocation, visitDateFrom, visitDateTo);
        return pageData.getContent();
    }

    @Override
    public CreateResultData createAccessRequest(CreateAccessRequestFormData createAccessFormData) {
        if(createAccessFormData==null){
            LOG.warn("Null accessFormData.");
            return CreateResultData.of("Null input!");
        }

        // convert input data to bean
        SdAccessRequestBean accessRequestBean = new SdAccessRequestBean();
        // TODO: 2019/7/17  Change Model for Populator
        //sdAccessRequestBeanPopulator.populate(createAccessFormData, accessRequestBean);

        // create access request
        Integer newAccessRequestId;
        try {
            newAccessRequestId = sdAccessRequestService.createAccessRequest(accessRequestBean);
        }catch (UserNotFoundException e){
            LOG.warn(e.getMessage());
            return CreateResultData.of("Requester user not found!");
        }catch (CompanyNotFoundException e){
            LOG.warn(e.getMessage());
            return CreateResultData.of("Requester company not found!");
        }catch (InvalidInputException | LocationNotFoundException e){
            LOG.warn(e.getMessage());
            return CreateResultData.of(e.getMessage());
        }

        // check new id
        Integer hashedRequestId = sdAccessRequestHashService.getHashedId(newAccessRequestId);
        if (hashedRequestId==null) {
            LOG.warn("Request created but hashed id not found! (newAccessRequestId: " + newAccessRequestId + ")");
            return CreateResultData.of("Request created but hashed id not found!");
        }

        // send confirmation email
        try {
            sdAccessRequestService.sendAccessRequestCfmEmail(newAccessRequestId);
        } catch (MessagingException | AccessRequestNotFoundException e){
            LOG.error(e.getMessage());
            return CreateResultData.of(e.getMessage());
        }

        return CreateResultData.of(hashedRequestId);
    }


    @Override
    public String checkinAccessRequestVisitor(Integer visitorAccessId, String visitorCardNum) {
        if(visitorAccessId==null){
            return "Null visitor access ID.";
        }else if(StringUtils.isEmpty(visitorCardNum)){
            return "Empty visitor card number.";
        }

        try {
            sdAccessRequestVisitorService.checkin(visitorAccessId, visitorCardNum);
            return null;
        }catch (AuthorityNotFoundException | AccessRequestVisitorNotFoundException |
                InvalidWorkflowException | AccessRequestNotFoundException e){
            return e.getMessage();
        }
    }

    @Override
    public String checkoutAccessRequestVisitor(Integer visitorAccessId) {
        if(visitorAccessId==null){
            return "Null visitor access ID.";
        }

        try {
            sdAccessRequestVisitorService.checkout(visitorAccessId);
            return null;
        }catch (AuthorityNotFoundException | AccessRequestVisitorNotFoundException |
                InvalidWorkflowException | AccessRequestNotFoundException e){
            return e.getMessage();
        }
    }

    @SuppressWarnings("Duplicates")
    @Override
    public List<SdOperationHistData> getOperationHistList(Integer hashedRequestId) {
        Integer accessRequestId = getAccessRequestId(hashedRequestId);
        if(accessRequestId==null){
            LOG.warn("Access request id not found for hashed ID " + hashedRequestId + ".");
            return null;
        }

        List<SdOperationHistBean> beanList;
        try {
            beanList = sdOperationHistService.getAccessRequestOptHistList(accessRequestId);
        } catch (AuthorityNotFoundException e){
            LOG.warn(e.getMessage());
            return null;
        }
        if(CollectionUtils.isEmpty(beanList)){
            return new LinkedList<>();
        }

        List<SdOperationHistData> dataList = new LinkedList<>();
        for(SdOperationHistBean bean : beanList){
            SdOperationHistData data = new SdOperationHistData();
            sdOperationHistDataPopulator.populate(bean, data);
            dataList.add(data);
        }
        return dataList;
    }

    private Integer getAccessRequestId(Integer hashedRequestId){
        if(hashedRequestId==null){
            return null;
        }
        return sdAccessRequestHashService.getAccessRequestId(hashedRequestId);
    }
}
