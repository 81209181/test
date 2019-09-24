package com.hkt.btu.sd.controller;

import com.hkt.btu.common.facade.data.PageData;
import com.hkt.btu.sd.controller.response.helper.ResponseEntityHelper;
import com.hkt.btu.sd.facade.SdRequestCreateFacade;
import com.hkt.btu.sd.facade.SdTicketFacade;
import com.hkt.btu.sd.facade.SdUserRoleFacade;
import com.hkt.btu.sd.facade.data.*;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import java.security.Principal;
import java.util.List;

@RequestMapping("ticket")
@Controller
public class TicketController {

    @Resource(name = "requestCreateFacade")
    SdRequestCreateFacade requestCreateFacade;
    @Resource(name = "ticketFacade")
    SdTicketFacade ticketFacade;
    @Resource(name = "userRoleFacade")
    SdUserRoleFacade userRoleFacade;

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
    @ResponseBody
    public int createQueryTicket(String custCode, String serviceNo, String serviceType) {
        return ticketFacade.createQueryTicket(custCode, serviceNo, serviceType);
    }

    @GetMapping("{ticketId}")
    public ModelAndView showQueryTicket(@PathVariable Integer ticketId, Principal principal) {
        ModelAndView modelAndView = new ModelAndView("ticket/ticket_info");
        ticketFacade.getTicket(ticketId)
                .filter(sdTicketMasData -> userRoleFacade.checkSameTeamRole(principal.getName(), sdTicketMasData.getCreateBy()))
                .ifPresentOrElse(sdTicketMasData -> {
                    modelAndView.addObject("customerCode", sdTicketMasData.getCustCode())
                            .addObject("ticketMasId", sdTicketMasData.getTicketMasId());
                }, () -> {
                    modelAndView.setViewName("redirect:/ticket/search-ticket");
                });
        return modelAndView;
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

    @GetMapping("/search-ticket")
    public String searchTicket() {
        return "ticket/searchTicket";
    }

    @GetMapping("/searchTicket")
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

    @GetMapping("/my-ticket")
    public String myTicket() {
        return "ticket/myTicket";
    }

    @GetMapping("/myTicket")
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

    @PostMapping("/service/update")
    public ResponseEntity<?> updateServiceInfo(@RequestBody List<RequestTicketServiceData> ticketServiceList) {
        String errorMsg = ticketFacade.updateServiceInfo(ticketServiceList);
        if (StringUtils.isEmpty(errorMsg)) {
            return ResponseEntity.ok("Update success.");
        } else {
            return ResponseEntity.badRequest().body(errorMsg);
        }
    }

    @GetMapping("/remark/{ticketMasId}")
    public ResponseEntity<?> getRemarkInfo(@PathVariable Integer ticketMasId) {
        List<SdTicketRemarkData> data = ticketFacade.getRemarkInfo(ticketMasId);
        return ResponseEntity.ok(data);
    }

    @PostMapping("/remark/update")
    public ResponseEntity<?> updateRemark(@RequestBody List<SdTicketRemarkData> remarkList) {
        ticketFacade.updateRemark(remarkList);
        return ResponseEntity.ok("Update success.");
    }
}
