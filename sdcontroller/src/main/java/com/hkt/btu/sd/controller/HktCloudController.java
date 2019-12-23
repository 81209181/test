package com.hkt.btu.sd.controller;

import com.hkt.btu.sd.facade.HktCloudCaseData;
import com.hkt.btu.sd.facade.HktCloudViewData;
import com.hkt.btu.sd.facade.SdTicketFacade;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RequestMapping("hkt-cloud-api")
@RestController
public class HktCloudController {

    @Resource(name = "ticketFacade")
    SdTicketFacade ticketFacade;

    @GetMapping("getNewTicketId")
    public String getNewTicketId() {
        return ticketFacade.getNewTicketId();
    }

    @PostMapping("createFault")
    public String createFaultCase(@RequestBody HktCloudCaseData cloudCaseData) {
        try {
            return ticketFacade.createTicket4hktCloud(cloudCaseData);
        } catch (Exception e) {
            e.printStackTrace();
            return "Fail.";
        }
    }

    @GetMapping("getTicket/{tenantId}/{username}")
    public List<HktCloudViewData> getHktCloudTicket(@PathVariable String tenantId, @PathVariable String username) {
        return ticketFacade.getHktCloudTicket(tenantId,username);
    }

}
