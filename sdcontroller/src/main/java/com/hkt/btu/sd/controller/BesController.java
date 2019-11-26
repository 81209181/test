package com.hkt.btu.sd.controller;

import com.hkt.btu.sd.facade.SdTicketFacade;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;

@Controller
@RequestMapping("bes-api")
public class BesController {

    @Resource(name = "ticketFacade")
    SdTicketFacade ticketFacade;

    @GetMapping("ajax-get-fault")
    public ResponseEntity<?> getFaultInfo(@RequestParam String subscriberId) {
        return ResponseEntity.ok(ticketFacade.getFaultInfo(subscriberId, null));
    }

    @GetMapping("ajax-get-paged")
    public ResponseEntity<?> getFaultPaged(@RequestParam(defaultValue = "0") int draw,
                                           @RequestParam(defaultValue = "0") int start,
                                           @RequestParam(defaultValue = "10") int length,
                                           @RequestParam String subscriberId) {
        int page = start / length;
        Pageable pageable = PageRequest.of(page, length);
        return ResponseEntity.ok(ticketFacade.getFaultInfo(subscriberId,pageable));
    }
}
