package com.hkt.btu.sd.facade.data.bes;

import com.hkt.btu.common.facade.data.DataInterface;

public class BesOfferingInstAttrData implements DataInterface {


    private String propCode;
    private String propValue;

    private String effDate;
    private String expDate;



    public String getPropCode() {
        return propCode;
    }

    public void setPropCode(String propCode) {
        this.propCode = propCode;
    }

    public String getPropValue() {
        return propValue;
    }

    public void setPropValue(String propValue) {
        this.propValue = propValue;
    }

    public String getEffDate() {
        return effDate;
    }

    public void setEffDate(String effDate) {
        this.effDate = effDate;
    }

    public String getExpDate() {
        return expDate;
    }

    public void setExpDate(String expDate) {
        this.expDate = expDate;
    }

}
