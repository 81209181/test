package com.hkt.btu.sd.controller;

import com.hkt.btu.common.facade.data.BtuSimpleResponseData;
import com.hkt.btu.common.facade.data.PageData;
import com.hkt.btu.sd.facade.SdSmartMeterFacade;
import com.hkt.btu.sd.facade.data.SdTicketData;
import com.hkt.btu.sd.facade.data.SdTicketMasData;
import com.hkt.btu.sd.facade.data.oss.OssCaseData;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@Controller
@RequestMapping("/oss-api")
public class OssApiController {

    @Resource(name = "smartMeterFacade")
    SdSmartMeterFacade smartMeterFacade;

    @PostMapping("/create-ticket")
    public ResponseEntity<?> createTicket(@RequestBody OssCaseData ossCaseData) {
        BtuSimpleResponseData simpleResponseData = smartMeterFacade.createTicket(ossCaseData);
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
            @RequestParam(required = false) Integer poleId,
            @RequestParam(required = false) String plateId,
            @RequestParam(required = false) String createDateFrom,
            @RequestParam(required = false) String createDateTo,
            @RequestParam Integer page,
            @RequestParam Integer pageSize) {
        if(poleId == null && plateId == null){
            return ResponseEntity.badRequest().body("Null pole ID or plate ID.");
        }

        // limit page size
        pageSize = (pageSize==null || pageSize<1 || pageSize>100) ? 10 : pageSize;

        // search result
        Pageable pageable = PageRequest.of(page, pageSize);
        PageData<SdTicketMasData> ticketMasDataPageData = smartMeterFacade.searchTicketList(
                pageable, poleId, plateId, createDateFrom, createDateTo, null, null);

        if(ticketMasDataPageData==null){
            return ResponseEntity.badRequest().body(String.format("Cannot search Smart Meter tickets. (poleId=%d, page=%d, pageSize=%d)", poleId, page, pageSize));
        }else {
            return ResponseEntity.ok(ticketMasDataPageData);
        }
    }

}
