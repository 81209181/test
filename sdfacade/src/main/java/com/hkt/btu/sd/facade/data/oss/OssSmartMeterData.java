package com.hkt.btu.sd.facade.data.oss;

import com.hkt.btu.common.facade.data.DataInterface;

public class OssSmartMeterData implements DataInterface {
    private String poleId;
    private String model;
    private String exchange;
    private String sb;

    private String region;
    private String district;
    private String street;
    private String streetSection;

    public String getPoleId() {
        return poleId;
    }

    public void setPoleId(String poleID) {
        this.poleId = poleId;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getExchange() {
        return exchange;
    }

    public void setExchange(String exchange) {
        this.exchange = exchange;
    }

    public String getSb() {
        return sb;
    }

    public void setSb(String sb) {
        this.sb = sb;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getStreetSection() {
        return streetSection;
    }

    public void setStreetSection(String streetSection) {
        this.streetSection = streetSection;
    }
}
