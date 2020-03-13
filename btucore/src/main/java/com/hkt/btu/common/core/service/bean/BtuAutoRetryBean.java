package com.hkt.btu.common.core.service.bean;


import java.lang.reflect.Method;
import java.time.LocalDateTime;

public class BtuAutoRetryBean extends BaseBean {

    public static class STATUS {
        public static final String ACTIVE = "Active";
        public static final String COMPLETED = "Completed";
        public static final String CANCELLED = "Cancelled";
    }

    private Integer retryId;

    private String clazzName;
    private String methodName;
    private String methodParam;

    private Class clazz;
    private Method method;
    private Object [] paramArray;

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

    public String getClazzName() {
        return clazzName;
    }

    public void setClazzName(String clazzName) {
        this.clazzName = clazzName;
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

    public Class getClazz() {
        return clazz;
    }

    public void setClazz(Class clazz) {
        this.clazz = clazz;
    }

    public Method getMethod() {
        return method;
    }

    public void setMethod(Method method) {
        this.method = method;
    }

    public Object[] getParamArray() {
        return paramArray;
    }

    public void setParamArray(Object[] paramArray) {
        this.paramArray = paramArray;
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
