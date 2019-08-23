package com.hkt.btu.sd.facade.data;

import com.hkt.btu.common.facade.data.DataInterface;

public class BesContactPersonInfoData implements DataInterface {


    public class CONTACT_TYPE {
        public static final String ADMIN = "1";
        public static final String TECHNICAL = "2";
        public static final String BILL = "3";
        public static final String ON_SITE = "4";
    }

    public class COMM_OPTION {
        public static final String SMS = "1";
        public static final String EMAIL = "2";
    }

    private String contactPersonType;
    private String name;
    private String mobilePhone;
    private String officePhone;
    private String email;
    private String commOptions;


    public String getContactPersonType() {
        return contactPersonType;
    }

    public void setContactPersonType(String contactPersonType) {
        this.contactPersonType = contactPersonType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getCommOptions() {
        return commOptions;
    }

    public void setCommOptions(String commOptions) {
        this.commOptions = commOptions;
    }

}
