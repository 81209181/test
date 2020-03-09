package com.hkt.btu.sd.controller;

import com.hkt.btu.common.facade.data.BtuSimpleResponseData;
import com.hkt.btu.common.facade.data.PageData;
import com.hkt.btu.sd.facade.SdSmartMeterFacade;
import com.hkt.btu.sd.facade.data.SdTicketData;
import com.hkt.btu.sd.facade.data.SdTicketMasData;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequestMapping("/oss-api")
public class OssApiController {

    @Resource(name = "smartMeterFacade")
    SdSmartMeterFacade smartMeterFacade;

    @GetMapping("/create-ticket")
    public ResponseEntity<?> createTicket(
            @RequestParam Integer poleId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime triggerTime,
            @RequestParam List<String> workingPartyList ) {
        BtuSimpleResponseData simpleResponseData = smartMeterFacade.createTicket(poleId, triggerTime);
        return ResponseEntity.ok(simpleResponseData);
    }

    @GetMapping("/get-ticket")
    public ResponseEntity<?> getTicketInfo(@RequestParam Integer ticketMasId) {
        SdTicketData ticketData = smartMeterFacade.getTicketInfo(ticketMasId);
        if(ticketData==null){
            return ResponseEntity.badRequest().body("Smart Meter ticket not found. (ticketMasId=" + ticketMasId + ")");
        }else {
            return ResponseEntity.ok(ticketData);
        }
    }

    @GetMapping("/search-ticket")
    public ResponseEntity<?> searchTicketsOfMeter(
            @RequestParam Integer poleId,
            @RequestParam(required = false) String createDateFrom,
            @RequestParam(required = false) String createDateTo,
            @RequestParam Integer page,
            @RequestParam Integer pageSize) {
        pageSize = (pageSize==null || pageSize<1 || pageSize>100) ? 10 : pageSize;
        Pageable pageable = PageRequest.of(page, pageSize);
        PageData<SdTicketMasData> ticketMasDataPageData = smartMeterFacade.searchTicketList(pageable, poleId, createDateFrom, createDateTo);
        if(ticketMasDataPageData==null){
            return ResponseEntity.badRequest().body(String.format("Cannot search Smart Meter tickets. (poleId=%d, page=%d, pageSize=%d)", poleId, page, pageSize));
        }else {
            return ResponseEntity.ok(ticketMasDataPageData);
        }
    }

}
