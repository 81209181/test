package com.hkt.btu.sd.facade;


import com.hkt.btu.sd.facade.data.EditResultData;
import com.hkt.btu.sd.facade.data.SdUserRoleData;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public interface SdUserRoleFacade {

    List<SdUserRoleData> listAllUserRole();

    SdUserRoleData getUserRoleByRoleId(String roleId);

    EditResultData getUserRoleByUserId (String userId);

    LinkedList<SdUserRoleData> getEligibleUserRoleList();

    HashMap<String, SdUserRoleData> getUserRoleMap(List<SdUserRoleData> userGroupDataList);

    String updateUserRole(String roleId, String roleDesc, String status);
}
