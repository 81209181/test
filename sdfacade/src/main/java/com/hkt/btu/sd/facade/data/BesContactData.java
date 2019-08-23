package com.hkt.btu.sd.facade.data;

import com.hkt.btu.common.facade.data.DataInterface;

public class BesContactData implements DataInterface {


    public class OWNER_ENTITY_TYPE {
        public static final String ACCOUNT = "A";
        public static final String GROUP_USER = "G";
        public static final String USER = "S";
        public static final String CUSTOMER = "C";
    }

    public class CONTRACT_TYPE {
        public static final String PRIMARY_OFFERING_AGREEMENT = "P";
        public static final String SUPPLEMENTARY_OFFERING_AGREEMENT = "S";
    }

    public class PERIOD_UNIT {
        public static final String BILL_CYCLE = "C";
        public static final String DAY = "D";
        public static final String HOUR = "H";
        public static final String MONTH = "M";
        public static final String WEEK = "W";
        public static final String YEAR = "Y";
    }

    public class STATUS {
        public static final String TO_BE_ACTIVATED = "1";
        public static final String ACTIVATED = "2";
        public static final String SUSPENDED = "4";
        public static final String PRE_DEREGISTERED = "8";
        public static final String UNSUBSCRIBED = "9";
    }

    private long contractInstId;
    private String ownerEntityType;
    private long offeringInstId;

    private String contractId;
    private String contractCode;
    private String contractType;

    private String periodUnit;
    private String period;

    private String status;
    private String statusTime;

    private String effDate;
    private String expDate;


    public long getContractInstId() {
        return contractInstId;
    }

    public void setContractInstId(long contractInstId) {
        this.contractInstId = contractInstId;
    }

    public String getOwnerEntityType() {
        return ownerEntityType;
    }

    public void setOwnerEntityType(String ownerEntityType) {
        this.ownerEntityType = ownerEntityType;
    }

    public long getOfferingInstId() {
        return offeringInstId;
    }

    public void setOfferingInstId(long offeringInstId) {
        this.offeringInstId = offeringInstId;
    }

    public String getContractId() {
        return contractId;
    }

    public void setContractId(String contractId) {
        this.contractId = contractId;
    }

    public String getContractCode() {
        return contractCode;
    }

    public void setContractCode(String contractCode) {
        this.contractCode = contractCode;
    }

    public String getContractType() {
        return contractType;
    }

    public void setContractType(String contractType) {
        this.contractType = contractType;
    }

    public String getPeriodUnit() {
        return periodUnit;
    }

    public void setPeriodUnit(String periodUnit) {
        this.periodUnit = periodUnit;
    }

    public String getPeriod() {
        return period;
    }

    public void setPeriod(String period) {
        this.period = period;
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
