package com.hkt.btu.sd.controller;


import com.hkt.btu.sd.controller.response.SimpleAjaxResponse;
import com.hkt.btu.sd.facade.SdHealthCheckFacade;
import com.hkt.btu.sd.facade.SdPublicHolidayFacade;
import com.hkt.btu.sd.facade.data.SdPublicHolidayData;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.util.List;


@SuppressWarnings("SameReturnValue")
@Controller
@RequestMapping(value = "/public")
public class PublicController {

    @Resource(name = "healthCheckFacade")
    SdHealthCheckFacade sdHealthCheckFacade;

    @Resource(name = "publicHolidayFacade")
    SdPublicHolidayFacade sdPublicHolidayFacade;

    @GetMapping("/contact-us")
    public String contactUs() {
        return "public/contactUs";
    }

    @GetMapping(value = "health-check")
    public ResponseEntity healthCheck() {
        String errorMsg = sdHealthCheckFacade.healthCheck();
        if (errorMsg == null) {
            return ResponseEntity.ok().body("success");
        } else {
            return ResponseEntity.badRequest().body("fail");
        }
    }
    @GetMapping("keepSession")
    @ResponseBody
    public SimpleAjaxResponse keepSession() {
        return SimpleAjaxResponse.of();
    }

    @GetMapping("/terms")
    public String termsOfUser() {
        return "public/terms";
    }

    @GetMapping("/get-public-holiday")
    public ResponseEntity<?> ajaxListPublicHoliday(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        List<SdPublicHolidayData> publicHolidayList = sdPublicHolidayFacade.getPublicHolidayByDate(date);
        return ResponseEntity.ok(publicHolidayList);
    }
}
