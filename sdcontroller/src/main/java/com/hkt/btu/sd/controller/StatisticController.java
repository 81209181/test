package com.hkt.btu.sd.controller;

import com.hkt.btu.sd.facade.SdStatisticFacade;
import com.hkt.btu.sd.facade.data.SdStatisticData;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;

@Controller
@RequestMapping("/system/statistic")
public class StatisticController {
    @Resource(name = "statisticFacade")
    SdStatisticFacade statisticFacade;

    @GetMapping("")
    public String index() {
        return "system/statistic/statisticPage";
    }

    @GetMapping("getLoginCountLast90Days")
    public ResponseEntity<?> getLoginCountLast90Days() {
        SdStatisticData data = statisticFacade.getLoginCountLast90Days();

        if (ObjectUtils.isEmpty(data)) {
            return ResponseEntity.badRequest().body("get failed.");
        } else {
            return ResponseEntity.ok(data);
        }
    }

    @GetMapping("getLoginCountLastTwoWeeks")
    public ResponseEntity<?> getLoginCountLastTwoWeeks() {
        SdStatisticData data = statisticFacade.getLoginCountLastTwoWeeks();

        if (ObjectUtils.isEmpty(data)) {
            return ResponseEntity.badRequest().body("get failed.");
        } else {
            return ResponseEntity.ok(data);
        }
    }

    @GetMapping("getTicketTypeCountPerOwnerGroup")
    public ResponseEntity<?> getTicketTypeCountPerOwnerGroup() {
        SdStatisticData data = statisticFacade.ticketTypeCountPerOwnerGroup();

        if (ObjectUtils.isEmpty(data)) {
            return ResponseEntity.badRequest().body("get failed.");
        } else {
            return ResponseEntity.ok(data);
        }
    }

    @GetMapping("getTicketStatusCountPerOwnerGroup")
    public ResponseEntity<?> getTicketStatusCountPerOwnerGroup() {
        SdStatisticData data = statisticFacade.ticketStatusCountPerOwnerGroup();

        if (ObjectUtils.isEmpty(data)) {
            return ResponseEntity.badRequest().body("get failed.");
        } else {
            return ResponseEntity.ok(data);
        }
    }
}
