package com.hkt.btu.noc.core.service;

import com.hkt.btu.noc.core.exception.InsufficientAuthorityException;
import com.hkt.btu.noc.core.exception.InvalidInputException;
import com.hkt.btu.noc.core.service.bean.NocUserBean;
import com.hkt.btu.noc.core.service.bean.NocUserGroupBean;

import java.util.List;

public interface NocUserGroupService {
    List<NocUserGroupBean> getAllUserGroup();
    List<NocUserGroupBean> getEligibleUserGroupGrantList();
    boolean isEligibleToGrantUserGroup(List<String> groupIdList);

    void updateUserGroup(Integer userId, Boolean updateIsAdmin, Boolean updateIsUser,
                         Boolean updateIsCAdmin, Boolean updateIsCUser, NocUserBean modifier)
            throws InsufficientAuthorityException, InvalidInputException;
}
