package com.hkt.btu.noc.facade;

import com.hkt.btu.noc.facade.data.NocUserGroupData;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public interface NocUserGroupFacade {
    List<NocUserGroupData> listAllUserGroup();

    LinkedList<NocUserGroupData> getEligibleUserGroupList();
    HashMap<String, NocUserGroupData> getUserGroupMap(List<NocUserGroupData> userGroupDataList);
}
