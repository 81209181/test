package com.hkt.btu.common.core.dao.entity;

public class BtuUserEntity extends BaseEntity {
    public static class SYSTEM{
        public static final String USER_ID = "SYSTEM";
    }

    public static class STATUS{
        public static final String ACTIVE = "A";
        public static final String LOCKED = "L";
        public static final String DISABLE = "D";
    }
}
