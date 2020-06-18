package com.hkt.btu.sd.controller;

import com.hkt.btu.sd.controller.response.SimpleAjaxResponse;
import com.hkt.btu.sd.facade.SdSymptomFacade;
import com.hkt.btu.sd.facade.SdTicketFacade;
import com.hkt.btu.sd.facade.data.SdSymptomData;
import com.hkt.btu.sd.facade.data.SdTicketData;
import com.hkt.btu.sd.facade.data.wfm.WfmTicketCloseData;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@Controller
@RequestMapping("/wfm-api")
public class WfmApiController {

    @Resource(name = "ticketFacade")
    SdTicketFacade ticketFacade;
    @Resource(name = "sdSymptomFacade")
    SdSymptomFacade sdSymptomFacade;


    @GetMapping("/get-ticket")
    public ResponseEntity<?> getTicketInfo(@RequestParam Integer ticketMasId) {
        SdTicketData ticketData = ticketFacade.getTicketInfo(ticketMasId);
        return ResponseEntity.ok(ticketData);
    }

    @PostMapping("/close-ticket")
    public ResponseEntity<?> ticketClose(@RequestBody WfmTicketCloseData wfmTicketCloseData) {
        String errorMsg = ticketFacade.closeTicketByApi(wfmTicketCloseData);
        if(StringUtils.isEmpty(errorMsg)){
            return ResponseEntity.ok(SimpleAjaxResponse.of());
        }else {
            return ResponseEntity.ok(SimpleAjaxResponse.of(false, errorMsg));
        }
    }

    @GetMapping("/get-all-symptoms")
    public ResponseEntity<?> getAllSymptoms() {
        List<SdSymptomData> symptomDataList = sdSymptomFacade.getAllSymptomList();
        return ResponseEntity.ok(symptomDataList);
    }
}
