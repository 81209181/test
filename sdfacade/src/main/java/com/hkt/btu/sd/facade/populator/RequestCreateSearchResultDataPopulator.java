package com.hkt.btu.sd.facade.populator;

import com.hkt.btu.common.facade.populator.AbstractDataPopulator;
import com.hkt.btu.sd.facade.data.*;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

public class RequestCreateSearchResultDataPopulator extends AbstractDataPopulator<RequestCreateSearchResultData> {

    private static final Logger LOG = LogManager.getLogger(RequestCreateSearchResultDataPopulator.class);

    private static RequestCreateSearchResultDataPopulator instance;

    public static synchronized RequestCreateSearchResultDataPopulator getInstance() {
        if (instance == null) {
            instance = new RequestCreateSearchResultDataPopulator();
        }
        return instance;
    }

    public void populateFromBesSubscriberInfoResourceData(BesSubscriberInfoResourceData source, RequestCreateSearchResultData target) {
        BesSubscriberBasicData besSubscriberBasicData = source.getSubInfo();
        if (besSubscriberBasicData != null) {
            populateFromBesSubscriberBasicTypeData(besSubscriberBasicData, target);
        }

        List<BesOfferingInstDetailInfoData> primaryOfferingList = source.getPrimaryOfferingList();
        if (!CollectionUtils.isEmpty(primaryOfferingList)) {
            BesOfferingInstDetailInfoData primaryOffer = primaryOfferingList.get(0);
            populateFromBesPrimaryOfferData(primaryOffer, target);
        }
    }

    public void populateFromBesPrimaryOfferData(BesOfferingInstDetailInfoData source, RequestCreateSearchResultData target) {
        BesOfferingInstData besOfferingInstData = source.getOfferingBasic();
        if (besOfferingInstData != null) {
            populateFromBesOfferingInstData(besOfferingInstData, target);
        }
    }

    public void populateFromBesOfferingInstData(BesOfferingInstData source, RequestCreateSearchResultData target) {
        target.setOfferName(source.getOfferingName());
        target.setServiceStatus(source.getStatus());
    }

    private void populateFromBesSubscriberBasicTypeData(BesSubscriberBasicData source, RequestCreateSearchResultData target) {

        target.setServiceNo(source.getServiceNumber());
        target.setSubsId(source.getSubsId());

        String status = source.getStatus();
        if (StringUtils.equals(status, BesSubscriberBasicData.STATUS.TO_BE_ACTIVATED)) {
            target.setStatusDesc("To Be Activated");
        } else if (StringUtils.equals(status, BesSubscriberBasicData.STATUS.VALID)) {
            target.setStatusDesc("Valid");
        } else if (StringUtils.equals(status, BesSubscriberBasicData.STATUS.BARRED)) {
            target.setStatusDesc("Barred");
        } else if (StringUtils.equals(status, BesSubscriberBasicData.STATUS.SUSPENDED)) {
            target.setStatusDesc("Suspended");
        } else if (StringUtils.equals(status, BesSubscriberBasicData.STATUS.PRE_DEREGISTERED)) {
            target.setStatusDesc("Pre-deregistered");
        } else if (StringUtils.equals(status, BesSubscriberBasicData.STATUS.DEREGISTERED)) {
            target.setStatusDesc("Deregistered");
        } else {
            target.setStatusDesc("Unknown");
        }
    }

    public void populateFromBesCustomerDataData(BesCustomerData source, RequestCreateSearchResultData target) {
        BesCustomerInfosData besCustomerInfosData = source.getCustomerInfos();
        if (besCustomerInfosData != null) {
            populateFromBesCustomerInfosData(besCustomerInfosData, target);
        }
    }

    private void populateFromBesCustomerInfosData(BesCustomerInfosData source, RequestCreateSearchResultData target) {
        try {
            long custId = Long.parseLong(source.getCustomerId());
            target.setCustId(custId);
        } catch (NumberFormatException e) {
            LOG.warn(e.getMessage(), e);
        }

        BesCustBasicInfoData besCustBasicInfoData = source.getCustBasicInfo();
        if (besCustBasicInfoData != null) {
            populateFromBesCustBasicInfoData(besCustBasicInfoData, target);
        }

        BesContactPersonInfoData besContactPersonInfoData =
                CollectionUtils.isEmpty(source.getContactPersonInfo()) ? null : source.getContactPersonInfo().get(0);
        if (besContactPersonInfoData != null) {
            populateFromBesContactPersonInfoData(besContactPersonInfoData, target);
        }
    }

    private void populateFromBesCustBasicInfoData(BesCustBasicInfoData source, RequestCreateSearchResultData target) {
        target.setCustCode(source.getCustCode());
        target.setCustName(source.getCustName());
        target.setCustType(source.getCustType());
        target.setCustStatus(source.getStatus());
        switch (source.getLanguagePreference()) {
            case BesCustBasicInfoData.LANGUAGE_PREFERENCE.CHINESE:
                target.setLanguagePreference("Chinese");
                break;
            case BesCustBasicInfoData.LANGUAGE_PREFERENCE.ENGLISH:
                target.setLanguagePreference("English");
                break;
            default:
                target.setLanguagePreference("Unknown");
        }
    }

    private void populateFromBesContactPersonInfoData(BesContactPersonInfoData source, RequestCreateSearchResultData target) {
        target.setContactName(source.getName());
        target.setContactEmail(source.getEmail());
        target.setContactNumber(source.getOfficePhone());
    }

    public void populateFromItsmProfileData(ItsmProfileData source, RequestCreateSearchResultData target) {
        target.setAdminId(source.getAdminId());

        target.setCustCode(source.getCustCode());
        target.setCustId(source.getCustId());
        target.setCustName(source.getCustName());
        target.setCustomer(source.getCustomer());

        target.setDomainName(source.getDomainName());
        target.setEmail(source.getEmail());
        target.setMobileNo(source.getMobileNo());

        target.setJmsOrderNum(source.getJmsOrderNum());
        target.setL1OrderNum(source.getL1OrderNum());
        target.setLastUpdId(source.getLastUpdId());

        target.setOfferName(source.getOfferName());
        target.setPackageType(source.getPackageType());
        target.setPassword(source.getPassword());
        target.setPid(source.getPid());
        target.setPrefix(source.getPrefix());
        target.setProductDesc(source.getProductDesc());
        target.setProfileId(source.getProfileId());

        target.setResourceId(source.getResourceId());
        target.setResourceName(source.getResourceName());

        target.setServiceNo(source.getServiceNo());

        target.setStatus(source.getStatus());
        target.setStatusDesc(source.getStatusDesc());
        target.setStb(source.getStb());

        target.setTenantId(source.getTenantId());
        target.setType(source.getType());
        target.setUrl(source.getUrl());
        target.setUserName(source.getUserName());

        target.setServiceType(source.getServiceType());
    }

}
