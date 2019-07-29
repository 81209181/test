package com.hkt.btu.noc.facade.impl;


import com.hkt.btu.common.core.exception.UserNotFoundException;
import com.hkt.btu.common.facade.data.PageData;
import com.hkt.btu.noc.core.exception.*;
import com.hkt.btu.noc.core.service.*;
import com.hkt.btu.noc.core.service.bean.*;
import com.hkt.btu.noc.core.service.populator.NocAccessRequestBeanPopulator;
import com.hkt.btu.noc.facade.NocAccessRequestFacade;
import com.hkt.btu.noc.facade.NocUserFacade;
import com.hkt.btu.noc.facade.data.*;
import com.hkt.btu.noc.facade.populator.*;
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

public class NocAccessRequestFacadeImpl implements NocAccessRequestFacade {
    private static final Logger LOG = LogManager.getLogger(NocAccessRequestFacadeImpl.class);

    @Resource(name = "userFacade")
    NocUserFacade nocUserFacade;

    @Resource(name = "accessRequestService")
    NocAccessRequestService nocAccessRequestService;
    @Resource(name = "accessRequestVisitorService")
    NocAccessRequestVisitorService nocAccessRequestVisitorService;
    @Resource(name = "accessRequestEquipService")
    NocAccessRequestEquipService nocAccessRequestEquipService;
    @Resource(name = "operationHistService")
    NocOperationHistService nocOperationHistService;
    @Resource(name = "accessRequestHashService")
    NocAccessRequestHashService nocAccessRequestHashService;
    @Resource(name = "userService")
    NocUserService nocUserService;

    @Resource(name = "accessRequestBeanPopulator")
    NocAccessRequestBeanPopulator nocAccessRequestBeanPopulator;

    @Resource(name = "accessRequestDataPopulator")
    NocAccessRequestDataPopulator nocAccessRequestDataPopulator;
    @Resource(name = "accessRequestVisitorDataPopulator")
    NocAccessRequestVisitorDataPopulator nocAccessRequestVisitorDataPopulator;
    @Resource(name = "accessRequestEquipDataPopulator")
    NocAccessRequestEquipDataPopulator nocAccessRequestEquipDataPopulator;
    @Resource(name = "accessRequestLocDataPopulator")
    NocAccessRequestLocDataPopulator nocAccessRequestLocDataPopulator;

    @Resource(name = "operationHistDataPopulator")
    NocOperationHistDataPopulator nocOperationHistDataPopulator;

    @Override
    public boolean canOnlySubmitSelfVisit() {
        return nocAccessRequestService.canOnlySubmitSelfVisit();
    }

    @Override
    public void getPrefillAccessFormData(CreateAccessRequestFormData createAccessData) {
        int addVisitorNum = 5;
        int addEquitNum = 5;

        // fill in requestor info
        NocUserData currentUser = nocUserFacade.getCurrentUser();
        createAccessData.setRequesterName(currentUser.getName());
        createAccessData.setCompanyName(currentUser.getCompanyName());
        createAccessData.setMobile(currentUser.getMobile());
        createAccessData.setEmail(currentUser.getEmail());

        // fill in default time (office hours)
        createAccessData.setVisitDate(LocalDate.now());
        createAccessData.setVisitTimeFrom(LocalTime.of(9, 0));
        createAccessData.setVisitTimeTo(LocalTime.of(18, 0));

        // add blank rows for visitor
        List<NocAccessRequestVisitorData> nocAccessRequestVisitorDataList = new LinkedList<>();
        boolean canOnlySubmitSelfVisit = nocAccessRequestService.canOnlySubmitSelfVisit();
        if( canOnlySubmitSelfVisit ) {
            NocAccessRequestVisitorData visitorData = new NocAccessRequestVisitorData();
            NocAccessRequestVisitorBean visitorBean = nocAccessRequestVisitorService.getSelfVisitVisitorBean();
            nocAccessRequestVisitorDataPopulator.populate(visitorBean, visitorData);
            nocAccessRequestVisitorDataPopulator.populateSensitiveData(visitorBean, visitorData);

            nocAccessRequestVisitorDataList.add(visitorData);
        } else {
            // add blank rows for visitor
            for (int i = 0; i < addVisitorNum; i++) {
                nocAccessRequestVisitorDataList.add(new NocAccessRequestVisitorData());
            }
        }
        createAccessData.setVisitorDataList(nocAccessRequestVisitorDataList);

        // add blank rows for equipment
        List<NocAccessRequestEquipData> equipmentDataList = createAccessData.getEquipDataList()==null ?
                new LinkedList<>() : createAccessData.getEquipDataList();
        for (int i = 0; i < addEquitNum; i++) {
            equipmentDataList.add(new NocAccessRequestEquipData());
        }
        createAccessData.setEquipDataList(equipmentDataList);
    }

