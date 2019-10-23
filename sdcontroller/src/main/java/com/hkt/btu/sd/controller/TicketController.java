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
import org.springframework.ui.Model;
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

    @Resource(name = "norarsApiFacade")
    NorarsApiFacade norarsApiFacade;

    @GetMapping("service-identity")
    public String serviceIdentity(Model model ) {
        model.addAttribute("serviceSearchKeyList", requestCreateFacade.getSearchKeyEnumList() );
        return "ticket/serviceIdentity";
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
    public ResponseEntity<?> createQueryTicket(QueryTicketRequestData queryTicketRequestData) {
        if (StringUtils.isEmpty(queryTicketRequestData.getServiceNo()) || StringUtils.isEmpty(queryTicketRequestData.getServiceType())) {
            return ResponseEntity.badRequest().body("Service No. / Service Type is empty.");
        }
        String returnCode = wfmApiFacade.getPendingOrder(queryTicketRequestData.getServiceNo());
        if (StringUtils.isNotEmpty(returnCode)) {
            return ResponseEntity.badRequest().body("There is pending order of the service " + returnCode + " in WFM.");
        }
        List<SdTicketMasData> dataList = ticketFacade.getTicketByServiceNo(queryTicketRequestData.getServiceNo());
        if (CollectionUtils.isNotEmpty(dataList)) {
            return ResponseEntity.ok(ResponseTicketData.of(false, dataList));
        }
        return ResponseEntity.ok(ResponseTicketData.of(true, ticketFacade.createQueryTicket(queryTicketRequestData)));
    }

    @GetMapping("")
    public ModelAndView showQueryTicket(Principal principal, int ticketMasId) {
        return ticketFacade.getTicket(ticketMasId)
                .filter(sdTicketMasData -> userRoleFacade.checkSameTeamRole(principal.getName(), sdTicketMasData.getCreateBy()))
                .map(sdTicketMasData -> {
                    ModelAndView modelAndView = new ModelAndView("ticket/ticketInfo");
                    modelAndView.addObject("ticketInfo", requestCreateFacade.getTicketInfo(sdTicketMasData));
                    return modelAndView;
                }).orElse(new ModelAndView("redirect:/ticket/search-ticket"));
    }

    @PostMapping("contact/update")
    public ResponseEntity<?> updateContactInfo(@RequestBody List<SdTicketContactData> contactList) {
        try {
            contactList.stream().map(SdTicketContactData::getTicketMasId).findFirst().ifPresent(ticketMasId -> {
                ticketFacade.isAllow(String.valueOf(ticketMasId), SdTicketMasData.ACTION_TYPE.COMPLETE);
            });
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
        String errorMsg = ticketFacade.updateContactInfo(contactList);
        return ResponseEntity.ok(errorMsg);
    }

    @GetMapping("/contact")
    public ResponseEntity<?> getContactInfo(@RequestParam Integer ticketMasId) {
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

    @GetMapping("/service")
    public ResponseEntity<?> getServiceInfo(@RequestParam Integer ticketMasId) {
        List<SdTicketServiceData> serviceInfo = ticketFacade.getServiceInfo(ticketMasId);
        return ResponseEntity.ok(serviceInfo);
    }

    @PostMapping("/service/update")
    public ResponseEntity<?> updateServiceInfo(@RequestBody List<RequestTicketServiceData> ticketServiceList) {
        try {
            ticketServiceList.stream().map(RequestTicketServiceData::getTicketMasId).findFirst().ifPresent(ticketMasId -> {
                ticketFacade.isAllow(String.valueOf(ticketMasId), StringUtils.EMPTY);
            });
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
        String errorMsg = ticketFacade.updateServiceInfo(ticketServiceList);
        if (StringUtils.isEmpty(errorMsg)) {
            return ResponseEntity.ok("Update success.");
        } else {
            return ResponseEntity.badRequest().body(errorMsg);
        }
    }

    @GetMapping("/service/symptom")
    public ResponseEntity<?> getSymptom(@RequestParam Integer ticketMasId) {
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
        try {
            ticketFacade.isAllow(wfmRequestDetailsBeanDate.getTicketMasId(), StringUtils.EMPTY);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
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

    @GetMapping("ajax-search-ticket-remarks")
    public ResponseEntity<?> ajaxSearchTicketRemarks(@RequestParam Integer ticketMasId) {
        List<SdTicketRemarkData> dataList = ticketFacade.getTicketRemarksByTicketId(ticketMasId);
        return ResponseEntity.ok(dataList);
    }

    @PostMapping("/post-create-ticket-remarks")
    public ResponseEntity<?> createTicketRemarks(@RequestParam Integer ticketMasId, @RequestParam String remarks) {
        try {
            ticketFacade.isAllow(String.valueOf(ticketMasId), SdTicketMasData.ACTION_TYPE.COMPLETE);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
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

    @PostMapping("close")
    public ResponseEntity<?> ticketClose(int ticketMasId, String reasonType, String reasonContent, Principal principal) {
        String errorMsg = ticketFacade.closeTicket(ticketMasId, reasonType, reasonContent, principal.getName());
        if(StringUtils.isEmpty(errorMsg)){
            return ResponseEntity.ok(SimpleAjaxResponse.of());
        }else {
            return ResponseEntity.ok(SimpleAjaxResponse.of(false, errorMsg));
        }
    }

    @PostMapping("callInCount")
    public ResponseEntity<?> callInCount(@RequestParam Integer ticketMasId) {
        boolean result = ticketFacade.increaseCallInCount(ticketMasId);
        if (result) {
            return ResponseEntity.ok(SimpleAjaxResponse.of());
        } else {
            return ResponseEntity.ok(SimpleAjaxResponse.of(false, "increase call in count failed."));
        }
    }

    @PostMapping("getJobInfo")
    public ResponseEntity<?> getJobInfo(@RequestParam Integer ticketMasId) {
        WfmJobInfoResponseData jobInfo = wfmApiFacade.getJobInfo(ticketMasId);
        if (jobInfo == null) {
            return ResponseEntity.badRequest().body("WFM Error: Cannot get job data for ticket mas id :" + ticketMasId);
        } else {
            return ResponseEntity.ok(jobInfo);
        }
    }

    @PostMapping("getInventory")
    public ResponseEntity<?> getInventory(@RequestParam String bsn) {
        String inventory = norarsApiFacade.getInventory(bsn);
        if (StringUtils.isNotEmpty(inventory)) {
            return ResponseEntity.ok(inventory);
        }
        return ResponseEntity.badRequest().body("Nora Error: Cannot get Inventory for bsn :" + bsn);
    }
}
