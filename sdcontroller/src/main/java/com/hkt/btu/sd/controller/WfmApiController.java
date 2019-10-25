package com.hkt.btu.sd.controller;

import com.hkt.btu.sd.controller.response.SimpleAjaxResponse;
import com.hkt.btu.sd.facade.SdTicketFacade;
import com.hkt.btu.sd.facade.data.SdTicketData;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;

@Controller
@RequestMapping("/wfm-api")
public class WfmApiController {

    @Resource(name = "ticketFacade")
    SdTicketFacade ticketFacade;


    @GetMapping("/get-ticket")
    public ResponseEntity<?> getTicketInfo(@RequestParam Integer ticketMasId) {
        SdTicketData ticketData = ticketFacade.getTicketInfo(ticketMasId);
        return ResponseEntity.ok(ticketData);
    }

    @PostMapping("/close-ticket")
    public ResponseEntity<?> ticketClose(int ticketMasId, String reasonType, String reasonContent, String username) {
        String errorMsg = ticketFacade.closeTicketByApi(ticketMasId, reasonType, reasonContent, username);
        if(StringUtils.isEmpty(errorMsg)){
            return ResponseEntity.ok(SimpleAjaxResponse.of());
        }else {
            return ResponseEntity.ok(SimpleAjaxResponse.of(false, errorMsg));
        }
    }
}
