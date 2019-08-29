package com.hkt.btu.sd.facade;


import com.hkt.btu.sd.facade.data.SdUserRoleData;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public interface SdUserRoleFacade {

    List<SdUserRoleData> listAllUserRole();

    SdUserRoleData getUserRoleByRoleId(String roleId);

    List<String> getUserRoleByUserId (String userId);

    LinkedList<SdUserRoleData> getEligibleUserRoleList();

    HashMap<String, SdUserRoleData> getUserRoleMap(List<SdUserRoleData> userGroupDataList);

    Boolean updateUserRole(String roleId, String roleDesc, String status);
}
