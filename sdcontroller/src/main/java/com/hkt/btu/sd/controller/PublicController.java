package com.hkt.btu.sd.controller;


import com.hkt.btu.sd.facade.SdHealthCheckFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;


@SuppressWarnings("SameReturnValue")
@Controller
@RequestMapping(value = "/public")
public class PublicController {

    @Autowired
    SdHealthCheckFacade sdHealthCheckFacade;



    @GetMapping("/contact-us")
    public String contactUs() {
        return "public/contactUs";
    }

    @GetMapping(value = "health-check")
    public ResponseEntity healthCheck() {
        String errorMsg = sdHealthCheckFacade.healthCheck();
        if(errorMsg==null){
            return ResponseEntity.ok().body("success");
        }else {
            return ResponseEntity.badRequest().body("fail");
        }
    }
}
