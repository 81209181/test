package com.hkt.btu.sd.controller;


import com.hkt.btu.sd.controller.response.SimpleAjaxResponse;
import com.hkt.btu.sd.facade.SdUserRoleFacade;
import com.hkt.btu.sd.facade.data.SdUserPathCtrlData;
import com.hkt.btu.sd.facade.data.SdUserRoleData;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/edit-user-role")
    public String editUserRole(@RequestParam String roleId, Model model) {
        if (StringUtils.isNotEmpty(roleId)) {
            model.addAttribute("roleId", roleId);
        }
        return "admin/manageRole/editUserRole";
    }

    @GetMapping("/edit-user-role/getUserRole")
    public ResponseEntity<?> getUserRole(@RequestParam String roleId) {
        if (StringUtils.isEmpty(roleId)) {
            return ResponseEntity.badRequest().body("Empty role id!");
        }

        SdUserRoleData userRoleData = userRoleFacade.getUserRoleByRoleId(roleId);

        if (userRoleData == null) {
            return ResponseEntity.badRequest().body("Cannot load User Role.");
        } else {
            return ResponseEntity.ok(userRoleData);
        }
    }

    @GetMapping("/edit-user-role/getParentRolePathByRoleId")
    public ResponseEntity<?> getParentRolePathByRoleId(@RequestParam String roleId) {
        if (StringUtils.isEmpty(roleId)) {
            return ResponseEntity.badRequest().body("Empty role id!");
        }

        List<SdUserPathCtrlData> UserPathCtrlData = userRoleFacade.getParentRolePathByRoleId(roleId);
        return ResponseEntity.ok(UserPathCtrlData);
    }

    @PostMapping("/edit-user-role")
    public ResponseEntity<?> editUserForm(@RequestParam String roleId, @RequestParam String roleDesc, @RequestParam String status) {
        String errorMsg = userRoleFacade.updateUserRole(roleId, roleDesc, status);
        if(errorMsg==null){
            return ResponseEntity.ok(SimpleAjaxResponse.of());
        }else {
            return ResponseEntity.ok(SimpleAjaxResponse.of(false, errorMsg));
        }
    }
}
