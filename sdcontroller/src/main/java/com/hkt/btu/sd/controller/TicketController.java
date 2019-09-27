package com.hkt.btu.sd.controller;

import com.hkt.btu.common.facade.data.PageData;
import com.hkt.btu.sd.controller.response.SimpleAjaxResponse;
import com.hkt.btu.sd.controller.response.helper.ResponseEntityHelper;
import com.hkt.btu.sd.facade.SdRequestCreateFacade;
import com.hkt.btu.sd.facade.SdTicketFacade;
import com.hkt.btu.sd.facade.SdUserRoleFacade;
import com.hkt.btu.sd.facade.WfmApiFacade;
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
    @Resource(name = "wfmApiFacade")
    WfmApiFacade wfmApiFacade;

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
        return ticketFacade.getTicket(ticketId).filter(sdTicketMasData -> userRoleFacade.checkSameTeamRole(principal.getName(), sdTicketMasData.getCreateBy()))
                .map(sdTicketMasData -> {
                    ModelAndView modelAndView = new ModelAndView("ticket/ticket_info");
                    modelAndView.addObject("customerCode", sdTicketMasData.getCustCode())
                            .addObject("ticketMasId", sdTicketMasData.getTicketMasId());
                    return modelAndView;
                }).orElse(new ModelAndView("redirect:/ticket/search-ticket"));
    }

    @PostMapping("contact/update")
    public ResponseEntity<?> updateContactInfo(@RequestBody List<SdTicketContactData> contactList) {
        String errorMsg = ticketFacade.updateContactInfo(contactList);
        return ResponseEntity.ok(errorMsg);
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

    @GetMapping("/ajax-get-fault")
    public ResponseEntity<?> getFaultInfo(@RequestParam int subscriberId) {
        // todo: api for BES
        //    API:	 	GetSubFaultBySubscriberId
        //    Original:	http://{domain}/saws/api/v3/Request/GetSubFaultBySubscriberId?subscriberId={subscriberId}
        //    New: 		http://{domain}/servicedesk/ticket/ajax-get-fault?subscriberId={subscriberId}
        return ResponseEntity.ok(null);
    }

    @PostMapping("submit")
    public ResponseEntity<?> submit(WfmRequestDetailsBeanDate wfmRequestDetailsBeanDate, Principal principal) {
        Integer jobId = wfmApiFacade.createJob(wfmRequestDetailsBeanDate, principal.getName());
        return ResponseEntity.ok(jobId);
    }

    @GetMapping("ajax-search-ticket-remarks")
    public ResponseEntity<?> ajaxSearchTicketRemarks(@RequestParam Integer ticketMasId) {
        List<SdTicketRemarkData> dataList = ticketFacade.getTicketRemarksByTicketId(ticketMasId);
        return ResponseEntity.ok(dataList);
    }

    @PostMapping("/post-create-ticket-remarks")
    public ResponseEntity<?> createTicketRemarks(@RequestParam Integer ticketMasId, @RequestParam String remarksType, @RequestParam String remarks) {
        String errorMsg = ticketFacade.createTicketRemarks(ticketMasId, remarksType, remarks);
        if (errorMsg == null) {
            return ResponseEntity.ok(SimpleAjaxResponse.of());
        } else {
            return ResponseEntity.ok(SimpleAjaxResponse.of(false, errorMsg));
        }
    }
}
