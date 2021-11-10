package com.hkt.btu.common.facade.data;


import java.time.LocalDateTime;

public class BtuCacheProfileData implements DataInterface {

    private String cacheName;
    private String originServiceBeanName;
    private String originServiceMethodName;

    private int loadingPriority;
    private boolean lazyInit;
    private boolean sensitive;

    private LocalDateTime createDate;
    private String createBy;
    private LocalDateTime modifyDate;
    private String modifyBy;



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

    @Deprecated
    public LocalDateTime getCreatedate() {
        return createDate;
    }

    @Deprecated
    public void setCreatedate(LocalDateTime createDate) {
        this.createDate = createDate;
    }

    @Deprecated
    public String getCreateby() {
        return createBy;
    }

    @Deprecated
    public void setCreateby(String createBy) {
        this.createBy = createBy;
    }

    @Deprecated
    public LocalDateTime getModifydate() {
        return modifyDate;
    }

    @Deprecated
    public void setModifydate(LocalDateTime modifyDate) {
        this.modifyDate = modifyDate;
    }

    @Deprecated
    public String getModifyby() {
        return modifyBy;
    }

    @Deprecated
    public void setModifyby(String modifyBy) {
        this.modifyBy = modifyBy;
    }

    public LocalDateTime getCreateDate() {
        return createDate;
    }

    public void setCreateDate(LocalDateTime createDate) {
        this.createDate = createDate;
    }

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    public LocalDateTime getModifyDate() {
        return modifyDate;
    }

    public void setModifyDate(LocalDateTime modifyDate) {
        this.modifyDate = modifyDate;
    }

    public String getModifyBy() {
        return modifyBy;
    }

    public void setModifyBy(String modifyBy) {
        this.modifyBy = modifyBy;
    }
}
