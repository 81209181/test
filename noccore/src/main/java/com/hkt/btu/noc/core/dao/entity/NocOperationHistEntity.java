package com.hkt.btu.noc.core.dao.entity;

import com.hkt.btu.common.core.dao.entity.BaseEntity;


public class NocOperationHistEntity extends BaseEntity {

    public class ACCESS_REQUEST{
        public static final String ITEM_TYPE = "ACCESS_REQUEST";
        public static final String DETAIL_PATTERN_STATUS_CHANGE = "Status: %s --> %s";
    }

    private Integer logId;
    private String itemType;
    private String itemId;
    private String detail;

    private Integer userId;




    public Integer getLogId() {
        return logId;
    }

    public void setLogId(Integer logId) {
        this.logId = logId;
    }

    public String getItemType() {
        return itemType;
    }

    public void setItemType(String itemType) {
        this.itemType = itemType;
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }
}
