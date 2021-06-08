package com.hkt.btu.sd.core.dao.entity;

import com.hkt.btu.common.core.dao.entity.BaseEntity;

public class SdEntityVacharEntity extends BaseEntity {

    private Integer attributeId;
    private Integer entityId;
    private String value;

    public SdEntityVacharEntity(Integer attributeId, Integer entityId, String value) {
        this.attributeId = attributeId;
        this.entityId = entityId;
        this.value = value;
    }

    public static SdEntityVacharEntity of(Integer attributeId, Integer entityId, String value) {
        return new SdEntityVacharEntity(attributeId, entityId, value);
    }

    public Integer getAttributeId() {
        return attributeId;
    }

    public void setAttributeId(Integer attributeId) {
        this.attributeId = attributeId;
    }

    public Integer getEntityId() {
        return entityId;
    }

    public void setEntityId(Integer entityId) {
        this.entityId = entityId;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
