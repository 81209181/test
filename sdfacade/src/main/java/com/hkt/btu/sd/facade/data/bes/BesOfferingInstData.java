package com.hkt.btu.sd.facade.data.bes;

import com.hkt.btu.common.facade.data.DataInterface;

public class BesOfferingInstData implements DataInterface {


    public class BOOLEAN_PARAM {
        public static final String YES = "Y";
        public static final String NO = "N";
    }

    public class OWNER_ENTITY_TYPE {
        public static final String ACCOUNT = "A";
        public static final String GROUP_USER = "G";
        public static final String USER = "S";
        public static final String CUSTOMER = "C";
    }

    public class OFFERING_TYPE {
        public static final String NORMAL_OFFERING = "0";
        public static final String GOODS_OFFERING = "2";
        public static final String FWA_OFFERING = "FWA";
        public static final String ACCOUNT_BILL_OFFERING = "6";
    }

    public class STATUS {
        public static final String TO_BE_ACTIVATED = "1";
        public static final String ACTIVATED = "2";
        public static final String SUSPENDED = "4";
        public static final String PRE_DEREGISTERED = "8";
        public static final String UNSUBSCRIBED = "9";
    }


    private long offeringInstId;
    private String ownerEntityType;

    private long offeringId;
    private String offeringName;
    private String offeringDescription;

    private String primaryFlag;
    private String bundleFlag;

    private long pOfferingInstId;
    private String offeringType;
    private String contractFlag;
    private String status;

    private String effDate;
    private String expDate;



    public long getOfferingInstId() {
        return offeringInstId;
    }

    public void setOfferingInstId(long offeringInstId) {
        this.offeringInstId = offeringInstId;
    }

    public String getOwnerEntityType() {
        return ownerEntityType;
    }

    public void setOwnerEntityType(String ownerEntityType) {
        this.ownerEntityType = ownerEntityType;
    }

    public long getOfferingId() {
        return offeringId;
    }

    public void setOfferingId(long offeringId) {
        this.offeringId = offeringId;
    }

    public String getOfferingName() {
        return offeringName;
    }

    public void setOfferingName(String offeringName) {
        this.offeringName = offeringName;
    }

    public String getOfferingDescription() {
        return offeringDescription;
    }

    public void setOfferingDescription(String offeringDescription) {
        this.offeringDescription = offeringDescription;
    }

    public String getPrimaryFlag() {
        return primaryFlag;
    }

    public void setPrimaryFlag(String primaryFlag) {
        this.primaryFlag = primaryFlag;
    }

    public String getBundleFlag() {
        return bundleFlag;
    }

    public void setBundleFlag(String bundleFlag) {
        this.bundleFlag = bundleFlag;
    }

    public long getpOfferingInstId() {
        return pOfferingInstId;
    }

    public void setpOfferingInstId(long pOfferingInstId) {
        this.pOfferingInstId = pOfferingInstId;
    }

    public String getOfferingType() {
        return offeringType;
    }

    public void setOfferingType(String offeringType) {
        this.offeringType = offeringType;
    }

    public String getContractFlag() {
        return contractFlag;
    }

    public void setContractFlag(String contractFlag) {
        this.contractFlag = contractFlag;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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
