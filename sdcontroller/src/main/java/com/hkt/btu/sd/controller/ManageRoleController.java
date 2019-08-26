package com.hkt.btu.sd.controller;


import com.hkt.btu.sd.core.service.SdUserRoleService;
import com.hkt.btu.sd.facade.SdUserGroupFacade;
import com.hkt.btu.sd.facade.SdUserRoleFacade;
import com.hkt.btu.sd.facade.data.SdUserGroupData;
import com.hkt.btu.sd.facade.data.SdUserRoleData;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import java.util.List;

@SuppressWarnings("SameReturnValue")
@Controller
@RequestMapping(value = "/admin/manage-role")
public class ManageRoleController {

    @Resource(name = "userRoleFacade")
    SdUserRoleFacade userRoleFacade;

    @GetMapping("/list-user-role")
    public String listUserGrp() {
        return "admin/manageRole/listUserRole";
    }

    @GetMapping("/ajax-list-user-role")
    public ResponseEntity<?> ajaxListUserGrp() {
        List<SdUserRoleData> sdUserRoleDataList = userRoleFacade.listAllUserRole();
        return ResponseEntity.ok(sdUserRoleDataList);
    }

}
