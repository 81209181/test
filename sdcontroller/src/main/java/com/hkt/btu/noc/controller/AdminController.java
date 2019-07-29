package com.hkt.btu.noc.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;


@SuppressWarnings("SameReturnValue")
@Controller
@RequestMapping(value = "/admin")
public class AdminController {

    private static final Logger LOG = LogManager.getLogger(RootController.class);

    @GetMapping({"", "/", "/index"})
    public String adminIndex() {
        LOG.info("AdminController.admin");
        return "admin/index";
    }

}
