package com.hkt.btu.sd.controller;

import com.hkt.btu.sd.controller.response.SimpleAjaxResponse;
import com.hkt.btu.sd.facade.SdAuditTrailFacade;
import com.hkt.btu.sd.facade.SdUserFacade;
import com.hkt.btu.sd.facade.data.SdUserData;
import com.hkt.btu.sd.facade.data.UpdatePwdFormData;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;

@SuppressWarnings("SameReturnValue")
@Controller
@RequestMapping("/user")
public class UserController {

    @Resource(name = "userFacade")
    SdUserFacade userFacade;
    @Resource(name = "auditTrailFacade")
    SdAuditTrailFacade sdAuditTrailFacade;

    @GetMapping({"", "/", "/index"})
    public String userIndex() {
        return "redirect:/user/access-request/pending";
    }

    @GetMapping("/my-account")
    public String myAccount(@ModelAttribute("updatePwdFormData") UpdatePwdFormData updatePwdFormData) {
        return "user/myAccount";
    }

    @GetMapping("/my-account/get-current-user")
    public ResponseEntity<?> getCurrentUser() {
        SdUserData currentUserData = userFacade.getCurrentUser();
        if(currentUserData==null){
            return ResponseEntity.badRequest().body("Current user not found!");
        }else {
            sdAuditTrailFacade.insertViewUserAuditTrail(currentUserData);
            return ResponseEntity.ok(currentUserData);
        }
    }

    @PostMapping("/my-account/update-pwd")
    public ResponseEntity<?> updatePwd(@Valid @RequestBody UpdatePwdFormData updatePwdFormData) {
        String errorMsg = userFacade.updateCurrentUserPwd(updatePwdFormData);
        if(errorMsg==null){
            return ResponseEntity.ok(SimpleAjaxResponse.of());
        }else {
            return ResponseEntity.ok(SimpleAjaxResponse.of(false, errorMsg));
        }
    }

}
