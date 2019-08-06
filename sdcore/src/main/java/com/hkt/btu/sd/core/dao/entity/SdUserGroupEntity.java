package com.hkt.btu.sd.core.dao.entity;

import com.hkt.btu.common.core.dao.entity.BaseEntity;

public class SdUserGroupEntity extends BaseEntity {


    public static class GROUP_ID{
        public static final String ROOT = "ROOT";
        public static final String ADMIN = "ADMIN";
        public static final String USER = "USER";
        public static final String C_ADMIN = "C_ADMIN";
        public static final String C_USER = "C_USER";
    }


    private String groupId;
    private String groupName;
    private String groupDesc;
    private String parentGroup;



    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getGroupDesc() {
        return groupDesc;
    }

    public void setGroupDesc(String groupDesc) {
        this.groupDesc = groupDesc;
    }

    public String getParentGroup() {
        return parentGroup;
    }

    public void setParentGroup(String parentGroup) {
        this.parentGroup = parentGroup;
    }
}
