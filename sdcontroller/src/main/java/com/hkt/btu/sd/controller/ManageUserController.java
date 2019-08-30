package com.hkt.btu.sd.controller;

import com.hkt.btu.common.facade.data.PageData;
import com.hkt.btu.sd.controller.constant.CreateUserPathEnum;
import com.hkt.btu.sd.controller.response.SimpleAjaxResponse;
import com.hkt.btu.sd.controller.response.helper.ResponseEntityHelper;
import com.hkt.btu.sd.facade.SdAuditTrailFacade;
import com.hkt.btu.sd.facade.SdUserFacade;
import com.hkt.btu.sd.facade.SdUserRoleFacade;
import com.hkt.btu.sd.facade.data.*;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

@SuppressWarnings({"ParameterCanBeLocal", "SameReturnValue"})
@Controller
@RequestMapping(value = "/admin/manage-user")
public class ManageUserController {

    @Resource(name = "userFacade")
    SdUserFacade userFacade;
    @Resource(name = "userRoleFacade")
    SdUserRoleFacade userRoleFacade;
    @Resource(name = "auditTrailFacade")
    SdAuditTrailFacade sdAuditTrailFacade;

    @GetMapping({"create-ldap-user", "create-user", "create-non-pccw-hkt-user"})
    public String createUserForm(final Model model,
                                 final HttpServletRequest request,
                                 @ModelAttribute("createUserFormData") CreateUserFormData createUserFormData,
                                 @ModelAttribute("userRoleOptionDataMap") HashMap<String, SdUserRoleData> userRoleOptionDataMap) {
        // user role info
        List<SdUserRoleData> userRoleDataList = userRoleFacade.getEligibleUserRoleList();
        userRoleOptionDataMap = userRoleFacade.getUserRoleMap(userRoleDataList);
        if (!MapUtils.isEmpty(userRoleOptionDataMap)) {
            model.addAttribute("userRoleOptionDataMap", userRoleOptionDataMap);
        }

        String servletPath = request.getServletPath();
        return CreateUserPathEnum.getValue(servletPath);
    }


    @PostMapping("/create-pccw-hkt-user")
    public String createUserForm(final RedirectAttributes redirectAttributes,
                                 @ModelAttribute("inputUserData") CreateUserFormData createUserFormData) {
        CreateResultData createResultData = userFacade.createUser(createUserFormData);
        String newUserId = createResultData == null ? null : createResultData.getNewId();
        String errorMsg = createResultData == null ? "No create result." : createResultData.getErrorMsg();
        if (newUserId == null) {
            redirectAttributes.addFlashAttribute(PageMsgController.ERROR_MSG, errorMsg);
            redirectAttributes.addFlashAttribute("createUserFormData", createUserFormData);
            return "redirect:create-user";
        } else {
            String passwordMsg = null;
            if (createResultData != null) {
                passwordMsg = StringUtils.isNotEmpty(createResultData.getPasswordMsg()) ?
                        "Create User.OTP is " + createResultData.getPasswordMsg() : "Create User.";
            }
            redirectAttributes.addFlashAttribute(PageMsgController.INFO_MSG, passwordMsg);
            return "redirect:edit-user?userId=" + newUserId;
        }
    }

    @PostMapping("/create-non-pccw-hkt-user")
    public String createNonPccwOrHktUser(final RedirectAttributes redirectAttributes,
                                         @ModelAttribute("inputUserData") CreateUserFormData createUserFormData) {
        CreateResultData createResultData = userFacade.createNonPccwHktUser(createUserFormData);
        String newUserId = createResultData == null ? null : createResultData.getNewId();
        String errorMsg = createResultData == null ? "No create result." : createResultData.getErrorMsg();
        if (newUserId == null) {
            redirectAttributes.addFlashAttribute(PageMsgController.ERROR_MSG, errorMsg);
            redirectAttributes.addFlashAttribute("createUserFormData", createUserFormData);
            return "redirect:create-non-pccw-hkt-user";
        } else {
            String passwordMsg = null;
            if (createResultData != null) {
                passwordMsg = StringUtils.isNotEmpty(createResultData.getPasswordMsg()) ?
                        "Create User.OTP is " + createResultData.getPasswordMsg() : "Create User.";
            }
            redirectAttributes.addFlashAttribute(PageMsgController.INFO_MSG, passwordMsg);
            return "redirect:edit-user?userId=" + newUserId;
        }
    }

    @PostMapping("/create-ldap-user")
    public String createLdapUser(final RedirectAttributes redirectAttributes,
                                 @ModelAttribute("inputUserData") CreateUserFormData createUserFormData) {
        CreateResultData createResultData = userFacade.createLdapUser(createUserFormData);
        String newUserId = createResultData == null ? null : createResultData.getNewId();
        String errorMsg = createResultData == null ? "No create result." : createResultData.getErrorMsg();
        if (newUserId == null) {
            redirectAttributes.addFlashAttribute(PageMsgController.ERROR_MSG, errorMsg);
            redirectAttributes.addFlashAttribute("createUserFormData", createUserFormData);
            return "redirect:create-ldap-user";
        } else {
            redirectAttributes.addFlashAttribute(PageMsgController.INFO_MSG, "Created user.");
            return "redirect:edit-user?userId=" + newUserId;
        }
    }

