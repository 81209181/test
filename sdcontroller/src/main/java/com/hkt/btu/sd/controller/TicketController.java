package com.hkt.btu.sd.controller;

import com.hkt.btu.common.facade.data.PageData;
import com.hkt.btu.sd.controller.response.helper.ResponseEntityHelper;
import com.hkt.btu.sd.facade.SdRequestCreateFacade;
import com.hkt.btu.sd.facade.SdTicketFacade;
import com.hkt.btu.sd.facade.data.RequestCreateSearchResultsData;
import com.hkt.btu.sd.facade.data.SdTicketContactData;
import com.hkt.btu.sd.facade.data.SdTicketMasData;
import com.hkt.btu.sd.facade.data.SdTicketServiceData;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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

    @GetMapping("searchTicket")
    public String searchTicket() {
        return "ticket/searchTicket";
    }

    @GetMapping("search-ticket")
    public ResponseEntity<?> searchTicket(@RequestParam(defaultValue = "0") int draw,
                                          @RequestParam(defaultValue = "0") int start,
                                          @RequestParam(defaultValue = "10") int length,
                                          @RequestParam(required = false) String dateFrom,
                                          @RequestParam(required = false) String dateTo,
                                          @RequestParam(required = false) String status) {
        int page = start / length;
        Pageable pageable = PageRequest.of(page, length);

        PageData<SdTicketMasData> pageData = ticketFacade.searchTicketList(pageable, dateFrom, dateTo, status);
        return ResponseEntityHelper.buildDataTablesResponse(draw, pageData);
    }

    @GetMapping("my-ticket")
    public ResponseEntity<?> getMyTicket() {
        List<SdTicketMasData> dataList = ticketFacade.getMyTicket();
        if (CollectionUtils.isEmpty(dataList)) {
            return ResponseEntity.badRequest().body("Ticket list not found.");
        } else {
            return ResponseEntity.ok(dataList);
        }
    }

    @GetMapping("/service/{ticketMasId}")
    public ResponseEntity<?> getServiceInfo(@PathVariable Integer ticketMasId) {
        List<SdTicketServiceData> serviceInfo = ticketFacade.getServiceInfo(ticketMasId);
        if (CollectionUtils.isEmpty(serviceInfo)) {
            return ResponseEntity.badRequest().body("Service info not found.");
        } else {
            return ResponseEntity.ok(serviceInfo);
        }
    }
}
