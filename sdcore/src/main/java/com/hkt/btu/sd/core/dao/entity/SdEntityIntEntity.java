package com.hkt.btu.sd.core.dao.entity;

import com.hkt.btu.common.core.dao.entity.BaseEntity;

public class SdEntityIntEntity extends BaseEntity {

    private Integer attributeId;
    private Integer entityId;
    private Integer value;

    public SdEntityIntEntity(Integer attributeId, Integer entityId, Integer value) {
        this.attributeId = attributeId;
        this.entityId = entityId;
        this.value = value;
    }

    public static SdEntityIntEntity of(Integer attributeId, Integer entityId, Integer value) {
        return new SdEntityIntEntity(attributeId, entityId, value);
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

    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
    }
}
