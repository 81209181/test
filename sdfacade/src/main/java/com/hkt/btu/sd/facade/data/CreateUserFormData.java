package com.hkt.btu.sd.facade.data;

import com.hkt.btu.common.facade.data.DataInterface;

import java.util.List;

public class CreateUserFormData implements DataInterface {

    private String name;
    private String status;
    private String mobile;
    private String email;

    private String staffId;

    private Integer companyId;
    private List<String> userGroupIdList;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Integer companyId) {
        this.companyId = companyId;
    }

    public String getStaffId() {
        return staffId;
    }

    public void setStaffId(String staffId) {
        this.staffId = staffId;
    }

    public List<String> getUserGroupIdList() {
        return userGroupIdList;
    }

    public void setUserGroupIdList(List<String> userGroupIdList) {
        this.userGroupIdList = userGroupIdList;
    }
}
