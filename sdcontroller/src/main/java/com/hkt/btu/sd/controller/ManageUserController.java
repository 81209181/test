package com.hkt.btu.sd.controller;

import com.hkt.btu.common.facade.data.PageData;
import com.hkt.btu.sd.controller.constant.CreateUserPathEnum;
import com.hkt.btu.sd.controller.response.SimpleAjaxResponse;
import com.hkt.btu.sd.controller.response.helper.ResponseEntityHelper;
import com.hkt.btu.sd.core.service.bean.SdUserRoleBean;
import com.hkt.btu.sd.facade.SdAuditTrailFacade;
import com.hkt.btu.sd.facade.SdUserFacade;
import com.hkt.btu.sd.facade.SdUserRoleFacade;
import com.hkt.btu.sd.facade.data.*;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.BooleanUtils;
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
import java.security.Principal;
import java.util.*;
import java.util.stream.Collectors;

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
    public String createPccwOrHktUser(final Model model,
                                      final HttpServletRequest request,
                                      @ModelAttribute("createUserFormData") CreateUserFormData createUserFormData,
                                      @ModelAttribute("userRoleOptionDataMap") HashMap<String, SdUserRoleData> userRoleOptionDataMap) {
        // user role info
        List<SdUserRoleData> userRoleDataList = userRoleFacade.getEligibleUserRoleList();
        userRoleOptionDataMap = userRoleFacade.getUserRoleMap(userRoleDataList);
        if (!MapUtils.isEmpty(userRoleOptionDataMap)) {
            model.addAttribute("userRoleOptionDataMap", userRoleOptionDataMap);
        }
        List<SdUserRoleBean> primaryRoleList = userRoleFacade.getPrimaryRoleList(); // todo [SERVDESK-203]:  cannot put bean in Controller Layer
        model.addAttribute("primaryRoleList",primaryRoleList);
        String servletPath = request.getServletPath();
        return CreateUserPathEnum.getValue(servletPath);
    }


    @PostMapping("/create-pccw-hkt-user")
    public String createPccwOrHktUser(final RedirectAttributes redirectAttributes,
                                      @ModelAttribute("inputUserData") CreateUserFormData createUserFormData) {
        CreateResultData createResultData = userFacade.createPccwHktUser(createUserFormData);
        String newUserId = createResultData == null ? null : createResultData.getNewId();
        String errorMsg = createResultData == null ? "No create result." : createResultData.getErrorMsg();
        if (newUserId == null) {
            redirectAttributes.addFlashAttribute(PageMsgController.ERROR_MSG, errorMsg);
            redirectAttributes.addFlashAttribute("createUserFormData", createUserFormData);
            return "redirect:create-user";
        } else {
            String passwordMsg = null;
            passwordMsg = StringUtils.isNotEmpty(createResultData.getPasswordMsg()) ?
                    "Create User.OTP is " + createResultData.getPasswordMsg() : "Create User.";
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
            String passwordMsg = StringUtils.isNotEmpty(createResultData.getPasswordMsg()) ?
                    "Create User.OTP is " + createResultData.getPasswordMsg() : "Create User.";
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
        List<SdUserRoleData> allUserRoles = userRoleFacade.listAllUserRole();
        if (userId != null) {
            model.addAttribute("editUserId", userId);
            EditResultData result = userRoleFacade.getUserRoleByUserId(userId);
            String errorMsg = result == null ? "" : result.getErrorMsg();
            List<String> userRole = result == null ? null : (List<String>) result.getList();
            if (CollectionUtils.isNotEmpty(userRole)) {
                model.addAttribute("userRoleList", userRole);
                List<SdUserRoleBean> primaryRoleList = userRoleFacade.getPrimaryRoleList(); // todo [SERVDESK-203]:  cannot put bean in Controller Layer
                model.addAttribute("primaryRoleList",primaryRoleList);
            } else {
                redirectAttributes.addFlashAttribute(PageMsgController.ERROR_MSG, errorMsg);
                return "redirect:search-user";
            }
        }
        model.addAttribute("allUserRole", allUserRoles.stream()
                .filter(sdUserRoleData -> !BooleanUtils.toBoolean(sdUserRoleData.getAbstractFlag()))
                .filter(sdUserRoleData -> sdUserRoleData.getStatus().equals("A"))
                .collect(Collectors.toList()));
        model.addAttribute("eligibleUserRole", userRoleFacade.getEligibleUserRoleList().stream().map(SdUserRoleData::getRoleId).collect(Collectors.toList()));

        return "admin/manageUser/editUserForm";
    }

    @GetMapping("/edit-user/changeUserType")
    public String changeUserType(final Model model,
                                 String userId,
                                 @RequestParam String userType,
                                 @ModelAttribute("changeUserTypeFormData") ChangeUserTypeFormData changeUserTypeFormData) {
        model.addAttribute("oldUserId", userId);
        if (ChangeUserTypeFormData.PCCW_HKT_USER.equals(userType)) {
            return "/admin/manageUser/changeUserType/updateUserTypeToPccwOrHktUser";
        } else if (ChangeUserTypeFormData.LDAP_USER.equals(userType)) {
            return "/admin/manageUser/changeUserType/updateUserTypeToLdapUser";
        } else if (ChangeUserTypeFormData.NON_PCCW_HKT_USER.equals(userType)) {
            return "/admin/manageUser/changeUserType/updateUserTypeToNonPccwOrHktUser";
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
            return "redirect:/admin/manage-user/edit-user/changeUserType?userType=" + ChangeUserTypeFormData.PCCW_HKT_USER + "&userId=" + changeUserTypeFormData.getUserId();
        } else {
            redirectAttributes.addFlashAttribute(PageMsgController.INFO_MSG, "Change user.");
            return "redirect:/admin/manage-user/edit-user?userId=" + newUserId;
        }
    }

    @PostMapping("/changeUserType/non-pccw-hkt-user")
    public String changeNonPCCWOrUserType(final RedirectAttributes redirectAttributes,
                                          @ModelAttribute("changeUserTypeFormData") ChangeUserTypeFormData changeUserTypeFormData) {
        // Change User to  PCCW_HKT_USER
        ChangeUserTypeResultData changeUserTypeResultData = userFacade.changeUserTypeToNonPccwOrHktUser(changeUserTypeFormData);
        String newUserId = changeUserTypeResultData == null ? null : changeUserTypeResultData.getUserId();
        String errorMsg = changeUserTypeResultData == null ? "No create result." : changeUserTypeResultData.getErrorMsg();
        if (newUserId == null) {
            redirectAttributes.addFlashAttribute(PageMsgController.ERROR_MSG, errorMsg);
            redirectAttributes.addFlashAttribute("createUserFormData", changeUserTypeResultData);
            return "redirect:/admin/manage-user/edit-user/changeUserType?userType=" + ChangeUserTypeFormData.NON_PCCW_HKT_USER + "&userId=" + changeUserTypeFormData.getUserId();
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
            return "redirect:/admin/manage-user/edit-user/changeUserType?userType=" + ChangeUserTypeFormData.LDAP_USER + "&userId=" + changeUserTypeFormData.getUserId();
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

    @GetMapping("/expireUserSession")
    public ResponseEntity<?> expireUserSession(@RequestParam String userId, Principal p) {
        SdUserData user = userFacade.getUserByUserId(userId);
        if (ObjectUtils.isEmpty(user)) {
            ResponseEntity.badRequest().body("User not found.");
        }
        sessionRegistry.getAllPrincipals().stream()
                .filter(principal -> user.getUserId().equals(((User) principal).getUsername()))
                .map(principal -> sessionRegistry.getAllSessions(principal, false))
                .filter(allSessions -> !ObjectUtils.isEmpty(allSessions))
                .flatMap(Collection::stream).forEach(SessionInformation::expireNow);
        sdAuditTrailFacade.insertKickAuditTrail(userId, p.getName());
        return ResponseEntity.ok("Expire user session successfully.");

    }

    @GetMapping("resetUserPwd/{userId}")
    public ResponseEntity<?> resetUserPwd(@PathVariable String userId) {
        try {
            userFacade.resetPwd4NonLdapUser(userId);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
        return ResponseEntity.ok("Reset password email has been sent to your email.");
    }
}
