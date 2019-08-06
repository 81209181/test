package com.hkt.btu.sd.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

@SuppressWarnings("SameReturnValue")
@Controller
@RequestMapping(value = "/page-msg")
public class PageMsgController {

    public static final String INFO_MSG = "infoMsg";
    public static final String ERROR_MSG = "errorMsg";

    @GetMapping("/dummy")
    public String dummy(final Model model,
                        @ModelAttribute(INFO_MSG) String infoMsg,
                        @ModelAttribute(ERROR_MSG) String errorMsg) {
        model.addAttribute(INFO_MSG, "testing page info msg");
        model.addAttribute(ERROR_MSG, "testing page error msg");
        return "fragments/pageMsg";
    }
}
