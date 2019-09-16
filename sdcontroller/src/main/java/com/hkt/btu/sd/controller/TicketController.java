package com.hkt.btu.sd.controller;

import com.hkt.btu.sd.facade.SdRequestCreateFacade;
import com.hkt.btu.sd.facade.SdTicketFacade;
import com.hkt.btu.sd.facade.data.RequestCreateSearchResultsData;
import com.hkt.btu.sd.facade.data.SdTicketContactData;
import com.hkt.btu.sd.facade.data.SdTicketMasData;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.Optional;

@RequestMapping("ticket")
@Controller
public class TicketController {

    @Resource(name = "requestCreateFacade")
    SdRequestCreateFacade requestCreateFacade;
    @Resource(name = "ticketFacade")
    SdTicketFacade ticketFacade;

    @GetMapping("search-customer")
    public String searchCustomer() {
        return "ticket/search_customer";
    }

    @PostMapping("searchCustomer")
    public ResponseEntity<?> searchCustomer(String searchKey, String searchValue) {
        RequestCreateSearchResultsData resultsData = requestCreateFacade.searchProductList(searchKey, searchValue);
        if (!StringUtils.isEmpty(resultsData.getErrorMsg())) {
            return ResponseEntity.badRequest().body(resultsData.getErrorMsg());
        } else {
            return ResponseEntity.ok(resultsData.getList());
        }
    }

    @PostMapping("query/create")
    public ResponseEntity<?> createQueryTicket(String custCode) {
        Optional<SdTicketMasData> data = ticketFacade.createQueryTicket(custCode);
        if (data.isPresent()) {
            return ResponseEntity.ok(data);
        } else {
            return ResponseEntity.badRequest().body("Create ticket fail.");
        }
    }

    @GetMapping("{ticketId}")
    public String showQueryTicket(@PathVariable Integer ticketId, Model model) {
        Optional<SdTicketMasData> data = ticketFacade.getTicket(ticketId);
        if (data.isPresent()) {
            model.addAttribute("customerCode", data.get().getCustCode());
            model.addAttribute("ticketMasId", data.get().getTicketMasId());
            return "ticket/ticket_info";
        } else {
            return "error";
        }
    }

    @PostMapping("contact/update")
    public ResponseEntity<?> updateContactInfo(@RequestBody List<SdTicketContactData> contactList) {
        ticketFacade.updateContactInfo(contactList);
        return ResponseEntity.ok("Update contact info success.");
    }

    @GetMapping("/contact/{ticketMasId}")
    public ResponseEntity<?> getContactInfo(@PathVariable Integer ticketMasId) {
        List<SdTicketContactData> data = ticketFacade.getContactInfo(ticketMasId);
        return ResponseEntity.ok(data);
    }
}
