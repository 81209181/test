package com.hkt.btu.sd.facade.impl;

import com.hkt.btu.common.core.exception.InvalidInputException;
import com.hkt.btu.sd.core.exception.ApiException;
import com.hkt.btu.sd.core.service.bean.SdServiceTypeBean;
import com.hkt.btu.sd.facade.*;
import com.hkt.btu.sd.facade.constant.ServiceSearchEnum;
import com.hkt.btu.sd.facade.data.*;
import com.hkt.btu.sd.facade.data.bes.BesCustomerData;
import com.hkt.btu.sd.facade.data.bes.BesFaultInfoData;
import com.hkt.btu.sd.facade.data.bes.BesSubscriberData;
import com.hkt.btu.sd.facade.data.bes.BesSubscriberInfoResourceData;
import com.hkt.btu.sd.facade.data.itsm.ItsmProfileData;
import com.hkt.btu.sd.facade.data.itsm.ItsmSearchProfileResponseData;
import com.hkt.btu.sd.facade.data.oss.OssSmartMeterData;
import com.hkt.btu.sd.facade.data.wfm.WfmPendingOrderData;
import com.hkt.btu.sd.facade.populator.RequestCreateSearchResultDataPopulator;
import com.hkt.btu.sd.facade.populator.SdTicketInfoDataPopulator;
import com.hkt.btu.sd.facade.populator.SdTicketServiceInfoDataPopulator;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class SdRequestCreateFacadeImpl implements SdRequestCreateFacade {
    private static final Logger LOG = LogManager.getLogger(SdRequestCreateFacadeImpl.class);

    @Resource(name = "besApiFacade")
    BesApiFacade besApiFacade;
    @Resource(name = "itsmApiFacade")
    ItsmApiFacade itsmApiFacade;
    @Resource(name = "norarsApiFacade")
    NorarsApiFacade norarsApiFacade;
    @Resource(name = "wfmApiFacade")
    WfmApiFacade wfmApiFacade;
    @Resource(name = "serviceTypeFacade")
    SdServiceTypeFacade serviceTypeFacade;
    @Resource(name = "ossApiFacade")
    OssApiFacade ossApiFacade;

    @Resource(name = "requestCreateSearchResultDataPopulator")
    RequestCreateSearchResultDataPopulator requestCreateSearchResultDataPopulator;
    @Resource(name = "ticketInfoDataPopulator")
    SdTicketInfoDataPopulator ticketInfoDataPopulator;
    @Resource(name = "ticketServiceInfoDataPopulator")
    SdTicketServiceInfoDataPopulator ticketServiceInfoDataPopulator;

    @Override
    public List<ServiceSearchEnum> getSearchKeyEnumList() {
        return Arrays.asList(ServiceSearchEnum.values());
    }

    @Override
    public SdRequestCreateSearchResultsData searchProductList(String searchKey, String searchValue) {
        SdRequestCreateSearchResultsData resultsData = new SdRequestCreateSearchResultsData();
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
                case POLE_ID:
                    return findData4SmartMeter(searchValue);
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
        ticketInfoDataPopulator.populateFromSdTicketMasData(ticketMasData, ticketInfoData);

        // get customer info from BES
        if (StringUtils.isNotBlank(ticketMasData.getCustCode())) {
            BesCustomerData besCustomerData = besApiFacade.queryCustomerByCustomerCode(ticketMasData.getCustCode());
            ticketInfoDataPopulator.populateFromBesCustomerData(besCustomerData, ticketInfoData);
        } else {
            if (ServiceSearchEnum.TENANT_ID.getKey().equalsIgnoreCase(ticketMasData.getSearchKey())) {
                List<ItsmProfileData> profileData = itsmApiFacade.searchProfileByTenantId(ticketMasData.getSearchValue()).getList();
                if (CollectionUtils.isNotEmpty(profileData)) {
                    ticketInfoDataPopulator.populateFromItsmProfileData(profileData.get(0), ticketInfoData);
                }

            }
        }
        return ticketInfoData;
    }

    @Override
    public SdTicketServiceInfoData getServiceInfoInApi(String serviceTypeCode, String serviceNumber) {
        SdRequestCreateSearchResultData resultData = new SdRequestCreateSearchResultData();

        String bsn = null;
        String bnBsn = null;
        String eCloudServiceNo = null;
        String poleId = null;
        switch (serviceTypeCode) {
            case SdServiceTypeBean.SERVICE_TYPE.BROADBAND:
                bsn = serviceNumber;
                bnBsn = serviceNumber;
                break;
            case SdServiceTypeBean.SERVICE_TYPE.VOIP:
            case SdServiceTypeBean.SERVICE_TYPE.FIX_NUMBER:
                bsn = serviceNumber;
                bnBsn = norarsApiFacade.getBsnByDn(serviceNumber);
                resultData.setRelatedBsn(bnBsn);
                break;
            case SdServiceTypeBean.SERVICE_TYPE.ENTERPRISE_CLOUD:
            case SdServiceTypeBean.SERVICE_TYPE.ENTERPRISE_CLOUD_365:
                eCloudServiceNo = serviceNumber;
                bsn = serviceNumber; // some new enterprise cloud is in BSE
                break;
            case SdServiceTypeBean.SERVICE_TYPE.SMART_METER:
                poleId = serviceNumber;
                break;
            default:
                break;
        }

        // get data with e-cloud service number
        if (eCloudServiceNo != null) {
            ItsmSearchProfileResponseData itsmSearchProfileResponseData = itsmApiFacade.searchProfileByServiceNo(eCloudServiceNo);
            List<ItsmProfileData> itsmProfileDataList = itsmSearchProfileResponseData == null ? null : itsmSearchProfileResponseData.getList();
            if (CollectionUtils.isNotEmpty(itsmProfileDataList)) {
                ItsmProfileData itsmProfileData = itsmProfileDataList.get(0);
                requestCreateSearchResultDataPopulator.populateFromItsmProfileData(itsmProfileData, resultData);
            }
        }

        // get data with bsn
        if (bsn != null) {
            BesSubscriberData besSubscriberData = besApiFacade.querySubscriberByServiceNumber(bsn);
            if (!Objects.isNull(besSubscriberData)) {
                BesSubscriberInfoResourceData besSubscriberInfoResourceData = besSubscriberData.getSubscriberInfos().get(0);
                requestCreateSearchResultDataPopulator.populateFromBesSubscriberInfoResourceData(besSubscriberInfoResourceData, resultData);
            }

            BesCustomerData besCustomerData = besApiFacade.queryCustomerByServiceCode(bsn);
            if (!Objects.isNull(besCustomerData)) {
                requestCreateSearchResultDataPopulator.populateFromBesCustomerDataData(besCustomerData, resultData);
            }

            fillPendingOrderDataByBsn(bsn, resultData);
        }

        // get data with bn bsn
        if (bnBsn != null) {
            fillBnData(bnBsn, resultData);
        }

        // get data with poleId
        if (poleId != null) {
            SdRequestCreateSearchResultsData resultsData = findData4SmartMeter(poleId);
            resultData = CollectionUtils.isEmpty(resultsData.getList()) ? resultData : resultsData.getList().get(0);
        }

        SdTicketServiceInfoData serviceInfoData = new SdTicketServiceInfoData();
        ticketServiceInfoDataPopulator.populateFormRequestCreateSearchResultData(resultData, serviceInfoData);
        return serviceInfoData;
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

    private SdRequestCreateSearchResultsData findData4Bsn(String bsn) {
        // search basic customer and service info
        SdRequestCreateSearchResultsData resultsData = findCustomerServiceInfo(bsn);

        // service not found
        List<SdRequestCreateSearchResultData> besSubscriberInfoDataList = resultsData == null ? null : resultsData.getList();
        if (CollectionUtils.isEmpty(besSubscriberInfoDataList)) {
            resultsData = new SdRequestCreateSearchResultsData();
            resultsData.setErrorMsg(String.format("Service(s) not found with %s.", bsn));
            return resultsData;
        }

        // fill in detail info
        try {
            for (SdRequestCreateSearchResultData resultData : besSubscriberInfoDataList) {
                fillBnData(bsn, resultData);
                fillPendingOrderDataByBsn(bsn, resultData);
            }
        } catch (ApiException e) {
            LOG.warn(e.getMessage(), e);
            resultsData.setWarningMsg(e.getMessage());
        }

        resultsData.setList(besSubscriberInfoDataList);
        return resultsData;
    }

    private SdRequestCreateSearchResultsData findData4Dn(String dn) {
        // search basic customer and service info
        SdRequestCreateSearchResultsData resultsData = findCustomerServiceInfo(dn);

        // service not found
        List<SdRequestCreateSearchResultData> besSubscriberInfoDataList = resultsData == null ? null : resultsData.getList();
        if (CollectionUtils.isEmpty(besSubscriberInfoDataList)) {
            resultsData = new SdRequestCreateSearchResultsData();
            resultsData.setErrorMsg(String.format("Service(s) not found with %s.", dn));
            return resultsData;
        }

        // fill in detail info
        try {
            for (SdRequestCreateSearchResultData resultData : besSubscriberInfoDataList) {
                String bnBsn = norarsApiFacade.getBsnByDn(dn);
                if (StringUtils.isNotEmpty(bnBsn)) {
                    resultData.setRelatedBsn(bnBsn);
                    fillBnData(bnBsn, resultData);
                }

                fillPendingOrderDataByBsn(dn, resultData);
            }
        } catch (ApiException e) {
            LOG.warn(e.getMessage(), e);
            resultsData.setWarningMsg(e.getMessage());
        }

        resultsData.setList(besSubscriberInfoDataList);
        return resultsData;
    }

    private SdRequestCreateSearchResultsData findData4Tenant(String tenantId) {
        ItsmSearchProfileResponseData searchProfileResponseData = itsmApiFacade.searchProfileByTenantId(tenantId);
        List<ItsmProfileData> profileDataList = searchProfileResponseData == null ? null : searchProfileResponseData.getList();

        List<SdRequestCreateSearchResultData> resultDataList = new ArrayList<>();
        SdRequestCreateSearchResultData resultData;
        if (CollectionUtils.isNotEmpty(profileDataList)) {
            for (ItsmProfileData itsmProfileData : profileDataList) {
                if (StringUtils.isNotEmpty(itsmProfileData.getCustCode())) {
                    if (!itsmProfileData.getServiceNo().equals(tenantId)) {
                        resultData = new SdRequestCreateSearchResultData();
                        requestCreateSearchResultDataPopulator.populateFromItsmProfileData(itsmProfileData, resultData);
                        BesSubscriberInfoResourceData besSubscriberInfoResourceData = besApiFacade.querySubscriberByServiceNumber(itsmProfileData.getServiceNo()).getSubscriberInfos().get(0);
                        BesCustomerData besCustomerData = besApiFacade.queryCustomerByServiceCode(itsmProfileData.getServiceNo());
                        requestCreateSearchResultDataPopulator.populateFromBesSubscriberInfoResourceData(besSubscriberInfoResourceData, resultData);
                        requestCreateSearchResultDataPopulator.populateFromBesCustomerDataData(besCustomerData, resultData);
                        SdServiceTypeData serviceTypeData = serviceTypeFacade.getServiceTypeByOfferName(resultData.getOfferName());
                        requestCreateSearchResultDataPopulator.populateFromServiceTypeData(serviceTypeData, resultData);
                        resultDataList.add(resultData);
                    }
                } else {
                    resultData = new SdRequestCreateSearchResultData();
                    requestCreateSearchResultDataPopulator.populateFromItsmProfileData(itsmProfileData, resultData);
                    SdServiceTypeData serviceTypeData = serviceTypeFacade.getServiceTypeByOfferName(itsmProfileData.getOfferName());
                    requestCreateSearchResultDataPopulator.populateFromServiceTypeData(serviceTypeData, resultData);
                    resultDataList.add(resultData);
                }
            }
        }

        // build response
        SdRequestCreateSearchResultsData resultsData = new SdRequestCreateSearchResultsData();
        if (CollectionUtils.isEmpty(resultDataList)) {
            resultsData.setErrorMsg(String.format("Service(s) not found with %s .", tenantId));
        } else {
            resultsData.setList(resultDataList);
        }
        return resultsData;
    }

    private SdRequestCreateSearchResultsData findData4SmartMeter(String searchValue) {
        SdRequestCreateSearchResultsData resultsData = new SdRequestCreateSearchResultsData();
        List<SdRequestCreateSearchResultData> resultDataList = new ArrayList<>();
        SdRequestCreateSearchResultData resultData = new SdRequestCreateSearchResultData();

        Integer poleId = Integer.parseInt(searchValue);
        OssSmartMeterData ossSmartMeterData = ossApiFacade.queryMeterInfo(poleId);

        if (ossSmartMeterData != null) {
            requestCreateSearchResultDataPopulator.populateFromOssSmartMeterData(ossSmartMeterData, resultData);
            resultDataList.add(resultData);
        }

        if (CollectionUtils.isEmpty(resultDataList)) {
            resultsData.setErrorMsg(String.format("Service(s) not found with %s .", poleId));
        } else {
            resultsData.setList(resultDataList);
        }
        return resultsData;
    }

    private SdRequestCreateSearchResultsData findCustomerServiceInfo(String serviceNumber) {
        SdRequestCreateSearchResultsData resultData = new SdRequestCreateSearchResultsData();
        List<SdRequestCreateSearchResultData> requestSearchResultDataList = new ArrayList<>();

        // get service data list
        BesSubscriberData besSubscriberData = besApiFacade.querySubscriberByServiceNumber(serviceNumber);
        List<BesSubscriberInfoResourceData> besSubscriberInfoDataList = besSubscriberData == null ? null : besSubscriberData.getSubscriberInfos();
        if (CollectionUtils.isEmpty(besSubscriberInfoDataList)) {
            return null;
        }

        // get customer data
        BesCustomerData besCustomerData = besApiFacade.queryCustomerByServiceCode(serviceNumber);

        // transform service data list
        for (BesSubscriberInfoResourceData besSubscriberInfoData : besSubscriberInfoDataList) {
            SdRequestCreateSearchResultData searchResultData = new SdRequestCreateSearchResultData();

            // fill in service info
            requestCreateSearchResultDataPopulator.populateFromBesSubscriberInfoResourceData(besSubscriberInfoData, searchResultData);
            // fill in customer info
            requestCreateSearchResultDataPopulator.populateFromBesCustomerDataData(besCustomerData, searchResultData);

            // resolve offer name to service type
            String offerName = searchResultData.getOfferName();
            SdServiceTypeData serviceTypeData = serviceTypeFacade.getServiceTypeByOfferName(offerName);
            searchResultData.setServiceType(serviceTypeData.getServiceTypeCode());
            searchResultData.setServiceTypeDesc(serviceTypeData.getServiceTypeName());
            requestCreateSearchResultDataPopulator.populateFromServiceTypeData(serviceTypeData, searchResultData);

            // add to result list
            requestSearchResultDataList.add(searchResultData);
        }
        resultData.setList(requestSearchResultDataList);
        return resultData;
    }

    private void fillBnData(String bnBsn, SdRequestCreateSearchResultData resultData) {
        // find service address
        SdServiceAddressData serviceAddressData = norarsApiFacade.getServiceAddressByBsn(bnBsn);
        if (serviceAddressData != null) {
            requestCreateSearchResultDataPopulator.populateFromServiceAddressData(serviceAddressData, resultData);
        }

        // find L1 info
        String l1Info = norarsApiFacade.getL1InfoByBsn(bnBsn);
        resultData.setDescription(l1Info);
    }

    private void fillPendingOrderDataByBsn(String bsn, SdRequestCreateSearchResultData resultData) throws ApiException {
        WfmPendingOrderData wfmPendingOrderData = wfmApiFacade.getPendingOrderByBsn(bsn);
        if (wfmPendingOrderData == null) {
            throw new ApiException("WFM API response is null.");
        } else if (StringUtils.isNotEmpty(wfmPendingOrderData.getErrorMsg())) {
            throw new ApiException(wfmPendingOrderData.getErrorMsg());
        }

        requestCreateSearchResultDataPopulator.populateFromWfmPendingOrderData(wfmPendingOrderData, resultData);
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
