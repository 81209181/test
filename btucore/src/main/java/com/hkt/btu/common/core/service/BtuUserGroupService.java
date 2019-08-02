package com.hkt.btu.common.core.service;

import com.hkt.btu.common.core.exception.InsufficientAuthorityException;
import com.hkt.btu.common.core.exception.InvalidInputException;
import com.hkt.btu.common.core.service.bean.BtuUserBean;
import com.hkt.btu.common.core.service.bean.BtuUserGroupBean;

import java.util.List;

public interface BtuUserGroupService {
    List<BtuUserGroupBean> getAllUserGroup();
    List<BtuUserGroupBean> getEligibleUserGroupGrantList();
    boolean isEligibleToGrantUserGroup(List<String> groupIdList);

    void updateUserGroup(Integer userId, Boolean updateIsAdmin, Boolean updateIsUser,
                         Boolean updateIsCAdmin, Boolean updateIsCUser, BtuUserBean modifier)
            throws InsufficientAuthorityException, InvalidInputException;
}
