package com.hkt.btu.sd.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.hkt.btu.common.facade.data.PageData;
import com.hkt.btu.sd.controller.response.SimpleAjaxResponse;
import com.hkt.btu.sd.controller.response.helper.ResponseEntityHelper;
import com.hkt.btu.sd.facade.*;
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
import java.util.Optional;

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

    @GetMapping("service-identity")
    public String serviceIdentity() {
        return "ticket/service_identity";
    }

    @PostMapping("search-service")
    public ResponseEntity<?> searchService(String searchKey, String searchValue) {
        RequestCreateSearchResultsData resultsData = requestCreateFacade.searchProductList(searchKey, searchValue);
        if (!StringUtils.isEmpty(resultsData.getErrorMsg())) {
            return ResponseEntity.badRequest().body(resultsData.getErrorMsg());
        } else {
            return ResponseEntity.ok(resultsData.getList());
        }
    }

    @PostMapping("query/create")
    public ResponseEntity<?> createQueryTicket(String custCode, String serviceNo, String serviceType, String subsId) {
        if (StringUtils.isEmpty(serviceNo) || StringUtils.isEmpty(serviceType)) {
            return ResponseEntity.badRequest().body("Service No. / Service Type is empty.");
        }
        List<SdTicketMasData> dataList = ticketFacade.getTicketByServiceNo(serviceNo);
        if (CollectionUtils.isNotEmpty(dataList)) {
            return ResponseEntity.ok(ResponseTicketData.of(false, dataList));
        }
        return ResponseEntity.ok(ResponseTicketData.of(true, ticketFacade.createQueryTicket(custCode, serviceNo, serviceType, subsId)));
    }

    @GetMapping("")
    public ModelAndView showQueryTicket(Principal principal, int ticketMasId) {
        return ticketFacade.getTicket(ticketMasId)
                .filter(sdTicketMasData -> userRoleFacade.checkSameTeamRole(principal.getName(), sdTicketMasData.getCreateBy()))
                .map(sdTicketMasData -> {
                    ModelAndView modelAndView = new ModelAndView("ticket/ticket_info");
                    modelAndView.addObject("ticketInfo", requestCreateFacade.getTicketInfo(sdTicketMasData));
                    return modelAndView;
                }).orElse(new ModelAndView("redirect:/ticket/search-ticket"));
    }

    @PostMapping("contact/update")
    public ResponseEntity<?> updateContactInfo(@RequestBody List<SdTicketContactData> contactList) {
        if (contactList.stream().map(SdTicketContactData::getTicketMasId).findFirst()
                .filter(ticketMasId -> ticketFacade.isCancel(String.valueOf(ticketMasId))).isPresent()) {
            return ResponseEntity.badRequest().body("The ticket has been cancelled.");
        }
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
                                          @RequestParam(required = false) String status,
                                          @RequestParam(required = false) String ticketMasId,
                                          @RequestParam(required = false) String custCode) {
        int page = start / length;
        Pageable pageable = PageRequest.of(page, length);

        PageData<SdTicketMasData> pageData = ticketFacade.searchTicketList(pageable, dateFrom, dateTo, status, ticketMasId, custCode);
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
        if (ticketServiceList.stream().map(RequestTicketServiceData::getTicketMasId).findFirst()
                .filter(ticketMasId -> ticketFacade.isCancel(String.valueOf(ticketMasId))).isPresent()) {
            return ResponseEntity.badRequest().body("The ticket has been cancelled.");
        }
        String errorMsg = ticketFacade.updateServiceInfo(ticketServiceList);
        if (StringUtils.isEmpty(errorMsg)) {
            return ResponseEntity.ok("Update success.");
        } else {
            return ResponseEntity.badRequest().body(errorMsg);
        }
    }

    @GetMapping("/service/symptom/{ticketMasId}")
    public ResponseEntity<?> getSymptom(@PathVariable Integer ticketMasId) {
        List<SdSymptomData> symptomData = ticketFacade.getSymptom(ticketMasId);
        if (CollectionUtils.isEmpty(symptomData)) {
            return ResponseEntity.badRequest().body("Symptom info not found.");
        } else {
            return ResponseEntity.ok(symptomData);
        }
    }


    @GetMapping("/ajax-get-fault")
    public ResponseEntity<?> getFaultInfo(@RequestParam String subscriberId) {
        return ResponseEntity.ok(ticketFacade.getFaultInfo(subscriberId));
    }

    @PostMapping("submit")
    public ResponseEntity<?> submit(Principal principal, WfmRequestDetailsBeanDate wfmRequestDetailsBeanDate) throws JsonProcessingException {
        if (ticketFacade.isCancel(wfmRequestDetailsBeanDate.getTicketMasId())) {
            return ResponseEntity.badRequest().body("The ticket has been cancelled.");
        }
        if (ticketFacade.getService(Integer.valueOf(wfmRequestDetailsBeanDate.getTicketMasId())).map(SdTicketServiceData::getJobId).isPresent()) {
            return ResponseEntity.badRequest().body("This ticket has been submitted.");
        }
        Integer jobId = wfmApiFacade.createJob(wfmRequestDetailsBeanDate, principal.getName());
        if (jobId > 0) {
            ticketFacade.updateJobIdInService(jobId, wfmRequestDetailsBeanDate.getTicketMasId(), principal.getName());
            ObjectMapper mapper = new ObjectMapper();
            ObjectNode node = mapper.createObjectNode();
            node.put("success", true);
            return ResponseEntity.ok(mapper.writeValueAsString(node));
        } else {
            return ResponseEntity.badRequest().body("Submit fail.");
        }
    }

    @ResponseBody
    @PostMapping("cancel")
    public SimpleAjaxResponse cancel(Principal principal, int ticketMasId) {
        ticketFacade.cancelTicket(ticketMasId, principal.getName());
        return SimpleAjaxResponse.of();
    }

    @GetMapping("ajax-search-ticket-remarks")
    public ResponseEntity<?> ajaxSearchTicketRemarks(@RequestParam Integer ticketMasId) {
        List<SdTicketRemarkData> dataList = ticketFacade.getTicketRemarksByTicketId(ticketMasId);
        return ResponseEntity.ok(dataList);
    }

    @PostMapping("/post-create-ticket-remarks")
    public ResponseEntity<?> createTicketRemarks(@RequestParam Integer ticketMasId, @RequestParam String remarks) {
        if (ticketFacade.isCancel(String.valueOf(ticketMasId))) {
            return ResponseEntity.badRequest().body("The ticket has been cancelled.");
        }
        String errorMsg = ticketFacade.createTicketRemarks(ticketMasId, remarks);
        if (errorMsg == null) {
            return ResponseEntity.ok(SimpleAjaxResponse.of());
        } else {
            return ResponseEntity.ok(SimpleAjaxResponse.of(false, errorMsg));
        }
    }

    @PostMapping("appointment/update")
    public ResponseEntity<?> updateAppointment(String appointmentDate, boolean asap, Principal principal, String ticketMasId) {
        if (ticketFacade.isCancel(ticketMasId)) {
            return ResponseEntity.badRequest().body("The ticket has been cancelled.");
        }
        if (!asap) {
            if (!ticketFacade.checkAppointmentDate(appointmentDate)) {
                return ResponseEntity.badRequest().body("The appointment time must be two hours later.");
            }
        }
        ticketFacade.updateAppointment(appointmentDate, asap, principal.getName(), ticketMasId);
        return ResponseEntity.ok("Update appointment success.");
    }

    @GetMapping("/ajax-get-ticket")
    public ResponseEntity<?> getTicketInfo(@RequestParam Integer ticketMasId) {
        SdTicketData ticketData = ticketFacade.getTicketInfo(ticketMasId);
        return ResponseEntity.ok(ticketData);
    }
}
