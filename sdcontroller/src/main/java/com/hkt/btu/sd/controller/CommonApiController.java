package com.hkt.btu.sd.controller;

import com.hkt.btu.common.facade.data.BtuSimpleResponseData;
import com.hkt.btu.common.facade.data.PageData;
import com.hkt.btu.sd.facade.SdSmartMeterFacade;
import com.hkt.btu.sd.facade.data.SdTicketData;
import com.hkt.btu.sd.facade.data.SdTicketMasData;
import com.hkt.btu.sd.facade.data.oss.OssCaseData;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequestMapping("/api/v1")
public class CommonApiController {

    @Resource(name = "smartMeterFacade")
    SdSmartMeterFacade smartMeterFacade;

    @PostMapping("/ticket/create")
    public ResponseEntity<?> createTicket(@RequestBody OssCaseData ossCaseData) {
        BtuSimpleResponseData simpleResponseData = smartMeterFacade.createTicket(ossCaseData);
        return ResponseEntity.ok(simpleResponseData);
    }
}
