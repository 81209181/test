package com.hkt.btu.sd.facade.data;

import com.hkt.btu.common.facade.data.DataInterface;

public class BesContactHktInfoData implements DataInterface {


    public class TYPE {
        public static final String ACCOUNT_MANAGER = "1";
        public static final String CONSULTANT = "2";
        public static final String SALES_ASSISTANT = "3";
        public static final String SALES_SUPPORT = "4";
        public static final String ACCOUNT_SERVICE_MANAGER = "5";
    }


    private String type;
    private String name;
    private String operatorID;
    private String mobilePhone;
    private String officePhone;
    private String email;


    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOperatorID() {
        return operatorID;
    }

    public void setOperatorID(String operatorID) {
        this.operatorID = operatorID;
    }

    public String getMobilePhone() {
        return mobilePhone;
    }

    public void setMobilePhone(String mobilePhone) {
        this.mobilePhone = mobilePhone;
    }

    public String getOfficePhone() {
        return officePhone;
    }

    public void setOfficePhone(String officePhone) {
        this.officePhone = officePhone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

}
