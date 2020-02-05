package com.hkt.btu.sd.facade.impl;

import com.hkt.btu.common.facade.AbstractRestfulApiFacade;
import com.hkt.btu.sd.core.service.SdApiService;
import com.hkt.btu.sd.core.service.bean.SdApiProfileBean;
import com.hkt.btu.sd.facade.BesApiFacade;
import com.hkt.btu.sd.facade.data.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;


public class BesApiFacadeImpl extends AbstractRestfulApiFacade implements BesApiFacade {
    private static final Logger LOG = LogManager.getLogger(BesApiFacadeImpl.class);
    private static final long PAGE_SIZE = 100;
    private static final long PAGE_RECORD_TOTAL_LIMIT = 2000;

    @Resource(name = "apiService")
    SdApiService apiService;

    @Override
    protected SdApiProfileBean getTargetApiProfile() {
        return apiService.getBesApiProfileBean();
    }

    @Override
    public BesCustomerData queryCustomerByCustomerCode(String CustomerCode) {
        return queryCustomer(
                CustomerCode, null,
                null, null,
                false, false, true, false, false);
    }

    @Override
    public BesCustomerData queryCustomerByServiceCode(String serviceNumber) {
        return queryCustomer(
                null, serviceNumber,
                null, null,
                false, false, true, false, false);
    }

    @Override
    public BesCustomerData queryCustomerByBusinessRegNum(String businessRegNum) {
        return queryCustomer(
                null, null,
                BesCustomerData.ID_TYPE.BUSINESS_REGISTRATION, businessRegNum,
                false, false, true, false, false);
    }

    @Override
    public BesCustomerData queryCustomerByPassport(String passport) {
        return queryCustomer(
                null, null,
                BesCustomerData.ID_TYPE.PASSPORT, passport,
                false, false, true, false, false);
    }

    @Override
    public BesCustomerData queryCustomerByIdCard(String idCard) {
        return queryCustomer(null, null,
                BesCustomerData.ID_TYPE.IDENTIFICATION_CARD, idCard,
                false, false, false, false, false);
    }

    @Override
    public BesCustomerData queryCustomerBySchoolCert(String schoolCert) {
        return queryCustomer(null, null,
                BesCustomerData.ID_TYPE.SCHOOL_CERTIFICATE, schoolCert,
                false, false, false, false, false);
    }

    @Override
    public BesCustomerData queryCustomerByCertIncorp(String certIncorp) {
        return queryCustomer(null, null,
                BesCustomerData.ID_TYPE.CERTIFICATE_INCORPORATION, certIncorp,
                false, false, false, false, false);
    }

    @Override
    public BesSubscriberData querySubscriberByServiceNumber(String serviceNumber) {
        return querySubscriberAllRecords(
                serviceNumber, null, null,
                true, true, true );
    }

    @Override
    public BesSubscriberData querySubscriberByCustomerCode(String customerCode) {
        return querySubscriberAllRecords(
                null, customerCode, null,
                true, true, true );
    }

    @Override
    public String getBesCustomerCode(BesCustomerData besCustomerData) {
        BesCustomerInfosData besCustomerInfosData = besCustomerData==null ? null : besCustomerData.getCustomerInfos();
        BesCustBasicInfoData besCustBasicInfoData = besCustomerInfosData==null ? null : besCustomerInfosData.getCustBasicInfo();
        return besCustBasicInfoData==null ? null : besCustBasicInfoData.getCustCode();
    }

    private BesCustomerData queryCustomer(
            String customerCode, String serviceNumber, String idType, String idNumber,
            boolean includeDeactivation, boolean includeDefAcct, boolean includeContactPerson,
            boolean includeAddr, boolean includeCert) {
        String apiPath = "/apiaccess/httpservices/customer-mgmt/v1/customers";

        Map<String, String> queryParamMap = new HashMap<>();
        queryParamMap.put("customerCode", customerCode);
        queryParamMap.put("serviceNumber", serviceNumber);
        queryParamMap.put("idType", idType);
        queryParamMap.put("idNumber", idNumber);
        queryParamMap.put("includeDeactivation", includeDeactivation ? BesCustomerData.BOOLEAN_PARAM.YES : BesCustomerData.BOOLEAN_PARAM.NO);
        queryParamMap.put("includeDefAcct", includeDefAcct ? BesCustomerData.BOOLEAN_PARAM.YES : BesCustomerData.BOOLEAN_PARAM.NO);
        queryParamMap.put("includeContactPerson", includeContactPerson ? BesCustomerData.BOOLEAN_PARAM.YES : BesCustomerData.BOOLEAN_PARAM.NO);
        queryParamMap.put("includeAddr", includeAddr ? BesCustomerData.BOOLEAN_PARAM.YES : BesCustomerData.BOOLEAN_PARAM.NO);
        queryParamMap.put("includeCert", includeCert ? BesCustomerData.BOOLEAN_PARAM.YES : BesCustomerData.BOOLEAN_PARAM.NO);
        return getData(apiPath, BesCustomerData.class, queryParamMap);
    }
    private BesSubscriberData querySubscriberAllRecords(
            String serviceNumber, String customerCode, String accountCode,
            boolean includeOffer, boolean includeProd, boolean includeContract) {
        // get data from API
        long currentPageNum = 0;
        BesSubscriberData besSubscriberData = querySubscriber(
                serviceNumber, customerCode, accountCode, includeOffer, includeProd, includeContract,
                currentPageNum, PAGE_SIZE );
        if(besSubscriberData==null){
            return null;
        }

        // check received page info
        BesPageData besPageData = besSubscriberData.getPage();
        if(besPageData==null){
            LOG.error("Corrupted page data from BES API.");
            return null;
        }

        // check total number of records
        long totalNum = besPageData.getTotalNum();
        if( totalNum>PAGE_RECORD_TOTAL_LIMIT ){
            LOG.warn("Cannot get more than " + PAGE_RECORD_TOTAL_LIMIT + " records from BES API. (total: " + totalNum + ")");
        }

        return besSubscriberData;
    }
    private BesSubscriberData querySubscriber(
            String serviceNumber, String customerCode, String accountCode,
            boolean includeOffer, boolean includeProd, boolean includeContract,
            long pageStartnum, long pagePagesize) {
        String apiPath = "/apiaccess/httpservices/customer-mgmt/v1/subscribers";

        Map<String, String> queryParamMap = new HashMap<>();
        queryParamMap.put("serviceNumber", serviceNumber);
        queryParamMap.put("customerCode", customerCode);
        queryParamMap.put("accountCode", accountCode);
        queryParamMap.put("includeOffer", includeOffer ? BesSubscriberData.BOOLEAN_PARAM.YES : BesSubscriberData.BOOLEAN_PARAM.NO);
        queryParamMap.put("includeProd", includeProd ? BesSubscriberData.BOOLEAN_PARAM.YES : BesSubscriberData.BOOLEAN_PARAM.NO);
        queryParamMap.put("includeContract", includeContract ? BesSubscriberData.BOOLEAN_PARAM.YES : BesSubscriberData.BOOLEAN_PARAM.NO);
        queryParamMap.put("pageStartnum", String.valueOf(pageStartnum) );
        queryParamMap.put("pagePagesize", String.valueOf(pagePagesize) );

        // MUST be null so that actual total is return
//        queryParamMap.put("pageTotalnum", String.valueOf(pageTotalnum) );

        return getData(apiPath, BesSubscriberData.class, queryParamMap);
    }

}
