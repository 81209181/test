package com.hkt.btu.sd.core.service.bean;

import com.hkt.btu.common.core.service.bean.BaseBean;

import java.time.LocalDateTime;
import java.util.List;

public class SdSymptomGroupBean extends BaseBean {
    private String symptomGroupCode;
    private String symptomGroupName;
    private List<String> roleList;
    private String symptomCodePrefix;

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

    public List<String> getRoleList() {
        return roleList;
    }

    public void setRoleList(List<String> roleList) {
        this.roleList = roleList;
    }

    public String getSymptomCodePrefix() {
        return symptomCodePrefix;
    }

    public void setSymptomCodePrefix(String symptomCodePrefix) {
        this.symptomCodePrefix = symptomCodePrefix;
    }
}
