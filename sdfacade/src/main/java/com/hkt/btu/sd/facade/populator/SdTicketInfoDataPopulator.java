package com.hkt.btu.sd.facade.populator;

import com.hkt.btu.common.facade.populator.AbstractDataPopulator;
import com.hkt.btu.sd.core.service.bean.SdServiceTypeBean;
import com.hkt.btu.sd.facade.data.*;

import java.util.Optional;

public class SdTicketInfoDataPopulator extends AbstractDataPopulator<SdTicketInfoData> {

    public void populateFromSdTicketMasData(SdTicketMasData source, SdTicketInfoData target) {
        target.setCustCode(source.getCustCode());
        target.setTicketMasId(source.getTicketMasId());
        target.setTicketStatus(source.getStatus());
        target.setTicketStatusDesc(source.getStatusDesc());
        target.setTicketType(source.getTicketType());
        target.setCallInCount(source.getCallInCount());
        target.setSearchKeyDesc(source.getSearchKeyDesc());
        target.setSearchValue(source.getSearchValue());
        target.setOwningRole(source.getOwningRole());
    }

    public void populateFromBesCustomerData(BesCustomerData source, SdTicketInfoData target) {
        Optional.ofNullable(source)
                .map(BesCustomerData::getCustomerInfos)
                .map(BesCustomerInfosData::getCustBasicInfo).ifPresent(data -> {
            target.setCustName(data.getCustName());
            target.setCustStatus(data.getStatus());
            switch (data.getCustType()) {
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
            switch (data.getLanguagePreference()) {
                case BesCustBasicInfoData.LANGUAGE_PREFERENCE.CHINESE:
                    target.setLanguagePreference("Chinese");
                    break;
                case BesCustBasicInfoData.LANGUAGE_PREFERENCE.ENGLISH:
                    target.setLanguagePreference("English");
                    break;
                default:
                    target.setLanguagePreference("Unknown");
            }
        });

    }

    public void populateFromItsmProfileData(ItsmProfileData itsmProfileData, SdTicketInfoData ticketInfoData) {
        ticketInfoData.setCustName(itsmProfileData.getCustName());
        ticketInfoData.setCustCode(itsmProfileData.getCustCode());
        ticketInfoData.setCustStatus(itsmProfileData.getStatus());
    }
}
