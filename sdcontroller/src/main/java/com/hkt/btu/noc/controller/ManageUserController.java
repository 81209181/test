package com.hkt.btu.noc.controller;

import com.hkt.btu.common.facade.data.PageData;
import com.hkt.btu.noc.controller.response.SimpleAjaxResponse;
import com.hkt.btu.noc.controller.response.helper.ResponseEntityHelper;
import com.hkt.btu.noc.facade.NocAuditTrailFacade;
import com.hkt.btu.noc.facade.NocUserFacade;
import com.hkt.btu.noc.facade.NocUserGroupFacade;
import com.hkt.btu.noc.facade.data.*;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

@SuppressWarnings({"ParameterCanBeLocal", "SameReturnValue"})
@Controller
@RequestMapping(value = "/admin/manage-user")
public class ManageUserController {

//    @Resource(name = "userFacade")
    @Autowired
    NocUserFacade userFacade;
    @Autowired
    NocUserGroupFacade nocUserGroupFacade;
    @Autowired
    NocAuditTrailFacade nocAuditTrailFacade;

    @GetMapping("/create-user")
    public String createUserForm (final Model model,
                                  @ModelAttribute("createUserFormData") CreateUserFormData createUserFormData,
                                  @ModelAttribute("companyOptionList") LinkedList<NocCompanyData> companyOptionList,
                                  @ModelAttribute("userGroupOptionDataMap") HashMap<String, NocUserGroupData> userGroupOptionDataMap) {
        companyOptionList = userFacade.getEligibleCompanyList();
        if(!CollectionUtils.isEmpty(companyOptionList)){
            model.addAttribute("companyOptionList", companyOptionList);
        }

        // user group info
        List<NocUserGroupData> userGroupDataList = nocUserGroupFacade.getEligibleUserGroupList();
        userGroupOptionDataMap = nocUserGroupFacade.getUserGroupMap(userGroupDataList);
        if(! MapUtils.isEmpty(userGroupOptionDataMap)) {
            model.addAttribute("userGroupOptionDataMap", userGroupOptionDataMap);
        }

        return "admin/manageUser/createUserForm";
    }

    @PostMapping("/create-user")
    public String createUserForm (final RedirectAttributes redirectAttributes,
                                  @ModelAttribute("inputUserData") CreateUserFormData createUserFormData) {
        CreateResultData createResultData = userFacade.createUser(createUserFormData);
        Integer newUserId = createResultData==null ? null : createResultData.getNewId();
        String errorMsg = createResultData==null ? "No create result." : createResultData.getErrorMsg();

        if(newUserId==null){
            redirectAttributes.addFlashAttribute(PageMsgController.ERROR_MSG, errorMsg);
            redirectAttributes.addFlashAttribute("createUserFormData", createUserFormData);
            return "redirect:create-user";
        } else {
            redirectAttributes.addFlashAttribute(PageMsgController.INFO_MSG, "Created user.");
            return "redirect:edit-user?userId="+newUserId;
        }
    }

    @GetMapping("/edit-user")
    public String editUserForm (final Model model,
                                @RequestParam Integer userId,
                                @ModelAttribute("editUserId") String editUserId,
                                @ModelAttribute("userGroupDataMap") HashMap<String, NocUserGroupData> userGroupDataMap) {
        if(userId!=null){
            model.addAttribute("editUserId", userId);
        }

        // user group info
        List<NocUserGroupData> userGroupDataList = nocUserGroupFacade.getEligibleUserGroupList();
        userGroupDataMap = nocUserGroupFacade.getUserGroupMap(userGroupDataList);
        if(! MapUtils.isEmpty(userGroupDataMap)) {
            model.addAttribute("userGroupDataMap", userGroupDataMap);
        }

        return "admin/manageUser/editUserForm";
    }

    @GetMapping("/edit-user/get-user")
    public ResponseEntity<?> getUserById (@RequestParam Integer userId) {
        if(userId==null){
            return ResponseEntity.badRequest().body("Empty user id!");
        }

        NocUserData nocUserData = userFacade.getUserByUserId(userId);
        if(nocUserData==null) {
            return ResponseEntity.badRequest().body("User not found.");
        } else {
            nocAuditTrailFacade.insertViewUserAuditTrail(nocUserData);
            return ResponseEntity.ok(nocUserData);
        }
    }

    @PostMapping("/edit-user")
    public ResponseEntity<?> editUserForm (@RequestBody UpdateUserFormData updateUserFormData) {
        String errorMsg = userFacade.updateUser(updateUserFormData);
        if(errorMsg==null){
            return ResponseEntity.ok(SimpleAjaxResponse.of());
        }else {
            return ResponseEntity.ok(SimpleAjaxResponse.of(false, errorMsg));
        }
    }

    @PostMapping("/edit-user/activate")
    public ResponseEntity<?> activateUser (@RequestParam Integer userId) {
        String errorMsg = userFacade.activateUser(userId);
        if(errorMsg==null){
            return ResponseEntity.ok(SimpleAjaxResponse.of());
        }else {
            return ResponseEntity.ok(SimpleAjaxResponse.of(false, errorMsg));
        }
    }

    @PostMapping("/edit-user/deactivate")
    public ResponseEntity<?> deactivateUser (@RequestParam Integer userId) {
        String errorMsg = userFacade.deactivateUser(userId);
        if(errorMsg==null){
            return ResponseEntity.ok(SimpleAjaxResponse.of());
        }else {
            return ResponseEntity.ok(SimpleAjaxResponse.of(false, errorMsg));
        }
    }

    @GetMapping("/search-user")
    public String searchUser(final Model model,
                             @ModelAttribute("userGroupOptionList") LinkedList<NocUserGroupData> userGroupOptionList) {
        userGroupOptionList = nocUserGroupFacade.getEligibleUserGroupList();
        if(!CollectionUtils.isEmpty(userGroupOptionList)){
            model.addAttribute("userGroupOptionList", userGroupOptionList);
        }

        return "admin/manageUser/searchUser";
    }

    @GetMapping("/ajax-search-user")
    public ResponseEntity<?> ajaxSearchUser(
            @RequestParam(defaultValue = "0") int draw,
            @RequestParam(defaultValue = "0") int start,
            @RequestParam(defaultValue = "10") int length,
            @RequestParam(required= false) Integer userId,
            @RequestParam(required= false) String name,
            @RequestParam(required= false) String email,
            @RequestParam(required= false) String userGroupId) {
        int page = start/length;
        Pageable pageable = PageRequest.of(page, length);

        PageData<NocUserData> pageData = userFacade.searchUser(pageable, userId, email, name, userGroupId);
        return ResponseEntityHelper.buildDataTablesResponse(draw, pageData);
    }



}
