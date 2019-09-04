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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.session.SessionInformation;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Collection;
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

    @Autowired
    SessionRegistry sessionRegistry;

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
                               final RedirectAttributes redirectAttributes,
                               @ModelAttribute("editUserId") String editUserId,
                               @ModelAttribute("userRoleDataMap") HashMap<String, SdUserRoleData> userRoleDataMap) {
        if (userId != null) {
            model.addAttribute("editUserId", userId);
            EditResultData result = userRoleFacade.getUserRoleByUserId(userId);
            String errorMsg = result == null ? "" : result.getErrorMsg();
            List<String> userRole = result == null ? null : (List<String>) result.getList();
            if (CollectionUtils.isNotEmpty(userRole)) {
                model.addAttribute("userRoleList", userRole);
            } else {
                redirectAttributes.addFlashAttribute(PageMsgController.ERROR_MSG, errorMsg);
                return "redirect:search-user";
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
        if (ChangeUserTypeFormData.PCCW_HKT_USER.equals(userType)) {
            return "/admin/manageUser/changeUserType/updateUserTypeToPccwOrHktUser";
        } else if (ChangeUserTypeFormData.LDAP_USER.equals(userType)) {
            return "/admin/manageUser/changeUserType/updateUserTypeToLdapUser";
        }
        return null;
    }


    @PostMapping("/changeUserType/pccw-hkt-user")
    public String changeUserType(final RedirectAttributes redirectAttributes,
                                 @ModelAttribute("changeUserTypeFormData") ChangeUserTypeFormData changeUserTypeFormData) {
        // Change User to  PCCW_HKT_USER
        ChangeUserTypeResultData changeUserTypeResultData = userFacade.changeUserTypeToPccwOrHktUser(changeUserTypeFormData);
        String newUserId = changeUserTypeResultData == null ? null : changeUserTypeResultData.getUserId();
        String errorMsg = changeUserTypeResultData == null ? "No create result." : changeUserTypeResultData.getErrorMsg();
        if (newUserId == null) {
            redirectAttributes.addFlashAttribute(PageMsgController.ERROR_MSG, errorMsg);
            redirectAttributes.addFlashAttribute("createUserFormData", changeUserTypeResultData);
            return "redirect:/admin/manage-user/edit-user/changeUserType/pccw-hkt-user?userId=" + changeUserTypeFormData.getUserId();
        } else {
            redirectAttributes.addFlashAttribute(PageMsgController.INFO_MSG, "Change user.");
            return "redirect:/admin/manage-user/edit-user?userId=" + newUserId;
        }
    }

    @PostMapping("/changeUserType/ldap-user")
    public String changeLdapUserType(final RedirectAttributes redirectAttributes,
                                     @ModelAttribute("changeUserTypeFormData") ChangeUserTypeFormData changeUserTypeFormData) {
        // Change User to LDAP_USER
        ChangeUserTypeResultData changeUserTypeResultData = userFacade.changeUserTypeToLdapUser(changeUserTypeFormData);
        String newUserId = changeUserTypeResultData == null ? null : changeUserTypeResultData.getUserId();
        String errorMsg = changeUserTypeResultData == null ? "No create result." : changeUserTypeResultData.getErrorMsg();
        if (newUserId == null) {
            redirectAttributes.addFlashAttribute(PageMsgController.ERROR_MSG, errorMsg);
            redirectAttributes.addFlashAttribute("createUserFormData", changeUserTypeResultData);
            return "redirect:/admin/manage-user/edit-user/changeUserType/ldap-user?userId=" + changeUserTypeFormData.getUserId();
        } else {
            redirectAttributes.addFlashAttribute(PageMsgController.INFO_MSG, "Change user.");
            return "redirect:/admin/manage-user/edit-user?userId=" + newUserId;
        }
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

    @GetMapping("expireUserSession/{userId}")
    public ResponseEntity<?> expireUserSession(@PathVariable String userId) {
        SdUserData user = userFacade.getUserByUserId(userId);
        if (ObjectUtils.isEmpty(user)) {
            ResponseEntity.badRequest().body("User not found.");
        }
        sessionRegistry.getAllPrincipals().stream()
                .filter(principal -> user.getUserId().equals(((User) principal).getUsername()))
                .map(principal -> sessionRegistry.getAllSessions(principal, false))
                .filter(allSessions -> !ObjectUtils.isEmpty(allSessions))
                .flatMap(Collection::stream).forEach(SessionInformation::expireNow);
        return ResponseEntity.ok("Expire user session successfully.");

    }
}
