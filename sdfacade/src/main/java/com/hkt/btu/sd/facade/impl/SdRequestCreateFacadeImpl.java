package com.hkt.btu.sd.facade.impl;

import com.hkt.btu.common.core.exception.InvalidInputException;
import com.hkt.btu.sd.facade.BesApiFacade;
import com.hkt.btu.sd.facade.ItsmApiFacade;
import com.hkt.btu.sd.facade.NorarsApiFacade;
import com.hkt.btu.sd.facade.SdRequestCreateFacade;
import com.hkt.btu.sd.facade.data.*;
import com.hkt.btu.sd.facade.populator.RequestCreateSearchResultDataPopulator;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Resource;
import java.util.ArrayList;
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

    @Resource(name = "requestCreateSearchResultDataPopulator")
    RequestCreateSearchResultDataPopulator requestCreateSearchResultDataPopulator;

    @Override
    public RequestCreateSearchResultsData searchProductList(String searchKey, String searchValue) {
        RequestCreateSearchResultsData resultsData = new RequestCreateSearchResultsData();
        try {
            switch (searchKey) {
                case "sn":
                case "bsn":
                    return findData4Bsn(searchValue);
                case "tenantId":
                    return findData4Tenant(searchValue);
                case "dn":
                    return findData4Dn(searchValue);
                default:
                    String errorMsg = "Unknown search key: " + searchKey + ", search value: " + searchValue;
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

    private RequestCreateSearchResultsData findData4Dn(String dn) {
        return Optional.ofNullable(norarsApiFacade.getBsnByDn(dn))
                .map(NorarsBsnData::getBsn)
                .map(this::findData4Bsn).get();
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
            besCustomerData.ifPresent(bes -> resultDataList.forEach(resultData -> requestCreateSearchResultDataPopulator.populateFromBesCustomerDataData(bes, resultData)));
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
