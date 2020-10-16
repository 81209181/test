package com.hkt.btu.sd.facade;


import com.hkt.btu.sd.facade.data.EditResultData;
import com.hkt.btu.sd.facade.data.SdUserPathCtrlData;
import com.hkt.btu.sd.facade.data.SdUserRoleData;

import java.util.List;

public interface SdUserRoleFacade {

    String updateUserRole(String roleId, String roleDesc, String status, String isAbstract);

    List<SdUserRoleData> listAllUserRole();

    SdUserRoleData getUserRoleByRoleId(String roleId);
    List<SdUserPathCtrlData> getParentRolePathByRoleId(String roleId);
    EditResultData getUserRoleByUserId (String userId, Boolean checkTeamHead);

    List<SdUserRoleData> filterUserRoleList(List<SdUserRoleData> roleList);

    List<SdUserRoleData> getEligibleUserRoleList();

    boolean checkSameTeamRole(String name, String createBy);

    void reloadUserRole();

    List<SdUserRoleData> getTeamHeadRoleList();

    List<List<Object>> getRole4Chart();
}
