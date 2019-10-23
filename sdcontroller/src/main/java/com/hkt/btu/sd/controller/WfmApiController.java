package com.hkt.btu.sd.controller;

import com.hkt.btu.sd.controller.response.SimpleAjaxResponse;
import com.hkt.btu.sd.facade.SdTicketFacade;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;

@Controller
@RequestMapping("/wfm-api")
public class WfmApiController {

    @Resource(name = "ticketFacade")
    SdTicketFacade ticketFacade;


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
