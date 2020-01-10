package com.hkt.btu.common.facade.data;


import java.time.LocalDateTime;

public class BtuCacheProfileData implements DataInterface {

    private String cacheName;
    private String originServiceBeanName;
    private String originServiceMethodName;

    private int loadingPriority;
    private boolean lazyInit;
    private boolean sensitive;

    private LocalDateTime createdate;
    private String createby;
    private LocalDateTime modifydate;
    private String modifyby;



    public String getCacheName() {
        return cacheName;
    }

    public void setCacheName(String cacheName) {
        this.cacheName = cacheName;
    }

    public String getOriginServiceBeanName() {
        return originServiceBeanName;
    }

    public void setOriginServiceBeanName(String originServiceBeanName) {
        this.originServiceBeanName = originServiceBeanName;
    }

    public String getOriginServiceMethodName() {
        return originServiceMethodName;
    }

    public void setOriginServiceMethodName(String originServiceMethodName) {
        this.originServiceMethodName = originServiceMethodName;
    }

    public int getLoadingPriority() {
        return loadingPriority;
    }

    public void setLoadingPriority(int loadingPriority) {
        this.loadingPriority = loadingPriority;
    }

    public boolean isLazyInit() {
        return lazyInit;
    }

    public void setLazyInit(boolean lazyInit) {
        this.lazyInit = lazyInit;
    }

    public boolean isSensitive() {
        return sensitive;
    }

    public void setSensitive(boolean sensitive) {
        this.sensitive = sensitive;
    }

    public LocalDateTime getCreatedate() {
        return createdate;
    }

    public void setCreatedate(LocalDateTime createdate) {
        this.createdate = createdate;
    }

    public String getCreateby() {
        return createby;
    }

    public void setCreateby(String createby) {
        this.createby = createby;
    }

    public LocalDateTime getModifydate() {
        return modifydate;
    }

    public void setModifydate(LocalDateTime modifydate) {
        this.modifydate = modifydate;
    }

    public String getModifyby() {
        return modifyby;
    }

    public void setModifyby(String modifyby) {
        this.modifyby = modifyby;
    }
}
