package com.hkt.btu.common.core.dao.entity;

import java.time.LocalDateTime;

public class BtuAutoRetryEntity extends BaseEntity {

    public static class STATUS {
        public static final String ACTIVE = "A";
        public static final String COMPLETED = "CP";
        public static final String CANCELLED = "X";
    }

    private Integer retryId;

    private String beanName;
    private String methodName;
    private String methodParam;

    private String status;
    private Integer tryCount;
    private Integer minWaitSecond;
    private LocalDateTime nextTargetTime;



    public Integer getRetryId() {
        return retryId;
    }

    public void setRetryId(Integer retryId) {
        this.retryId = retryId;
    }

    public String getBeanName() {
        return beanName;
    }

    public void setBeanName(String beanName) {
        this.beanName = beanName;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public String getMethodParam() {
        return methodParam;
    }

    public void setMethodParam(String methodParam) {
        this.methodParam = methodParam;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getTryCount() {
        return tryCount;
    }

    public void setTryCount(Integer tryCount) {
        this.tryCount = tryCount;
    }

    public Integer getMinWaitSecond() {
        return minWaitSecond;
    }

    public void setMinWaitSecond(Integer minWaitSecond) {
        this.minWaitSecond = minWaitSecond;
    }

    public LocalDateTime getNextTargetTime() {
        return nextTargetTime;
    }

    public void setNextTargetTime(LocalDateTime nextTargetTime) {
        this.nextTargetTime = nextTargetTime;
    }
}
