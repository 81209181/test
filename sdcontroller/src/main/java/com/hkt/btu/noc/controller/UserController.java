package com.hkt.btu.noc.controller;

import com.hkt.btu.noc.controller.response.SimpleAjaxResponse;
import com.hkt.btu.noc.facade.NocAuditTrailFacade;
import com.hkt.btu.noc.facade.NocUserFacade;
import com.hkt.btu.noc.facade.data.NocUserData;
import com.hkt.btu.noc.facade.data.UpdatePwdFormData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;

@SuppressWarnings("SameReturnValue")
@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    NocUserFacade userFacade;
    @Autowired
    NocAuditTrailFacade nocAuditTrailFacade;

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
        NocUserData currentUserData = userFacade.getCurrentUser();
        if(currentUserData==null){
            return ResponseEntity.badRequest().body("Current user not found!");
        }else {
            nocAuditTrailFacade.insertViewUserAuditTrail(currentUserData);
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
