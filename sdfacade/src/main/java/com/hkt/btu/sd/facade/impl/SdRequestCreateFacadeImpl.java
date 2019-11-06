package com.hkt.btu.sd.facade.impl;

import com.hkt.btu.common.core.exception.InvalidInputException;
import com.hkt.btu.sd.core.service.bean.SdServiceTypeBean;
import com.hkt.btu.sd.facade.*;
import com.hkt.btu.sd.facade.constant.ServiceSearchEnum;
import com.hkt.btu.sd.facade.data.*;
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
        ticketFacade.getService(sdTicketMasData.getTicketMasId()).ifPresent(sdTicketServiceData -> {
            Optional.ofNullable(sdTicketServiceData.getJobId()).ifPresent(s -> {
                infoData.setJobId(s);
                Optional.ofNullable(wfmApiFacade.getJobDetails(Integer.valueOf(s)).getJobBean()).map(WfmJobBeanData::getStatus).ifPresent(infoData::setJobStatus);
            });

            List<RequestCreateSearchResultData> resultsDataList = findData4Bsn(sdTicketServiceData.getServiceCode()).getList();
            if (CollectionUtils.isNotEmpty(resultsDataList)) {
                RequestCreateSearchResultData requestCreateSearchResultData = resultsDataList.get(0);
                if (requestCreateSearchResultData != null) {
                    if (requestCreateSearchResultData.getServiceNo().equals(sdTicketServiceData.getServiceCode())) {
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
                        if (StringUtils.isNotEmpty(sdTicketMasData.getSearchKey()) ? sdTicketMasData.getSearchKey().equals("dn") : false) {
                            infoData.setRelatedBsn(norarsApiFacade.getBsnByDn(sdTicketServiceData.getServiceCode()));
                        }
                    }
                }
            }
            infoData.setServiceType(sdTicketServiceData.getServiceType());
            infoData.setServiceTypeDesc(serviceTypeFacade.getServiceTypeDescByServiceTypeCode(sdTicketServiceData.getServiceType()));
            infoData.setServiceNo(sdTicketServiceData.getServiceCode());
            infoData.setNgn3reset(sdTicketServiceData.getServiceType().equals(SdServiceTypeBean.SERVICE_TYPE.VOIP));
        });
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

    private RequestCreateSearchResultsData findData4Dn(String dn) {
        RequestCreateSearchResultsData data = findData4Bsn(dn);
        data.getList().forEach(requestCreateSearchResultData -> {
            requestCreateSearchResultData.setRelatedBsn(norarsApiFacade.getBsnByDn(dn));
        });
        return data;
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

    private RequestCreateSearchResultsData findData4Bsn(String bsn) {
        RequestCreateSearchResultsData resultsData = new RequestCreateSearchResultsData();
        List<RequestCreateSearchResultData> resultDataList = new ArrayList<>();
        //find in BES API
        Optional<BesCustomerData> besCustomerData = Optional.ofNullable(besApiFacade.queryCustomerByServiceCode(bsn));
        besCustomerData.map(BesCustomerData::getCustomerInfos)
                .map(BesCustomerInfosData::getCustBasicInfo)
                .map(BesCustBasicInfoData::getCustCode)
                .flatMap(s -> Optional.ofNullable(getBesSubscriberDataForSearchResult(s, bsn))
                        .map(BesSubscriberData::getSubscriberInfos))
                .ifPresent(subscriberInfos -> subscriberInfos.forEach(info -> {
                    RequestCreateSearchResultData resultData = new RequestCreateSearchResultData();
                    requestCreateSearchResultDataPopulator.populateFromBesSubscriberInfoResourceData(info, resultData);
                    resultDataList.add(resultData);
                }));
        //find in ITSM API
        /*Optional.ofNullable(itsmApiFacade.searchProfileByServiceNo(bsn)).map(ItsmSearchProfileResponseData::getList).ifPresent(list -> list.forEach(profileData -> {
            RequestCreateSearchResultData resultData = new RequestCreateSearchResultData();
            requestCreateSearchResultDataPopulator.populateFromItsmProfileData(profileData, resultData);
            resultDataList.add(resultData);
        }));*/
        // fill in customer data
        if (CollectionUtils.isNotEmpty(resultDataList)) {
            besCustomerData.ifPresent(bes -> resultDataList.forEach(resultData -> {
                requestCreateSearchResultDataPopulator.populateFromBesCustomerDataData(bes, resultData);
                SdServiceTypeData serviceTypeByOfferName = serviceTypeFacade.getServiceTypeByOfferName(resultData.getOfferName());
                resultData.setServiceType(serviceTypeByOfferName.getServiceTypeCode());
                resultData.setServiceTypeDesc(serviceTypeByOfferName.getServiceTypeName());

                //find in NORA API
                Optional.ofNullable(norarsApiFacade.getServiceAddressByBsn(bsn)).ifPresent(serviceAddressData -> {
                    resultData.setServiceAddress(serviceAddressData.getServiceAddress());
                    resultData.setGridId(serviceAddressData.getGridId());
                    resultData.setExchangeBuildingId(serviceAddressData.getExchangeBuildingId());
                });
                resultData.setDescription(norarsApiFacade.getL1InfoByBsn(bsn));

                //find in WFM API
                Optional.ofNullable(wfmApiFacade.getPendingOrderByBsn(bsn)).ifPresent(pendingOrderData -> {
                    resultData.setOrderId(pendingOrderData.getOrderId() == 0 ? null : pendingOrderData.getOrderId());
                    resultData.setOrderType(pendingOrderData.getOrderType());
                    resultData.setFulfillmentId(pendingOrderData.getFulfillmentId() == 0 ? null : pendingOrderData.getFulfillmentId());
                    resultData.setFulfillmentType(pendingOrderData.getFulfillmentType());
                    resultData.setServiceReadyDate(pendingOrderData.getServiceReadyDate());
                    resultData.setAppointmentDate(pendingOrderData.getAppointmentDate());
                });
            }));
        } else {
            resultsData.setErrorMsg(String.format("Service(s) not found with %s .", bsn));
        }
        resultsData.setList(resultDataList);
        return resultsData;
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

    private BesSubscriberData getBesSubscriberDataForSearchResult(String besCustCode, String besServiceId) {
        if (StringUtils.isNotEmpty(besServiceId)) {
            return besApiFacade.querySubscriberByServiceNumber(besServiceId);
        } else if (StringUtils.isNotEmpty(besCustCode)) {
            return besApiFacade.querySubscriberByCustomerCode(besCustCode);
        } else {
            LOG.warn("Cannot get subscriber without both besCustCode and besServiceId.");
            return null;
        }
    }

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
