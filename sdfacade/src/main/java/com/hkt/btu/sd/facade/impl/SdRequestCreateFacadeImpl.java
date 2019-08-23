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
            if (StringUtils.startsWith(searchKey, "bes_")) {
                String besServiceId = StringUtils.equals(searchKey, "besServiceId") ? searchValue : null;
                // find customer with BES API
                BesCustomerData besCustomerData = getBesCustomerDataForSearchResult(searchKey, searchValue);
                String besCustCode = besApiFacade.getBesCustomerCode(besCustomerData);
                if (StringUtils.isEmpty(besCustCode)) {
                    String errorMsg = String.format("Service(s) not found with BES [searchKey: %s, searchValue:%s].", searchKey, searchValue);
                    LOG.info(errorMsg);
                    resultsData.setErrorMsg(errorMsg);
                    return resultsData;
                }
                // find subscribe list with BES API
                BesSubscriberData besSubscriberData = getBesSubscriberDataForSearchResult(besCustCode, besServiceId);
                List<BesSubscriberInfoResourceData> subscriberInfos = besSubscriberData == null ? null : besSubscriberData.getSubscriberInfos();
                if (CollectionUtils.isEmpty(subscriberInfos)) {
                    String errorMsg = String.format("Service(s) not found with BES API [besCustCode: %s, besServiceId:%s].", besCustCode, besServiceId);
                    LOG.info(errorMsg);
                    resultsData.setErrorMsg(errorMsg);
                    return resultsData;
                }
                // transform result
                List<RequestCreateSearchResultData> resultDataList = new ArrayList<>();
                for (BesSubscriberInfoResourceData subscriberInfoResourceData : subscriberInfos) {
                    RequestCreateSearchResultData resultData = new RequestCreateSearchResultData();
                    requestCreateSearchResultDataPopulator.populateFromBesSubscriberInfoResourceData(
                            subscriberInfoResourceData, resultData);
                    resultDataList.add(resultData);
                }
                // fill in customer data
                if (!CollectionUtils.isEmpty(resultDataList) && besCustomerData != null) {
                    for (RequestCreateSearchResultData resultData : resultDataList) {
                        requestCreateSearchResultDataPopulator.populateFromBesCustomerDataData(besCustomerData, resultData);
                    }
                }
                // set result
                resultsData.setList(resultDataList);
                return resultsData;
            } else if (StringUtils.startsWith(searchKey, "itsm_")) {
                // get data from ITSM API
                ItsmSearchProfileResponseData itsmResponseData = getItsmDataForSearchResult(searchKey, searchValue);
                List<ItsmProfileData> itsmProfileDataList = itsmResponseData == null ? null : itsmResponseData.getList();
                if (CollectionUtils.isEmpty(itsmProfileDataList)) {
                    String errorMsg = String.format("Service(s) not found with ITSM [searchKey: %s, searchValue:%s].", searchKey, searchValue);
                    LOG.info(errorMsg);
                    resultsData.setErrorMsg(errorMsg);
                    return resultsData;
                }
                // transform result
                List<RequestCreateSearchResultData> resultDataList = new ArrayList<>();
                for (ItsmProfileData itsmProfileData : itsmProfileDataList) {
                    RequestCreateSearchResultData resultData = new RequestCreateSearchResultData();
                    requestCreateSearchResultDataPopulator.populateFromItsmProfileData(itsmProfileData, resultData);
                    resultDataList.add(resultData);
                }
                // set result
                resultsData.setList(resultDataList);
                return resultsData;
            } else if (StringUtils.startsWith(searchKey, "norars_dn")) {
                // get data from NORARS
                NorarsBsnData norarsBsnData = norarsApiFacade.getBsnByDn(searchValue);
                String bsn = norarsBsnData == null ? null : norarsBsnData.getBsn();
                if (StringUtils.isEmpty(bsn)) {
                    String errorMsg = "BSN of DN (" + searchValue + ") not found in NORARS.";
                    LOG.warn(errorMsg);
                    resultsData.setErrorMsg(errorMsg);
                    return resultsData;
                }
                // get data from BES
                RequestCreateSearchResultsData besResultData = searchProductList("bes_serviceId", norarsBsnData.getBsn());
                if (StringUtils.isNotEmpty(besResultData.getErrorMsg())) {
                    String errorMsg = "BSN (" + bsn + ") of DN (" + searchValue + ") not found in BES.";
                    LOG.warn(errorMsg);
                    resultsData.setErrorMsg(errorMsg);
                    return resultsData;
                }
                return besResultData;
            } else {
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
        if (!StringUtils.isEmpty(besServiceId)) {
            return besApiFacade.querySubscriberByServiceNumber(besServiceId);
        } else if (!StringUtils.isEmpty(besCustCode)) {
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
