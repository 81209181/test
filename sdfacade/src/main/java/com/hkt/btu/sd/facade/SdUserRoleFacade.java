package com.hkt.btu.sd.facade;


import com.hkt.btu.sd.facade.data.EditResultData;
import com.hkt.btu.sd.facade.data.SdUserOwnerAuthRoleData;
import com.hkt.btu.sd.facade.data.SdUserPathCtrlData;
import com.hkt.btu.sd.facade.data.SdUserRoleData;
import com.hkt.btu.sd.facade.data.SdUserRoleWorkgroupData;

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

    List<SdUserRoleData> getCurrentUserUserRole();

    List<SdUserRoleData> getAbstracParenttRole(String roleId);

    List<SdUserPathCtrlData> getActivePathCtrl();

    String createUserRolePathCtrl(String roleId, List<Integer> pathCtrlIdList);

    String delUserRolePathCtrl(String roleId, int pathCtrlId);

    List<SdUserRoleData> getRole4UserOwnerAuth();

    List<SdUserOwnerAuthRoleData> getUserOwnerAuthRoleList();

    String createUserOwnerAuthRole(String ownerId, String authRoleId, String serviceTypeCode);

    String delUserOwnerAuthRole(String ownerId, String authRoleId, String serviceTypeCode);

    List<SdUserRoleWorkgroupData> getUserRoleWorkgroupList();

    String delUserRoleWorkgroup(String roleId, String workgroup);

    String createUserRoleWorkgroup(String roleId, String workgroup);

    List<String> getWorkgroupList();
}
