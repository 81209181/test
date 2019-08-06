package com.hkt.btu.sd.core.service;

import com.hkt.btu.sd.core.exception.InsufficientAuthorityException;
import com.hkt.btu.sd.core.exception.InvalidInputException;
import com.hkt.btu.sd.core.service.bean.SdUserBean;
import com.hkt.btu.sd.core.service.bean.SdUserGroupBean;

import java.util.List;

public interface SdUserGroupService {
    List<SdUserGroupBean> getAllUserGroup();
    List<SdUserGroupBean> getEligibleUserGroupGrantList();
    boolean isEligibleToGrantUserGroup(List<String> groupIdList);

    void updateUserGroup(Integer userId, Boolean updateIsAdmin, Boolean updateIsUser,
                         Boolean updateIsCAdmin, Boolean updateIsCUser, SdUserBean modifier)
            throws InsufficientAuthorityException, InvalidInputException;
}