    @Override
    public void copyAccessFormData(NocAccessRequestData nocAccessRequestData, CreateAccessRequestFormData createAccessRequestFormData) {
        createAccessRequestFormData.setVisitReason(nocAccessRequestData.getVisitReason());
        createAccessRequestFormData.setVisitLocation(nocAccessRequestData.getVisitLocation());
        createAccessRequestFormData.setVisitRackNum(nocAccessRequestData.getVisitRackNum());

        boolean canOnlySubmitSelfVisit = nocAccessRequestService.canOnlySubmitSelfVisit();
        if( canOnlySubmitSelfVisit ){
            NocAccessRequestVisitorData visitorData = new NocAccessRequestVisitorData();
            NocAccessRequestVisitorBean visitorBean = nocAccessRequestVisitorService.getSelfVisitVisitorBean();
            nocAccessRequestVisitorDataPopulator.populate(visitorBean, visitorData);
            nocAccessRequestVisitorDataPopulator.populateSensitiveData(visitorBean, visitorData);

            List<NocAccessRequestVisitorData> nocAccessRequestVisitorDataList = new LinkedList<>();
            nocAccessRequestVisitorDataList.add(visitorData);
            createAccessRequestFormData.setVisitorDataList(nocAccessRequestVisitorDataList);
        } else {
            createAccessRequestFormData.setVisitorDataList(nocAccessRequestData.getVisitorDataList());
        }

        // set visit date empty (ux requirement, for preventing consecutive copying)
        createAccessRequestFormData.setVisitDate(null);
    }

    @Override
    public NocAccessRequestData getAccessRequestByRequestId(Integer hashedRequestId) {
        Integer accessRequestId = getAccessRequestId(hashedRequestId);
        if(accessRequestId==null){
            LOG.warn("Access request id not found for hashed ID " + hashedRequestId + ".");
            return null;
        }

        NocAccessRequestBean nocAccessRequestBean = nocAccessRequestService.getAccessRequestByRequestId(accessRequestId);
        if(nocAccessRequestBean==null){
            return null;
        }

        // short-long name map
//        HashMap<String, NocAccessRequestLocData> locDataHashMap = getAccessRequestLocMap();

        // populate
        NocAccessRequestData requestData = new NocAccessRequestData();
        nocAccessRequestDataPopulator.populate(nocAccessRequestBean, requestData);
        nocAccessRequestDataPopulator.populateSensitiveData(nocAccessRequestBean, requestData);

        // populate visitor
        List<NocAccessRequestVisitorBean> visitorBeanList = nocAccessRequestBean.getRequestVisitorBeanList();
        List<NocAccessRequestVisitorData> visitorDataList = new LinkedList<>();
        if(CollectionUtils.isNotEmpty(visitorBeanList)){
            for(NocAccessRequestVisitorBean visitorBean : visitorBeanList){
                NocAccessRequestVisitorData visitorData = new NocAccessRequestVisitorData();
                nocAccessRequestVisitorDataPopulator.populate(visitorBean, visitorData);
                nocAccessRequestVisitorDataPopulator.populateSensitiveData(visitorBean, visitorData);
                visitorDataList.add(visitorData);
            }
        }
        requestData.setVisitorDataList(visitorDataList);

        // populate equipment
        List<NocAccessRequestEquipBean> equipBeanList = nocAccessRequestBean.getRequestEquipBeanList();
        List<NocAccessRequestEquipData> equipDataList = new LinkedList<>();
        if(CollectionUtils.isNotEmpty(equipBeanList)){
            for(NocAccessRequestEquipBean equipBean : equipBeanList){
                NocAccessRequestEquipData equipData = new NocAccessRequestEquipData();
                nocAccessRequestEquipDataPopulator.populate(equipBean, equipData);
                equipDataList.add(equipData);
            }
        }
        requestData.setEquipDataList(equipDataList);

        return requestData;
    }

    @Override
    public NocAccessRequestData getAccessRequestBasicInfoByRequestId(Integer hashedRequestId) {
        Integer accessRequestId = getAccessRequestId(hashedRequestId);
        if(accessRequestId==null){
            LOG.warn("Access request id not found for hashed ID " + hashedRequestId + ".");
            return null;
        }

        NocAccessRequestBean nocAccessRequestBean;
        try {
            nocAccessRequestBean = nocAccessRequestService.getAccessRequestBasicInfoByRequestId(accessRequestId);
            if (nocAccessRequestBean == null) {
                return null;
            }
        } catch (AuthorityNotFoundException e){
            LOG.warn(e.getMessage());
            return null;
        }

        // populate
        NocAccessRequestData requestData = new NocAccessRequestData();
        nocAccessRequestDataPopulator.populate(nocAccessRequestBean, requestData);
        nocAccessRequestDataPopulator.populateSensitiveData(nocAccessRequestBean, requestData);
        return requestData;
    }

