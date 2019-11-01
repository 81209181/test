package com.hkt.btu.sd.controller;

import com.hkt.btu.common.spring.security.web.authentication.BtuLoginUrlAuthenticationEntryPoint.LOGIN_ERROR;
import com.hkt.btu.sd.controller.response.SimpleAjaxResponse;
import com.hkt.btu.sd.facade.SdUserFacade;
import com.hkt.btu.sd.facade.data.ResetPwdFormData;
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
    SdUserFacade sdUserFacade;

    @GetMapping({"", "index"})
    public String root() {
        return "login";
    }

    @GetMapping(value = "login")
    public String login(final Model model,
                        @RequestParam(required = false) String logout,
                        @RequestParam(required = false) String error) {
        if (logout != null) {
            model.addAttribute(PageMsgController.INFO_MSG, "You have been logged out.");
        }
        if (error != null) {
            model.addAttribute(PageMsgController.ERROR_MSG, LOGIN_ERROR.getValue(error));
        }

        return "login";
    }

    @GetMapping(value = "reset-password")
    public String resetPassword(final Model model,
                                @RequestParam(defaultValue = "false") boolean init,
                                @RequestParam(required = false) String otp,
                                @ModelAttribute("isInit") String isInit,
                                @ModelAttribute("resetPwdFormData") ResetPwdFormData resetPwdFormData) {
        model.addAttribute("isInit", init);

        if (!StringUtils.isEmpty(otp)) {
            resetPwdFormData.setResetOtp(otp);
            model.addAttribute("resetPwdFormData", resetPwdFormData);
        }

        return "resetPassword";
    }

    @PostMapping(value = "reset-password")
    public ResponseEntity<?> resetPassword(@Valid ResetPwdFormData resetPwdFormData) {
        String errorMsg = sdUserFacade.resetPassword(resetPwdFormData);
        if (errorMsg == null) {
            return ResponseEntity.ok(SimpleAjaxResponse.of());
        } else {
            return ResponseEntity.ok(SimpleAjaxResponse.of(false, errorMsg));
        }
    }

    @PostMapping(value = "reset-password-otp")
    public ResponseEntity<?> requestResetPassword(@RequestParam String name) {
        String errorMsg = sdUserFacade.requestResetPassword(name);
        if (errorMsg == null) {
            return ResponseEntity.ok(SimpleAjaxResponse.of());
        } else {
            return ResponseEntity.ok(SimpleAjaxResponse.of(false, errorMsg));
        }
    }

}