    @GetMapping("/edit-user")
    public String editUserForm(final Model model,
                               @RequestParam String userId,
                               @ModelAttribute("editUserId") String editUserId,
                               @ModelAttribute("userRoleDataMap") HashMap<String, SdUserRoleData> userRoleDataMap) {
        if (userId != null) {
            model.addAttribute("editUserId", userId);
            List<String> userRole = userRoleFacade.getUserRoleByUserId(userId);
            if (CollectionUtils.isNotEmpty(userRole)) {
                model.addAttribute("userRoleList", userRole);
            }
        }

        // user group info
        List<SdUserRoleData> userRoleDataList = userRoleFacade.getEligibleUserRoleList();
        userRoleDataMap = userRoleFacade.getUserRoleMap(userRoleDataList);
        if (!MapUtils.isEmpty(userRoleDataMap)) {
            model.addAttribute("userRoleOptionDataMap", userRoleDataMap);
        }

        return "admin/manageUser/editUserForm";
    }

    @GetMapping("/edit-user/changeUserType/{userType}")
    public String changeUserType(final Model model,
                                 String userId,
                                 @PathVariable("userType") String userType,
                                 @ModelAttribute("changeUserTypeFormData") ChangeUserTypeFormData changeUserTypeFormData) {
        model.addAttribute("oldUserId", userId);
        if (userType.equals(ChangeUserTypeFormData.PCCW_HKT_USER)) {
            return "/admin/manageUser/changeUserType/updateUserTypeToPccwOrHktUser";
        } else if (userType.equals(ChangeUserTypeFormData.NON_PCCW_HKT_USER)) {

        } else if (userType.equals(ChangeUserTypeFormData.LDAP_USER)) {

        }
        return null;
    }

    @GetMapping("/edit-user/get-user")
    public ResponseEntity<?> getUserById(@RequestParam String userId) {
        if (userId == null) {
            return ResponseEntity.badRequest().body("Empty user id!");
        }

        SdUserData sdUserData = userFacade.getUserByUserId(userId);
        if (sdUserData == null) {
            return ResponseEntity.badRequest().body("User not found.");
        } else {
            sdAuditTrailFacade.insertViewUserAuditTrail(sdUserData);
            return ResponseEntity.ok(sdUserData);
        }
    }

    @PostMapping("/edit-user")
    public ResponseEntity<?> editUserForm(@RequestBody UpdateUserFormData updateUserFormData) {
        String errorMsg = userFacade.updateUser(updateUserFormData);
        if (errorMsg == null) {
            return ResponseEntity.ok(SimpleAjaxResponse.of());
        } else {
            return ResponseEntity.ok(SimpleAjaxResponse.of(false, errorMsg));
        }
    }

    @PostMapping("/edit-user/activate")
    public ResponseEntity<?> activateUser(@RequestParam String userId) {
        String errorMsg = userFacade.activateUser(userId);
        if (errorMsg == null) {
            return ResponseEntity.ok(SimpleAjaxResponse.of());
        } else {
            return ResponseEntity.ok(SimpleAjaxResponse.of(false, errorMsg));
        }
    }

    @PostMapping("/edit-user/deactivate")
    public ResponseEntity<?> deactivateUser(@RequestParam String userId) {
        String errorMsg = userFacade.deactivateUser(userId);
        if (errorMsg == null) {
            return ResponseEntity.ok(SimpleAjaxResponse.of());
        } else {
            return ResponseEntity.ok(SimpleAjaxResponse.of(false, errorMsg));
        }
    }

    @GetMapping("/search-user")
    public String searchUser(final Model model,
                             @ModelAttribute("userGroupOptionList") LinkedList<SdUserGroupData> userGroupOptionList) {
        //userGroupOptionList = sdUserGroupFacade.getEligibleUserGroupList();
        if (!CollectionUtils.isEmpty(userGroupOptionList)) {
            model.addAttribute("userGroupOptionList", userGroupOptionList);
        }

        return "admin/manageUser/searchUser";
    }

    @GetMapping("/ajax-search-user")
    public ResponseEntity<?> ajaxSearchUser(
            @RequestParam(defaultValue = "0") int draw,
            @RequestParam(defaultValue = "0") int start,
            @RequestParam(defaultValue = "10") int length,
            @RequestParam(required = false) String userId,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String email) {
        int page = start / length;
        Pageable pageable = PageRequest.of(page, length);

        PageData<SdUserData> pageData = userFacade.searchUser(pageable, userId, email, name);
        return ResponseEntityHelper.buildDataTablesResponse(draw, pageData);
    }


}
