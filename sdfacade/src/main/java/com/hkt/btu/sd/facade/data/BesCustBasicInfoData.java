package com.hkt.btu.sd.facade.data;

import com.hkt.btu.common.facade.data.DataInterface;

public class BesCustBasicInfoData implements DataInterface {


    public class CUST_REG_TYPE {
        public static final String BUSINESS_REGISTRATION = "1";
        public static final String PASSPORT = "2";
        public static final String IDENTIFICATION_CARD = "3";
        public static final String SCHOOL_CERTIFICATION = "4";
        public static final String CERTIFICATION_INCORPORATION = "5";
        public static final String NO_BR_NUMBER = "6";
    }

    public class CUST_SEGMENT {
        public static final String CORPERATE = "1";
        public static final String SME = "2";
        public static final String CES = "3";
    }

    public class CUST_SUB_SEGMENT {
        public static final String TOP_50_ACCOUNT = "1";
        public static final String TOP_100_ACCOUNT = "2";
        public static final String TOP_300_ACCOUNT = "3";
    }

    public class INDUCTRY_TYPE {
        public static final String BANKING_FINANCE_INVESTMENT = "1";
        public static final String CATERING = "2";
        public static final String CONSTRUCTION_MINING = "3";
        public static final String EDUCATION = "4";
        public static final String ELECTRICITY_GAS_SUPPLY = "5";
        public static final String ENTERTAINMENT_TOURISM = "6";
        public static final String GOVERMENT = "7";
        public static final String HEALTH_CARE = "8";
        public static final String HOTELS = "9";
        public static final String INFORMATION_TECHNOLOGY = "10";
        public static final String INSURANCE = "11";
        public static final String MANUFACTURING = "12";
        public static final String MEDIA_ADVERTISING = "13";
        public static final String MOBILE_SERVICE = "14";
        public static final String NON_GOVERNMENT_ORGANIZATION = "15";
        public static final String PROPERTY_REAL_ESTATE = "16";
        public static final String RETAIL_WHOLESALES = "17";
        public static final String TRADING_IMPORT_EXPORT = "18";
        public static final String TRANSPORTATION_LOGISTIC = "19";
        public static final String OTHERS = "20";
    }

    public class CORPORATION_TYPE {
        public static final String GLOBAL = "1";
        public static final String REGIONAL = "2";
        public static final String LOCAL = "3";
    }

    public class CORPORATION_SIZE {
        public static final String FROM_0_TO_100 = "1";
        public static final String FROM_101_TO_1000 = "2";
        public static final String FROM_1000_ONWARDS = "3";
    }

    public class LANGUAGE_PREFERENCE {
        public static final String CHINESE = "2001";
        public static final String ENGLISH = "2002";
    }

    public class SENSITIVE_CUSTOMER {
        public static final String YES = "Y";
        public static final String NO = "N";
    }






    private String custCode;
    private String custName;
    private String custRegistrationType;
    private String documentNumber;
    private String custSegment;
    private String custSubSegment;
    private String AGN;
    private String keepConfidentialPwd;
    private String industryType;
    private String corporationType;
    private String corporationSize;
    private String businessFax;
    private String businessTel;
    private String languagePreference;
    private String registrationDate;
    private String sensitiveCustomer;
    private String custRemark;


    public String getCustCode() {
        return custCode;
    }

    public void setCustCode(String custCode) {
        this.custCode = custCode;
    }

    public String getCustName() {
        return custName;
    }

    public void setCustName(String custName) {
        this.custName = custName;
    }

    public String getCustRegistrationType() {
        return custRegistrationType;
    }

    public void setCustRegistrationType(String custRegistrationType) {
        this.custRegistrationType = custRegistrationType;
    }

    public String getDocumentNumber() {
        return documentNumber;
    }

    public void setDocumentNumber(String documentNumber) {
        this.documentNumber = documentNumber;
    }

    public String getCustSegment() {
        return custSegment;
    }

    public void setCustSegment(String custSegment) {
        this.custSegment = custSegment;
    }

    public String getCustSubSegment() {
        return custSubSegment;
    }

    public void setCustSubSegment(String custSubSegment) {
        this.custSubSegment = custSubSegment;
    }

    public String getAGN() {
        return AGN;
    }

    public void setAGN(String AGN) {
        this.AGN = AGN;
    }

    public String getKeepConfidentialPwd() {
        return keepConfidentialPwd;
    }

    public void setKeepConfidentialPwd(String keepConfidentialPwd) {
        this.keepConfidentialPwd = keepConfidentialPwd;
    }

    public String getIndustryType() {
        return industryType;
    }

    public void setIndustryType(String industryType) {
        this.industryType = industryType;
    }

    public String getCorporationType() {
        return corporationType;
    }

    public void setCorporationType(String corporationType) {
        this.corporationType = corporationType;
    }

    public String getCorporationSize() {
        return corporationSize;
    }

    public void setCorporationSize(String corporationSize) {
        this.corporationSize = corporationSize;
    }

    public String getBusinessFax() {
        return businessFax;
    }

    public void setBusinessFax(String businessFax) {
        this.businessFax = businessFax;
    }

    public String getBusinessTel() {
        return businessTel;
    }

    public void setBusinessTel(String businessTel) {
        this.businessTel = businessTel;
    }

    public String getLanguagePreference() {
        return languagePreference;
    }

    public void setLanguagePreference(String languagePreference) {
        this.languagePreference = languagePreference;
    }

    public String getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate(String registrationDate) {
        this.registrationDate = registrationDate;
    }

    public String getSensitiveCustomer() {
        return sensitiveCustomer;
    }

    public void setSensitiveCustomer(String sensitiveCustomer) {
        this.sensitiveCustomer = sensitiveCustomer;
    }

    public String getCustRemark() {
        return custRemark;
    }

    public void setCustRemark(String custRemark) {
        this.custRemark = custRemark;
    }

}
