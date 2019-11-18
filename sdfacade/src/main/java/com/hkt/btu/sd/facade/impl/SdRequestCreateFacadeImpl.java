package com.hkt.btu.sd.facade.impl;

import com.hkt.btu.common.core.exception.InvalidInputException;
import com.hkt.btu.sd.core.service.bean.SdServiceTypeBean;
import com.hkt.btu.sd.facade.*;
import com.hkt.btu.sd.facade.constant.ServiceSearchEnum;
import com.hkt.btu.sd.facade.data.*;
import com.hkt.btu.sd.facade.data.wfm.WfmPendingOrderData;
import com.hkt.btu.sd.facade.populator.RequestCreateSearchResultDataPopulator;
import com.hkt.btu.sd.facade.populator.SdTicketInfoDataPopulator;
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
    @Resource(name = "ticketInfoDataPopulator")
    SdTicketInfoDataPopulator ticketInfoDataPopulator;

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
    public SdTicketInfoData getTicketInfo(SdTicketMasData ticketMasData) {
        SdTicketInfoData ticketInfoData = new SdTicketInfoData();

        // get ticket info from db
        ticketInfoDataPopulator.populateFromSdTicketMasData(ticketMasData,ticketInfoData);

        // get customer info from BES
        BesCustomerData besCustomerData = besApiFacade.queryCustomerByCustomerCode(ticketMasData.getCustCode());
        ticketInfoDataPopulator.populateFromBesCustomerData(besCustomerData, ticketInfoData);

        SdTicketServiceData ticketServiceData = ticketFacade.getService(ticketMasData.getTicketMasId());
        if (ticketServiceData != null) {
            // todo [SERVDESK-208] Detach Service Info from Ticket Page: service should be a list
            //  0. remove below service data
            //  1. frontend loop all serviceNumber+serviceType from ajax return of TicketController.getServiceInfo
            //  2. call API to fill-in data
            //  3. reflect API data on frontend

            // find service data
            List<RequestCreateSearchResultData> resultsDataList;
            if ("DN".equals(ticketInfoData.getSearchKeyDesc())) {
                resultsDataList = findData4Dn(ticketServiceData.getServiceCode()).getList();
            } else {
                resultsDataList = findData4Bsn(ticketServiceData.getServiceCode()).getList();
            }
            if (CollectionUtils.isNotEmpty(resultsDataList)) {
                RequestCreateSearchResultData requestCreateSearchResultData = resultsDataList.get(0);
                if (requestCreateSearchResultData != null) {
                    if (requestCreateSearchResultData.getServiceNo().equals(ticketServiceData.getServiceCode())) {
                        // todo [SERVDESK-208] Detach Service Info from Ticket Page
                        //  0. remove below service data
                        ticketInfoData.setServiceStatus(requestCreateSearchResultData.getServiceStatus());
                        ticketInfoData.setServiceStatusDesc(requestCreateSearchResultData.getServiceStatusDesc());
                        ticketInfoData.setSubsId(requestCreateSearchResultData.getSubsId());
                        ticketInfoData.setOfferName(requestCreateSearchResultData.getOfferName());
                        ticketInfoData.setItsmUrl(requestCreateSearchResultData.getUrl());
                        ticketInfoData.setDescription(requestCreateSearchResultData.getDescription());
                        ticketInfoData.setServiceAddress(requestCreateSearchResultData.getServiceAddress());
                        ticketInfoData.setGridId(requestCreateSearchResultData.getGridId());
                        ticketInfoData.setExchangeBuildingId(requestCreateSearchResultData.getExchangeBuildingId());
                        ticketInfoData.setRelatedBsn(requestCreateSearchResultData.getRelatedBsn());
                    }
                }
            }

            // translate service type code to name
            String serviceTypeDesc = serviceTypeFacade.getServiceTypeDescByServiceTypeCode(ticketServiceData.getServiceType());
            ticketInfoData.setServiceTypeDesc(serviceTypeDesc);

            ticketInfoDataPopulator.populateFromSdTicketServiceData(ticketServiceData, ticketInfoData);
        }
        return ticketInfoData;
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

            String serviceType = resultData.getServiceType();
            if (SdServiceTypeBean.SERVICE_TYPE.ENTERPRISE_CLOUD.equals(serviceType) ||
                SdServiceTypeBean.SERVICE_TYPE.ENTERPRISE_CLOUD_365.equals(serviceType)) {
                resultData.setDetailButton(true);
            }
            BesSubscriberData besSubscriberData = besApiFacade.querySubscriberByServiceNumber(resultData.getServiceNo());
            if (besSubscriberData != null) {
                resultData.setSubsId(besSubscriberData.getSubscriberInfos().get(0).getSubInfo().getSubsId());
            }
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
