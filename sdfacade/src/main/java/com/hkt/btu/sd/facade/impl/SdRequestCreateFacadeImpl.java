package com.hkt.btu.sd.facade.impl;

import com.hkt.btu.common.core.exception.InvalidInputException;
import com.hkt.btu.sd.core.service.bean.SdServiceTypeBean;
import com.hkt.btu.sd.facade.*;
import com.hkt.btu.sd.facade.constant.ServiceSearchEnum;
import com.hkt.btu.sd.facade.data.*;
import com.hkt.btu.sd.facade.data.wfm.WfmPendingOrderData;
import com.hkt.btu.sd.facade.populator.RequestCreateSearchResultDataPopulator;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class SdRequestCreateFacadeImpl implements SdRequestCreateFacade {
    private static final Logger LOG = LogManager.getLogger(SdRequestCreateFacadeImpl.class);

    @Resource(name = "besApiFacade")
    BesApiFacade besApiFacade;
    @Resource(name = "itsmApiFacade")
    ItsmApiFacade itsmApiFacade;
    @Resource(name = "norarsApiFacade")
    NorarsApiFacade norarsApiFacade;
    @Resource(name = "ticketFacade")
    SdTicketFacade ticketFacade;
    @Resource(name = "wfmApiFacade")
    WfmApiFacade wfmApiFacade;
    @Resource(name = "serviceTypeFacade")
    SdServiceTypeFacade serviceTypeFacade;

    @Resource(name = "requestCreateSearchResultDataPopulator")
    RequestCreateSearchResultDataPopulator requestCreateSearchResultDataPopulator;

    @Override
    public List<ServiceSearchEnum> getSearchKeyEnumList() {
        return Arrays.asList(ServiceSearchEnum.values());
    }

    @Override
    public RequestCreateSearchResultsData searchProductList(String searchKey, String searchValue) {
        RequestCreateSearchResultsData resultsData = new RequestCreateSearchResultsData();
        ServiceSearchEnum serviceSearchEnum = ServiceSearchEnum.getEnum(searchKey);
        if (serviceSearchEnum == null) {
            String errorMsg = "Unknown search key: " + searchKey + ", search value: " + searchValue;
            LOG.warn(errorMsg);
            resultsData.setErrorMsg(errorMsg);
            return resultsData;
        }
        try {
            switch (serviceSearchEnum) {
                case BSN:
                    return findData4Bsn(searchValue);
                case DN:
                    return findData4Dn(searchValue);
                case TENANT_ID:
                    return findData4Tenant(searchValue);
                default:
                    String errorMsg = "Unexpected search key: " + searchKey + ", search value: " + searchValue;
                    LOG.warn(errorMsg);
                    resultsData.setErrorMsg(errorMsg);
                    return resultsData;
            }
        } catch (InvalidInputException e) {
            LOG.warn(e.getMessage());
            resultsData.setErrorMsg(e.getMessage());
            return resultsData;
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            resultsData.setErrorMsg("Internal System Error!");
            return resultsData;
        }
    }

    @Override
    public SdTicketInfoData getTicketInfo(SdTicketMasData sdTicketMasData) {
        SdTicketInfoData infoData = new SdTicketInfoData();
        infoData.setCustCode(sdTicketMasData.getCustCode());
        infoData.setTicketMasId(sdTicketMasData.getTicketMasId());
        infoData.setTicketStatus(sdTicketMasData.getStatus());
        infoData.setTicketStatusDesc(sdTicketMasData.getStatusDesc());
        infoData.setTicketType(sdTicketMasData.getTicketType());
        infoData.setAsap(sdTicketMasData.getAsap());
        infoData.setCallInCount(sdTicketMasData.getCallInCount());
        infoData.setSearchKeyDesc(sdTicketMasData.getSearchKeyDesc());
        infoData.setSearchValue(sdTicketMasData.getSearchValue());
        infoData.setAppointmentDate(sdTicketMasData.getAppointmentDate());
        SdTicketServiceData serviceData = ticketFacade.getService(sdTicketMasData.getTicketMasId());
        if (serviceData != null) {
            String jobId = serviceData.getJobId();
            if (StringUtils.isNotBlank(jobId)) {
                infoData.setJobId(jobId);
                WfmJobBeanData wfmJobBeanData = wfmApiFacade.getJobDetails(Integer.valueOf(jobId)).getJobBean();
                if (wfmJobBeanData != null) {
                    infoData.setJobStatus(wfmJobBeanData.getStatus());
                }
            }
            List<RequestCreateSearchResultData> resultsDataList;
            if (infoData.getSearchKeyDesc().equals("DN")) {
                resultsDataList = findData4Dn(serviceData.getServiceCode()).getList();
            } else {
                resultsDataList = findData4Bsn(serviceData.getServiceCode()).getList();
            }
            if (CollectionUtils.isNotEmpty(resultsDataList)) {
                RequestCreateSearchResultData requestCreateSearchResultData = resultsDataList.get(0);
                if (requestCreateSearchResultData != null) {
                    if (requestCreateSearchResultData.getServiceNo().equals(serviceData.getServiceCode())) {
                        infoData.setCustName(requestCreateSearchResultData.getCustName());
                        infoData.setCustType(requestCreateSearchResultData.getCustType());
                        infoData.setCustStatus(requestCreateSearchResultData.getCustStatus());
                        infoData.setLanguagePreference(requestCreateSearchResultData.getLanguagePreference());
                        infoData.setServiceStatus(requestCreateSearchResultData.getServiceStatus());
                        infoData.setServiceStatusDesc(requestCreateSearchResultData.getServiceStatusDesc());
                        infoData.setSubsId(requestCreateSearchResultData.getSubsId());
                        infoData.setOfferName(requestCreateSearchResultData.getOfferName());
                        infoData.setItsmUrl(requestCreateSearchResultData.getUrl());
                        infoData.setDescription(requestCreateSearchResultData.getDescription());
                        infoData.setServiceAddress(requestCreateSearchResultData.getServiceAddress());
                        infoData.setGridId(requestCreateSearchResultData.getGridId());
                        infoData.setExchangeBuildingId(requestCreateSearchResultData.getExchangeBuildingId());
                        infoData.setRelatedBsn(requestCreateSearchResultData.getRelatedBsn());
                    }
                }
            }
            infoData.setServiceType(serviceData.getServiceType());
            infoData.setServiceTypeDesc(serviceTypeFacade.getServiceTypeDescByServiceTypeCode(serviceData.getServiceType()));
            infoData.setServiceNo(serviceData.getServiceCode());
            infoData.setNgn3reset(serviceData.getServiceType().equals(SdServiceTypeBean.SERVICE_TYPE.VOIP));
        }
        return infoData;
    }

    @Override
    public BesFaultInfoData getCustomerInfo(String serviceCode) {
        return findData4Bsn(serviceCode).getList().stream().filter(requestCreateSearchResultData -> requestCreateSearchResultData.getServiceNo().equals(serviceCode))
                .findFirst().map(requestCreateSearchResultData -> {
                    BesFaultInfoData data = new BesFaultInfoData();
                    data.setCustName(requestCreateSearchResultData.getCustName());
                    data.setProductType(requestCreateSearchResultData.getOfferName());
                    return data;
                }).orElse(new BesFaultInfoData());
    }

    private RequestCreateSearchResultsData findData4Bsn(String bsn) {
        // search basic customer and service info
        RequestCreateSearchResultsData resultsData = findCustomerServiceInfo(bsn);

        // service not found
        List<RequestCreateSearchResultData> besSubscriberInfoDataList = resultsData==null ? null : resultsData.getList();
        if(CollectionUtils.isEmpty(besSubscriberInfoDataList)){
            resultsData = new RequestCreateSearchResultsData();
            resultsData.setErrorMsg(String.format("Service(s) not found with %s.", bsn));
            return resultsData;
        }

        // fill in detail info
        for(RequestCreateSearchResultData resultData : besSubscriberInfoDataList){
            fillBnData(bsn, resultData);
            fillPendingOrderData(bsn, resultData);
        }

        resultsData.setList(besSubscriberInfoDataList);
        return resultsData;
    }

    private RequestCreateSearchResultsData findData4Dn(String dn) {
        // search basic customer and service info
        RequestCreateSearchResultsData resultsData = findCustomerServiceInfo(dn);

        // service not found
        List<RequestCreateSearchResultData> besSubscriberInfoDataList = resultsData==null ? null : resultsData.getList();
        if(CollectionUtils.isEmpty(besSubscriberInfoDataList)){
            resultsData = new RequestCreateSearchResultsData();
            resultsData.setErrorMsg(String.format("Service(s) not found with %s.", dn));
            return resultsData;
        }

        // fill in detail info
        for(RequestCreateSearchResultData resultData : besSubscriberInfoDataList){
            String bnBsn = norarsApiFacade.getBsnByDn(dn);
            if (StringUtils.isNotEmpty(bnBsn)) {
                resultData.setRelatedBsn(bnBsn);
                fillBnData(bnBsn, resultData);
            }

            fillPendingOrderData(dn, resultData);
        }

        resultsData.setList(besSubscriberInfoDataList);
        return resultsData;
    }

    private RequestCreateSearchResultsData findData4Tenant(String tenantId) {
        RequestCreateSearchResultsData resultsData = new RequestCreateSearchResultsData();
        List<RequestCreateSearchResultData> resultDataList = new ArrayList<>();
        Optional.ofNullable(itsmApiFacade.searchProfileByTenantId(tenantId))
                .map(ItsmSearchProfileResponseData::getList)
                .ifPresent(list -> list.forEach(itsmProfileData -> {
                    RequestCreateSearchResultData resultData = new RequestCreateSearchResultData();
                    requestCreateSearchResultDataPopulator.populateFromItsmProfileData(itsmProfileData, resultData);
                    resultDataList.add(resultData);
                }));
        if (CollectionUtils.isEmpty(resultDataList)) {
            resultsData.setErrorMsg(String.format("Service(s) not found with %s .", tenantId));
            return resultsData;
        }

        for (RequestCreateSearchResultData resultData : resultDataList) {
            SdServiceTypeData serviceTypeByOfferName = serviceTypeFacade.getServiceTypeByOfferName(resultData.getOfferName());
            resultData.setServiceType(serviceTypeByOfferName.getServiceTypeCode());
            resultData.setServiceTypeDesc(serviceTypeByOfferName.getServiceTypeName());
        }

        resultsData.setList(resultDataList);
        return resultsData;
    }



    private RequestCreateSearchResultsData findCustomerServiceInfo(String serviceNumber) {
        RequestCreateSearchResultsData resultData = new RequestCreateSearchResultsData();
        List<RequestCreateSearchResultData> requestSearchResultDataList = new ArrayList<>();

        // get service data list
        BesSubscriberData besSubscriberData = besApiFacade.querySubscriberByServiceNumber(serviceNumber);
        List<BesSubscriberInfoResourceData> besSubscriberInfoDataList = besSubscriberData==null ? null : besSubscriberData.getSubscriberInfos();
        if(CollectionUtils.isEmpty(besSubscriberInfoDataList)){
            return null;
        }

        // get customer data
        BesCustomerData besCustomerData = besApiFacade.queryCustomerByServiceCode(serviceNumber);

        // transform service data list
        for(BesSubscriberInfoResourceData besSubscriberInfoData : besSubscriberInfoDataList){
            RequestCreateSearchResultData searchResultData = new RequestCreateSearchResultData();

            // fill in service info
            requestCreateSearchResultDataPopulator.populateFromBesSubscriberInfoResourceData(besSubscriberInfoData, searchResultData);
            // fill in customer info
            requestCreateSearchResultDataPopulator.populateFromBesCustomerDataData(besCustomerData, searchResultData);

            // resolve offer name to service type
            String offerName = searchResultData.getOfferName();
            SdServiceTypeData serviceTypeData = serviceTypeFacade.getServiceTypeByOfferName(offerName);
            searchResultData.setServiceType(serviceTypeData.getServiceTypeCode());
            searchResultData.setServiceTypeDesc(serviceTypeData.getServiceTypeName());

            // add to result list
            requestSearchResultDataList.add(searchResultData);
        }

        resultData.setList(requestSearchResultDataList);
        return resultData;
    }

    private void fillBnData(String bnBsn, RequestCreateSearchResultData resultData){
        // find service address
        ServiceAddressData serviceAddressData = norarsApiFacade.getServiceAddressByBsn(bnBsn);
        if(serviceAddressData!=null){
            requestCreateSearchResultDataPopulator.populateFromServiceAddressData(serviceAddressData, resultData);
        }

        // find L1 info
        String l1Info = norarsApiFacade.getL1InfoByBsn(bnBsn);
        resultData.setDescription(l1Info);
    }

    private void fillPendingOrderData(String serviceNumber, RequestCreateSearchResultData resultData){
        WfmPendingOrderData wfmPendingOrderData = wfmApiFacade.getPendingOrderByBsn(serviceNumber);
        if(wfmPendingOrderData!=null){
            requestCreateSearchResultDataPopulator.populateFromWfmPendingOrderData(wfmPendingOrderData, resultData);
        }
    }

    private ItsmSearchProfileResponseData getItsmDataForSearchResult(String searchKey, String searchValue) throws InvalidInputException {
        switch (searchKey) {
            case "itsm_custName":
                return itsmApiFacade.searchProfileByCustName(searchValue);
            case "itsm_custId":
                if (!StringUtils.isNumeric(searchValue)) {
                    throw new InvalidInputException("Customer ID should be numeric.");
                }
                return itsmApiFacade.searchProfileByCustId(searchValue);
            case "itsm_serviceNo":
                return itsmApiFacade.searchProfileByServiceNo(searchValue);
            case "itsm_tenantId":
                return itsmApiFacade.searchProfileByTenantId(searchValue);
            case "itsm_domainName":
                return itsmApiFacade.searchProfileByDomainName(searchValue);
            default:
                LOG.warn("Unknown ITSM search key: " + searchKey + ", search value: " + searchValue);
                return null;
        }

    }

//    private BesSubscriberData getBesSubscriberDataForSearchResult(String besCustCode, String besServiceId) {
//        if (StringUtils.isNotEmpty(besServiceId)) {
//            return besApiFacade.querySubscriberByServiceNumber(besServiceId);
//        } else if (StringUtils.isNotEmpty(besCustCode)) {
//            return besApiFacade.querySubscriberByCustomerCode(besCustCode);
//        } else {
//            LOG.warn("Cannot get subscriber without both besCustCode and besServiceId.");
//            return null;
//        }
//    }

    private BesCustomerData getBesCustomerDataForSearchResult(String searchKey, String searchValue) {
        switch (searchKey) {
            case "bes_custCode":
                return besApiFacade.queryCustomerByCustomerCode(searchValue);
            case "bes_serviceId":
                return besApiFacade.queryCustomerByServiceCode(searchValue);
            case "bes_brNo":
                return besApiFacade.queryCustomerByBusinessRegNum(searchValue);
            case "bes_passport":
                return besApiFacade.queryCustomerByPassport(searchValue);
            case "bes_idCard":
                return besApiFacade.queryCustomerByIdCard(searchValue);
            case "bes_schoolCert":
                return besApiFacade.queryCustomerBySchoolCert(searchValue);
            case "bes_certIncorp":
                return besApiFacade.queryCustomerByCertIncorp(searchValue);
            default:
                LOG.warn("Unknown BES search key: " + searchKey + ", search value: " + searchValue);
                return null;
        }
    }
}