    @Override
    public List<NocAccessRequestVisitorData> getAccessRequestVisitorListByRequestId(Integer hashedRequestId) {
        Integer accessRequestId = getAccessRequestId(hashedRequestId);
        if(accessRequestId==null){
            LOG.warn("Access request id not found for hashed ID " + hashedRequestId + ".");
            return null;
        }

        // decide check in/out available for request
        boolean isRequestNowEligibleForCheckInOut;
        try {
            NocAccessRequestBean accessRequestBean = nocAccessRequestService.getAccessRequestBasicInfoByRequestId(accessRequestId);
            nocAccessRequestService.checkRequestNowEligibleForCheckInOut(accessRequestBean);
            isRequestNowEligibleForCheckInOut = true;
        } catch (InvalidWorkflowException | AccessRequestNotFoundException e){
            isRequestNowEligibleForCheckInOut = false;
        }
        boolean isInternalUser = nocUserService.isInternalUser();
        boolean canCheckInOut = isRequestNowEligibleForCheckInOut && isInternalUser;

        // get all visitors of request
        List<NocAccessRequestVisitorBean> beanList;
        try {
            beanList = nocAccessRequestVisitorService.getVisitorListByAccessRequestId(accessRequestId);
        } catch (AuthorityNotFoundException e){
            LOG.warn(e.getMessage());
            return null;
        }

        // populate
        List<NocAccessRequestVisitorData> dataList = new LinkedList<>();
        for (NocAccessRequestVisitorBean bean : beanList){
            NocAccessRequestVisitorData data = new NocAccessRequestVisitorData();
            nocAccessRequestVisitorDataPopulator.populate(bean, data);
            nocAccessRequestVisitorDataPopulator.populateSensitiveData(bean, data);
            data.setCanCheckInOut(canCheckInOut);
            dataList.add(data);
        }

        return dataList;
    }

    @SuppressWarnings("Duplicates")
    @Override
    public List<NocAccessRequestEquipData> getAccessRequestEquipListByRequestId(Integer hashedRequestId) {
        Integer accessRequestId = getAccessRequestId(hashedRequestId);
        if(accessRequestId==null){
            LOG.warn("Access request id not found for hashed ID " + hashedRequestId + ".");
            return null;
        }

        List<NocAccessRequestEquipBean> beanList;
        try {
            beanList = nocAccessRequestEquipService.getEquipListByAccessRequestId(accessRequestId);
        } catch (AuthorityNotFoundException e){
            LOG.warn(e.getMessage());
            return null;
        }

        List<NocAccessRequestEquipData> dataList = new LinkedList<>();
        for (NocAccessRequestEquipBean bean : beanList){
            NocAccessRequestEquipData data = new NocAccessRequestEquipData();
            nocAccessRequestEquipDataPopulator.populate(bean, data);
            dataList.add(data);
        }

        return dataList;
    }

    @Override
    public LinkedList<String> getAccessRequestStatusList() {
        return nocAccessRequestService.getAccessRequestStatusList();
    }

    @Override
    public TreeMap<String, NocAccessRequestLocData> getAccessRequestLocMap() {
        List<NocAccessRequestLocBean> beanList = nocAccessRequestService.getAccessRequestLocList();
        if(CollectionUtils.isEmpty(beanList)){
            return new TreeMap<>();
        }

        TreeMap<String, NocAccessRequestLocData> result = new TreeMap<>();
        for (NocAccessRequestLocBean bean : beanList){
            NocAccessRequestLocData data = new NocAccessRequestLocData();
            nocAccessRequestLocDataPopulator.populate(bean, data);
            result.put(data.getLocId(), data);
        }

        return result;
    }

    @Override
    public PageData<NocAccessRequestData> searchAccessRequest(Pageable pageable, Integer hashedRequestId,
                                                              String companyName, String status, String visitLocation,
                                                              LocalDate visitDateFrom, LocalDate visitDateTo) {
        Integer accessRequestId = getAccessRequestId(hashedRequestId);

        companyName = StringUtils.trimToNull(companyName);
        status = StringUtils.trimToNull(status);
        visitLocation = StringUtils.trimToNull(visitLocation);

        Page<NocAccessRequestBean> pageBean = nocAccessRequestService.searchAccessRequest(pageable,
                accessRequestId, companyName, status, visitLocation, visitDateFrom, visitDateTo);

        // populate content
        List<NocAccessRequestBean> beanList = pageBean.getContent();
        List<NocAccessRequestData> dataList = new LinkedList<>();
        if(!CollectionUtils.isEmpty(beanList)){
            for(NocAccessRequestBean bean : beanList){
                NocAccessRequestData data = new NocAccessRequestData();
                nocAccessRequestDataPopulator.populate(bean, data);
                dataList.add(data);
            }
        }

        return new PageData<>(dataList, pageBean.getPageable(), pageBean.getTotalElements());
    }

