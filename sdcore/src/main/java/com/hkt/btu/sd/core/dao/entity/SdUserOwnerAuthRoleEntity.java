package com.hkt.btu.sd.core.dao.entity;

import java.time.LocalDateTime;

public class SdUserOwnerAuthRoleEntity {

    private String ownerId;

    private String authRoleId;

    private LocalDateTime createDate;

    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    public String getAuthRoleId() {
        return authRoleId;
    }

    public void setAuthRoleId(String authRoleId) {
        this.authRoleId = authRoleId;
    }

    public LocalDateTime getCreateDate() {
        return createDate;
    }

    public void setCreateDate(LocalDateTime createDate) {
        this.createDate = createDate;
    }


}
