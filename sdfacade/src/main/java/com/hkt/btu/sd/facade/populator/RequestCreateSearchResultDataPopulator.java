package com.hkt.btu.sd.facade.populator;

import com.hkt.btu.common.facade.populator.AbstractDataPopulator;
import com.hkt.btu.sd.facade.data.*;
import com.hkt.btu.sd.facade.data.wfm.WfmPendingOrderData;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

public class RequestCreateSearchResultDataPopulator extends AbstractDataPopulator<RequestCreateSearchResultData> {
    private static final Logger LOG = LogManager.getLogger(RequestCreateSearchResultDataPopulator.class);

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

    private void populateFromBesPrimaryOfferData(BesOfferingInstDetailInfoData source, RequestCreateSearchResultData target) {
        BesOfferingInstData besOfferingInstData = source.getOfferingBasic();
        if (besOfferingInstData != null) {
            populateFromBesOfferingInstData(besOfferingInstData, target);
        }
    }

    private void populateFromBesOfferingInstData(BesOfferingInstData source, RequestCreateSearchResultData target) {
        target.setOfferName(source.getOfferingName());
        target.setServiceStatus(source.getStatus());
    }

    private void populateFromBesSubscriberBasicTypeData(BesSubscriberBasicData source, RequestCreateSearchResultData target) {
        target.setServiceNo(source.getServiceNumber());
        target.setSubsId(source.getSubsId());

        String status = source.getStatus();
        if (StringUtils.equals(status, BesSubscriberBasicData.STATUS.TO_BE_ACTIVATED)) {
            target.setServiceStatusDesc(BesSubscriberBasicData.STATUS_DESC.TO_BE_ACTIVATED);
        } else if (StringUtils.equals(status, BesSubscriberBasicData.STATUS.VALID)) {
            target.setServiceStatusDesc(BesSubscriberBasicData.STATUS_DESC.VALID);
        } else if (StringUtils.equals(status, BesSubscriberBasicData.STATUS.BARRED)) {
            target.setServiceStatusDesc(BesSubscriberBasicData.STATUS_DESC.BARRED);
        } else if (StringUtils.equals(status, BesSubscriberBasicData.STATUS.SUSPENDED)) {
            target.setServiceStatusDesc(BesSubscriberBasicData.STATUS_DESC.SUSPENDED);
        } else if (StringUtils.equals(status, BesSubscriberBasicData.STATUS.PRE_DEREGISTERED)) {
            target.setServiceStatusDesc(BesSubscriberBasicData.STATUS_DESC.PRE_DEREGISTERED);
        } else if (StringUtils.equals(status, BesSubscriberBasicData.STATUS.DEREGISTERED)) {
            target.setServiceStatusDesc(BesSubscriberBasicData.STATUS_DESC.DEREGISTERED);
        } else {
            target.setServiceStatusDesc(BesSubscriberBasicData.STATUS_DESC.UNDEFINED);
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
        switch (source.getCustType()) {
            case "1":
                target.setCustType("Business Registration");
                break;
            case "2":
                target.setCustType("Passport");
                break;
            case "3":
                target.setCustType("ID Card");
                break;
            case "4":
                target.setCustType("School Certificate");
                break;
            case "5":
                target.setCustType("Certificate of Incorporation");
                break;
            case "6":
                target.setCustType("NO BR Number");
                break;
            default:
                target.setCustType("Unknown");
        }
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
//        target.setStatusDesc(source.getStatusDesc());
        target.setStb(source.getStb());

        target.setTenantId(source.getTenantId());
        target.setType(source.getType());
        target.setUrl(source.getUrl());
        target.setUserName(source.getUserName());

        target.setServiceType(source.getServiceType());
    }

    public void populateFromServiceAddressData (ServiceAddressData source, RequestCreateSearchResultData target){
        target.setServiceAddress(source.getServiceAddress());
        target.setGridId(source.getGridId());
        target.setExchangeBuildingId(source.getExchangeBuildingId());
    }

    public void populateFromWfmPendingOrderData (WfmPendingOrderData source, RequestCreateSearchResultData target){
        target.setOrderId(source.getOrderId()==null? null:source.getOrderId() == 0 ? null : source.getOrderId());
        target.setOrderType(source.getOrderType());
        target.setFulfillmentId(source.getFulfillmentId()==null? null:source.getFulfillmentId() == 0 ? null : source.getFulfillmentId());
        target.setFulfillmentType(source.getFulfillmentType());
        target.setServiceReadyDate(source.getServiceReadyDate());
        target.setAppointmentDate(source.getAppointmentDate());
    }

}
