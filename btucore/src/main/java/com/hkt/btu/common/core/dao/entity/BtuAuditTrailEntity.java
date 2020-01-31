package com.hkt.btu.common.core.dao.entity;

public class BtuAuditTrailEntity extends BaseEntity {
    public static class ACTION{
        public static final String LOGIN = "LOGIN";
        public static final String LOGOUT = "LOGOUT";
        public static final String KICK = "KICK";
        public static final String VIEW_USER = "VIEW_USER";
        public static final String VIEW_API_AUTH = "VIEW_API_AUTH";
        public static final String REGEN_API_AUTH = "REGEN_API_AUTH";
        public static final String REPORT = "REPORT_DOWNLOAD";
    }

    private Integer auditId;
    private Integer uid;
    private String action;
    private String detail;


    public Integer getAuditId() {
        return auditId;
    }

    public void setAuditId(Integer auditId) {
        this.auditId = auditId;
    }

    public Integer getUid() {
        return uid;
    }

    public void setUid(Integer uid) {
        this.uid = uid;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }
}