    @Override
    public PageData<NocAccessRequestVisitorData> searchAccessRequestVisitor(
            Pageable pageable,
            String visitorName, String companyName,
            String visitLocation, LocalDate visitDateFrom, LocalDate visitDateTo) {

        visitorName = StringUtils.trimToNull(visitorName);
        companyName = StringUtils.trimToNull(companyName);
        visitLocation = StringUtils.trimToNull(visitLocation);

        Page<NocAccessRequestVisitorBean> pageBean = nocAccessRequestVisitorService.searchAccessRequest( pageable,
                visitorName, companyName, visitLocation, visitDateFrom, visitDateTo );

        // populate content
        List<NocAccessRequestVisitorBean> beanList = pageBean.getContent();
        List<NocAccessRequestVisitorData> dataList = new LinkedList<>();
        if(!CollectionUtils.isEmpty(beanList)){
            for(NocAccessRequestVisitorBean bean : beanList){
                NocAccessRequestVisitorData data = new NocAccessRequestVisitorData();
                nocAccessRequestVisitorDataPopulator.populate(bean, data);
                dataList.add(data);
            }
        }

        return new PageData<>(dataList, pageBean.getPageable(), pageBean.getTotalElements());
    }

    @Override
    public List<NocAccessRequestData> getPendingAccessRequest(String targetLocation) {
        Pageable pageable = PageRequest.of(0, 500);
        String status = "A";

        Page<NocAccessRequestData> pageData = searchAccessRequest(pageable,
                null, null, status, targetLocation, null, null);
        return pageData.getContent();
    }

    @Override
    public List<NocAccessRequestData> getRecentCompletedAccessRequest(String targetLocation) {
        Pageable pageable = PageRequest.of(0, 500);
        String status = "C";
        LocalDate visitDateTo = LocalDate.now();
        LocalDate visitDateFrom = visitDateTo.minusDays(30);

        Page<NocAccessRequestData> pageData = searchAccessRequest(pageable,
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
        NocAccessRequestBean accessRequestBean = new NocAccessRequestBean();
        // TODO: 2019/7/17  Change Model for Populator
        //nocAccessRequestBeanPopulator.populate(createAccessFormData, accessRequestBean);

        // create access request
        Integer newAccessRequestId;
        try {
            newAccessRequestId = nocAccessRequestService.createAccessRequest(accessRequestBean);
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
        Integer hashedRequestId = nocAccessRequestHashService.getHashedId(newAccessRequestId);
        if (hashedRequestId==null) {
            LOG.warn("Request created but hashed id not found! (newAccessRequestId: " + newAccessRequestId + ")");
            return CreateResultData.of("Request created but hashed id not found!");
        }

        // send confirmation email
        try {
            nocAccessRequestService.sendAccessRequestCfmEmail(newAccessRequestId);
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
            nocAccessRequestVisitorService.checkin(visitorAccessId, visitorCardNum);
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
            nocAccessRequestVisitorService.checkout(visitorAccessId);
            return null;
        }catch (AuthorityNotFoundException | AccessRequestVisitorNotFoundException |
                InvalidWorkflowException | AccessRequestNotFoundException e){
            return e.getMessage();
        }
    }

    @SuppressWarnings("Duplicates")
    @Override
    public List<NocOperationHistData> getOperationHistList(Integer hashedRequestId) {
        Integer accessRequestId = getAccessRequestId(hashedRequestId);
        if(accessRequestId==null){
            LOG.warn("Access request id not found for hashed ID " + hashedRequestId + ".");
            return null;
        }

        List<NocOperationHistBean> beanList;
        try {
            beanList = nocOperationHistService.getAccessRequestOptHistList(accessRequestId);
        } catch (AuthorityNotFoundException e){
            LOG.warn(e.getMessage());
            return null;
        }
        if(CollectionUtils.isEmpty(beanList)){
            return new LinkedList<>();
        }

        List<NocOperationHistData> dataList = new LinkedList<>();
        for(NocOperationHistBean bean : beanList){
            NocOperationHistData data = new NocOperationHistData();
            nocOperationHistDataPopulator.populate(bean, data);
            dataList.add(data);
        }
        return dataList;
    }

    private Integer getAccessRequestId(Integer hashedRequestId){
        if(hashedRequestId==null){
            return null;
        }
        return nocAccessRequestHashService.getAccessRequestId(hashedRequestId);
    }
}
