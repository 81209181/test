package com.hkt.btu.sd.facade;


import com.hkt.btu.sd.facade.data.EditResultData;
import com.hkt.btu.sd.facade.data.SdUserPathCtrlData;
import com.hkt.btu.sd.facade.data.SdUserRoleData;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public interface SdUserRoleFacade {

    String updateUserRole(String roleId, String roleDesc, String status, String isAbstract);

    List<SdUserRoleData> listAllUserRole();

    SdUserRoleData getUserRoleByRoleId(String roleId);
    List<SdUserPathCtrlData> getParentRolePathByRoleId(String roleId);
    EditResultData getUserRoleByUserId (String userId);

    HashMap<String, SdUserRoleData> getUserRoleMap(List<SdUserRoleData> userGroupDataList);

    LinkedList<SdUserRoleData> getEligibleUserRoleList();

    boolean checkSameTeamRole(String name, String createBy);
}
