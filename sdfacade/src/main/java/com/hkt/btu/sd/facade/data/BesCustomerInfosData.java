package com.hkt.btu.sd.facade.data;

import com.hkt.btu.common.facade.data.DataInterface;

import java.util.List;

public class BesCustomerInfosData implements DataInterface {
    private String customerId;

    private BesCustBasicInfoData custBasicInfo;
    private List<BesAddressInfoData> addressInfo;
    private List<BesContactPersonInfoData> contactPersonInfo;
    private List<BesContactHktInfoData> contactHKTInfo;


    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public BesCustBasicInfoData getCustBasicInfo() {
        return custBasicInfo;
    }

    public void setCustBasicInfo(BesCustBasicInfoData custBasicInfo) {
        this.custBasicInfo = custBasicInfo;
    }

    public List<BesAddressInfoData> getAddressInfo() {
        return addressInfo;
    }

    public void setAddressInfo(List<BesAddressInfoData> addressInfo) {
        this.addressInfo = addressInfo;
    }

    public List<BesContactPersonInfoData> getContactPersonInfo() {
        return contactPersonInfo;
    }

    public void setContactPersonInfo(List<BesContactPersonInfoData> contactPersonInfo) {
        this.contactPersonInfo = contactPersonInfo;
    }

    public List<BesContactHktInfoData> getContactHKTInfo() {
        return contactHKTInfo;
    }

    public void setContactHKTInfo(List<BesContactHktInfoData> contactHKTInfo) {
        this.contactHKTInfo = contactHKTInfo;
    }
}
