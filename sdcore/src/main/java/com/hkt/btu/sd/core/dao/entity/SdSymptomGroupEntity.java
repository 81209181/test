package com.hkt.btu.sd.core.dao.entity;

import com.hkt.btu.common.core.dao.entity.BaseEntity;

import java.util.List;

public class SdSymptomGroupEntity extends BaseEntity {

    private String symptomGroupCode;
    private String symptomGroupName;
    private List<SdSymptomGroupRoleMappingEntity> roleList;

    public String getSymptomGroupCode() {
        return symptomGroupCode;
    }

    public void setSymptomGroupCode(String symptomGroupCode) {
        this.symptomGroupCode = symptomGroupCode;
    }

    public String getSymptomGroupName() {
        return symptomGroupName;
    }

    public void setSymptomGroupName(String symptomGroupName) {
        this.symptomGroupName = symptomGroupName;
    }

    public List<SdSymptomGroupRoleMappingEntity> getRoleList() {
        return roleList;
    }

    public void setRoleList(List<SdSymptomGroupRoleMappingEntity> roleList) {
        this.roleList = roleList;
    }
}
