package com.hkt.btu.sd.core.dao.entity;


import com.hkt.btu.common.core.dao.entity.BtuAuditTrailEntity;

public class SdAuditTrailEntity extends BtuAuditTrailEntity {
    public static class ACTION {
        public static final String VIEW_TICKET = "VIEW_TICKET";

        public static final String GET_NGN3_ADMIN_ACCOUNT = "NGN3_ADMIN";
        public static final String RESET_NGN3_ACCOUNT = "NGN3_ACCT";
    }


}
