package com.hkt.btu.common.core.service.bean;

import java.lang.reflect.Method;

public class BtuCacheInfoBean extends BaseBean{
    private String cacheId;
    private Class objectType;
    private Class loadingClass;
    private Method loadingMethod;

    public String getCacheId() {
        return cacheId;
    }

    public void setCacheId(String cacheId) {
        this.cacheId = cacheId;
    }

    public Class getObjectType() {
        return objectType;
    }

    public void setObjectType(Class objectType) {
        this.objectType = objectType;
    }

    public Class getLoadingClass() {
        return loadingClass;
    }

    public void setLoadingClass(Class loadingClass) {
        this.loadingClass = loadingClass;
    }

    public Method getLoadingMethod() {
        return loadingMethod;
    }

    public void setLoadingMethod(Method loadingMethod) {
        this.loadingMethod = loadingMethod;
    }
}
