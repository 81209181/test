package com.hkt.btu.sd.facade.data;

import com.hkt.btu.common.facade.data.DataInterface;

public class BesSubscriberBasicData implements DataInterface {


    public class SUBS_VOICE_LANG {
        public static final String ENGLISH = "1";
        public static final String CANTONESE = "2";
        public static final String CHINESE = "3";
    }

    public class SBUS_WRITEN_LANG {
        public static final String ENGLISH = "1";
        public static final String TRANDITIONAL_CHINESE = "2";
        public static final String SIMPLIFY_CHINESE = "3";
    }

    public class BRAND {
        public static final String CSL = "1";
        public static final String THE_1010 = "2";
        public static final String HKT = "3";
    }

    public class PAYMENT_TYPE {
        public static final String PREPAID = "0";
        public static final String POSTPAID = "1";
    }

    public class PORT_FLAG {
        public static final String NORMAL = "0";
        public static final String PORT_IN = "1";
        public static final String PORT_OUT = "2";
        public static final String PORT_IN_INTERNALLY = "3";
        public static final String PORT_OUT_INTERNALLY = "4";
    }


    public class STATUS {
        public static final String TO_BE_ACTIVATED = "1";
        public static final String VALID = "2";
        public static final String BARRED = "3";
        public static final String SUSPENDED = "4";
        public static final String PRE_DEREGISTERED = "8";
        public static final String DEREGISTERED = "9";
    }

    public class SUB_TYPE_FLAG {
        public static final String COPE_USER = "C";
        public static final String BYOD_USER = "B";
    }


    private long subsId;
    private String subsName;
    private String subsVoiceLang;
    private String subsWritenLang;
    private long offeringId;
    private long brand;
    private String serviceNumber;
    private long defaultAcctId;
    private String paymentType;
    private String imsi;
    private String iccid;
    private String portFlag;
    private String effDate;
    private String expDate;
    private String status;
    private String statusTime;
    private String statusDetail;
    private String subTypeFlag;


    public long getSubsId() {
        return subsId;
    }

    public void setSubsId(long subsId) {
        this.subsId = subsId;
    }

    public String getSubsName() {
        return subsName;
    }

    public void setSubsName(String subsName) {
        this.subsName = subsName;
    }

    public String getSubsVoiceLang() {
        return subsVoiceLang;
    }

    public void setSubsVoiceLang(String subsVoiceLang) {
        this.subsVoiceLang = subsVoiceLang;
    }

    public String getSubsWritenLang() {
        return subsWritenLang;
    }

    public void setSubsWritenLang(String subsWritenLang) {
        this.subsWritenLang = subsWritenLang;
    }

    public long getOfferingId() {
        return offeringId;
    }

    public void setOfferingId(long offeringId) {
        this.offeringId = offeringId;
    }

    public long getBrand() {
        return brand;
    }

    public void setBrand(long brand) {
        this.brand = brand;
    }

    public String getServiceNumber() {
        return serviceNumber;
    }

    public void setServiceNumber(String serviceNumber) {
        this.serviceNumber = serviceNumber;
    }

    public long getDefaultAcctId() {
        return defaultAcctId;
    }

    public void setDefaultAcctId(long defaultAcctId) {
        this.defaultAcctId = defaultAcctId;
    }

    public String getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(String paymentType) {
        this.paymentType = paymentType;
    }

    public String getImsi() {
        return imsi;
    }

    public void setImsi(String imsi) {
        this.imsi = imsi;
    }

    public String getIccid() {
        return iccid;
    }

    public void setIccid(String iccid) {
        this.iccid = iccid;
    }

    public String getPortFlag() {
        return portFlag;
    }

    public void setPortFlag(String portFlag) {
        this.portFlag = portFlag;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatusTime() {
        return statusTime;
    }

    public void setStatusTime(String statusTime) {
        this.statusTime = statusTime;
    }

    public String getStatusDetail() {
        return statusDetail;
    }

    public void setStatusDetail(String statusDetail) {
        this.statusDetail = statusDetail;
    }

    public String getSubTypeFlag() {
        return subTypeFlag;
    }

    public void setSubTypeFlag(String subTypeFlag) {
        this.subTypeFlag = subTypeFlag;
    }

}
