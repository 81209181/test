package com.hkt.btu.sd.facade.data;

import com.hkt.btu.common.facade.data.DataInterface;


public class UpdateUserFormData implements DataInterface {
    private Integer userId;
    private String name;
    private String mobile;
    private String staffId;

    private Boolean userGroupAdmin;
    private Boolean userGroupUser;
    private Boolean userGroupCAdmin;
    private Boolean userGroupCUser;




    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getStaffId() {
        return staffId;
    }

    public void setStaffId(String staffId) {
        this.staffId = staffId;
    }

    public Boolean isUserGroupAdmin() {
        return userGroupAdmin;
    }

    public void setUserGroupAdmin(Boolean userGroupAdmin) {
        this.userGroupAdmin = userGroupAdmin;
    }

    public Boolean isUserGroupUser() {
        return userGroupUser;
    }

    public void setUserGroupUser(Boolean userGroupUser) {
        this.userGroupUser = userGroupUser;
    }

    public Boolean isUserGroupCAdmin() {
        return userGroupCAdmin;
    }

    public void setUserGroupCAdmin(Boolean userGroupCAdmin) {
        this.userGroupCAdmin = userGroupCAdmin;
    }

    public Boolean isUserGroupCUser() {
        return userGroupCUser;
    }

    public void setUserGroupCUser(Boolean userGroupCUser) {
        this.userGroupCUser = userGroupCUser;
    }
}
