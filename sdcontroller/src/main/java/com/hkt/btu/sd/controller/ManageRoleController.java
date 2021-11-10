package com.hkt.btu.sd.controller;


import com.hkt.btu.sd.controller.response.SimpleAjaxResponse;
import com.hkt.btu.sd.facade.SdServiceTypeFacade;
import com.hkt.btu.sd.facade.SdUserRoleFacade;
import com.hkt.btu.sd.facade.data.SdServiceTypeData;
import com.hkt.btu.sd.facade.data.SdUserOwnerAuthRoleData;
import com.hkt.btu.sd.facade.data.SdUserPathCtrlData;
import com.hkt.btu.sd.facade.data.SdUserRoleData;
import com.hkt.btu.sd.facade.data.SdUserRoleWorkgroupData;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;
import java.util.List;

@SuppressWarnings("SameReturnValue")
@Controller
@RequestMapping(value = "/admin/manage-role")
public class ManageRoleController {

    @Resource(name = "userRoleFacade")
    SdUserRoleFacade userRoleFacade;

    @Resource(name = "serviceTypeFacade")
    SdServiceTypeFacade serviceTypeFacade;

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
        List<SdUserRoleData> abstractRoleList = null;
        List<SdUserPathCtrlData> activePathCtrlList = null;

        if (StringUtils.isNotEmpty(roleId)) {
            model.addAttribute("roleId", roleId);
            abstractRoleList = userRoleFacade.getAbstracParenttRole(roleId);
            activePathCtrlList = userRoleFacade.getActivePathCtrl();
        }
        if (CollectionUtils.isNotEmpty(abstractRoleList)) {
            model.addAttribute("abstractRoleList", abstractRoleList);
        }
        if (CollectionUtils.isNotEmpty(activePathCtrlList)) {
            model.addAttribute("activePathCtrlList", activePathCtrlList);
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
    public ResponseEntity<?> editUserForm(@RequestParam String roleId, @RequestParam String roleDesc,
                                          @RequestParam String status, @RequestParam String abstractFlag) {
        String errorMsg = userRoleFacade.updateUserRole(roleId, roleDesc, status, abstractFlag);
        if(errorMsg==null){
            return ResponseEntity.ok(SimpleAjaxResponse.of());
        }else {
            return ResponseEntity.ok(SimpleAjaxResponse.of(false, errorMsg));
        }
    }

    @GetMapping("/reloadRole")
    public ResponseEntity<?> reloadUserRole() {
        userRoleFacade.reloadUserRole();
        return ResponseEntity.ok("done.");
    }

    @GetMapping("getRole4Chart")
    public ResponseEntity<?> getRole4Chart() {
        try {
            return ResponseEntity.ok(userRoleFacade.getRole4Chart());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/edit-user-role/createUserRoleCtrl")
    public ResponseEntity<?> createUserRolePathCtrl(@RequestParam String roleId, @RequestParam List<Integer> pathCtrlIdList) {
        String errMsg = userRoleFacade.createUserRolePathCtrl(roleId, pathCtrlIdList);
        if (errMsg != null) {
            return ResponseEntity.ok(SimpleAjaxResponse.of(false, errMsg));
        }
        return ResponseEntity.ok(SimpleAjaxResponse.of());
    }

    @PostMapping("/edit-user-role/delUserRoleCtrl")
    public ResponseEntity<?> delUserRolePathCtrl(@RequestParam String roleId, @RequestParam int pathCtrlId) {
        String errMsg = userRoleFacade.delUserRolePathCtrl(roleId, pathCtrlId);
        if (null != errMsg) {
            return ResponseEntity.ok(SimpleAjaxResponse.of(false, errMsg));
        }
        return ResponseEntity.ok(SimpleAjaxResponse.of());
    }
}
