package com.hkt.btu.sd.facade;

import com.hkt.btu.sd.facade.data.SdUserGroupData;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public interface SdUserGroupFacade {
    List<SdUserGroupData> listAllUserGroup();

    LinkedList<SdUserGroupData> getEligibleUserGroupList();
    HashMap<String, SdUserGroupData> getUserGroupMap(List<SdUserGroupData> userGroupDataList);
}
