package com.hkt.btu.sd.facade.data;

import com.hkt.btu.common.facade.data.DataInterface;

public class BesCustomerData implements DataInterface {

    public class BOOLEAN_PARAM {
        public static final String YES = "Y";
        public static final String NO = "N";
    }

    public class ID_TYPE {
        public static final String BUSINESS_REGISTRATION = "1";
        public static final String PASSPORT = "2";
        public static final String IDENTIFICATION_CARD = "3";
        public static final String SCHOOL_CERTIFICATE = "4";
        public static final String CERTIFICATE_INCORPORATION = "5";
        public static final String NO_BR_NUMBER = "6";
    }


    private BesCustomerInfosData customerInfos;



    public BesCustomerInfosData getCustomerInfos() {
        return customerInfos;
    }

    public void setCustomerInfos(BesCustomerInfosData customerInfos) {
        this.customerInfos = customerInfos;
    }
}
