package com.hkt.btu.noc.controller;

import com.hkt.btu.common.spring.security.web.authentication.BtuExceptionMappingAuthenticationFailureHandler.LOGIN_ERROR;
import com.hkt.btu.noc.controller.response.SimpleAjaxResponse;
import com.hkt.btu.noc.facade.NocUserFacade;
import com.hkt.btu.noc.facade.data.ResetPwdFormData;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;


@SuppressWarnings("SameReturnValue")
@Controller
@RequestMapping(value = "/")
public class RootController {
    private static final Logger LOG = LogManager.getLogger(RootController.class);

    @Resource(name = "userFacade")
    NocUserFacade nocUserFacade;

    @GetMapping({"", "index"})
    public String root() {
        return "login";
    }

    @GetMapping(value = "login")
    public String login(final Model model,
                        @RequestParam(required = false) String logout,
                        @RequestParam(required = false) String error) {
        if(logout!=null){
            model.addAttribute(PageMsgController.INFO_MSG, "You have been logged out.");
        }
        if(error!=null){
            if(StringUtils.equals(error, LOGIN_ERROR.LOCK)){
                model.addAttribute(PageMsgController.ERROR_MSG, "Account deactivated. Please contact admin.");
            } else if(StringUtils.equals(error, LOGIN_ERROR.BAD_CREDENTIALS)){
                model.addAttribute(PageMsgController.ERROR_MSG, "Invalid username or password.");
            } else if(StringUtils.equals(error, LOGIN_ERROR.FORBIDDEN)){
                model.addAttribute(PageMsgController.ERROR_MSG, "Please login again. (Timeout/Forbidden).");
            } else if(StringUtils.equals(error, LOGIN_ERROR.TIMEOUT)){
                model.addAttribute(PageMsgController.ERROR_MSG, "Please login again. (Timeout)");
            } else if(StringUtils.equals(error, LOGIN_ERROR.LOGIN)){
                model.addAttribute(PageMsgController.ERROR_MSG, "Please login.");
            } else{
                model.addAttribute(PageMsgController.ERROR_MSG, "Login error.");
            }
        }

        return "login";
    }

    @GetMapping(value = "reset-password")
    public String resetPassword(final Model model,
                                @RequestParam(defaultValue = "false") boolean init,
                                @RequestParam(required = false) String email,
                                @RequestParam(required = false) String otp,
                                @ModelAttribute("isInit") String isInit,
                                @ModelAttribute("otpEmail") String otpEmail,
                                @ModelAttribute("resetPwdFormData") ResetPwdFormData resetPwdFormData) {
        if(!StringUtils.isEmpty(otp) && !StringUtils.isEmpty(email)){
            LOG.error("[Security Concern] Password init/reset link should not contain both email and OTP!");
            model.addAttribute(PageMsgController.ERROR_MSG, "Insecure link.");
            return "login";
        }

        model.addAttribute("isInit", init);

        if(!StringUtils.isEmpty(otp)) {
            resetPwdFormData.setResetOtp(otp);
            model.addAttribute("resetPwdFormData", resetPwdFormData);
        } else if(!StringUtils.isEmpty(email)) {
            model.addAttribute("otpEmail", email);
        }

        return "resetPassword";
    }

    @PostMapping(value = "reset-password")
    public ResponseEntity<?> resetPassword(@Valid @RequestBody ResetPwdFormData resetPwdFormData) {
        String errorMsg = nocUserFacade.resetPassword(resetPwdFormData);
        if(errorMsg==null){
            return ResponseEntity.ok(SimpleAjaxResponse.of());
        }else {
            return ResponseEntity.ok(SimpleAjaxResponse.of(false, errorMsg));
        }
    }
    @PostMapping(value = "reset-password-otp")
    public ResponseEntity<?> requestResetPassword(@RequestParam String email) {
        String errorMsg = nocUserFacade.requestResetPassword(email);
        if(errorMsg==null){
            return ResponseEntity.ok(SimpleAjaxResponse.of());
        }else {
            return ResponseEntity.ok(SimpleAjaxResponse.of(false, errorMsg));
        }
    }

}
