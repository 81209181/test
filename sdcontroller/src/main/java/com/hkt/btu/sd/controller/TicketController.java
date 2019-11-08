package com.hkt.btu.sd.controller;

import com.hkt.btu.common.facade.data.PageData;
import com.hkt.btu.sd.controller.response.SimpleAjaxResponse;
import com.hkt.btu.sd.controller.response.helper.ResponseEntityHelper;
import com.hkt.btu.sd.facade.*;
import com.hkt.btu.sd.facade.data.*;
import com.hkt.btu.sd.facade.data.nora.NoraAccountData;
import com.hkt.btu.sd.facade.data.nora.NoraBroadbandInfoData;
import com.hkt.btu.sd.facade.data.nora.NoraDnGroupData;
import com.hkt.btu.sd.facade.data.wfm.WfmJobData;
import com.hkt.btu.sd.facade.data.wfm.WfmPendingOrderData;
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
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RequestMapping("/ticket")
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
    @Resource(name = "serviceTypeFacade")
    SdServiceTypeFacade serviceTypeFacade;

    @Resource(name = "norarsApiFacade")
    NorarsApiFacade norarsApiFacade;
    @Resource(name = "cloudApiFacade")
    CloudApiFacade cloudApiFacade;

    @GetMapping("service-identity")
    public String serviceIdentity(Model model) {
        model.addAttribute("serviceSearchKeyList", requestCreateFacade.getSearchKeyEnumList());
        return "ticket/serviceIdentity";
    }

    @PostMapping("search-service")
    public ResponseEntity<?> searchService(String searchKey, String searchValue, HttpServletRequest request) {
        // check pending ticket
        List<SdTicketMasData> pendingTicketDataList = ticketFacade.getPendingTicketList(searchValue);

        // search service
        RequestCreateSearchResultsData resultsData = requestCreateFacade.searchProductList(searchKey, searchValue);
        if (StringUtils.isNotEmpty(resultsData.getErrorMsg())) {
            return ResponseEntity.badRequest().body(resultsData.getErrorMsg());
        } else {
            if(CollectionUtils.isNotEmpty(pendingTicketDataList)){
                String warningMsg = String.format("The service number already exists in Ticket - <a href='%s/ticket?ticketMasId=%d'>%d</a>",
                        request.getContextPath(), pendingTicketDataList.get(0).getTicketMasId(), pendingTicketDataList.get(0).getTicketMasId());
                resultsData.setWarningMsg(warningMsg);
            }
            return ResponseEntity.ok(resultsData);
        }
    }

    @PostMapping("service-identity/checkPendingOrder")
    public ResponseEntity<?> checkPendingOrder(QueryTicketRequestData queryTicketRequestData) {
        if (StringUtils.isEmpty(queryTicketRequestData.getServiceNo()) || StringUtils.isEmpty(queryTicketRequestData.getServiceType())) {
            return ResponseEntity.badRequest().body("Service No. / Service Type is empty.");
        }

        // check wfm pending order
        boolean checkPendingOrder = serviceTypeFacade.needCheckPendingOrder(queryTicketRequestData.getServiceType());
        if (checkPendingOrder) {
            WfmPendingOrderData pendingOrderData = wfmApiFacade.getPendingOrderByBsn(queryTicketRequestData.getServiceNo());
            if (StringUtils.isEmpty(pendingOrderData.getErrorMsg())) {
                if (pendingOrderData.getOrderId() != null && pendingOrderData.getOrderId() != 0) {
                    return ResponseEntity.ok(ResponseTicketData.of(false, pendingOrderData.getOrderId()));
                }
            } else {
                return ResponseEntity.badRequest().body(pendingOrderData.getErrorMsg());
            }
        }

        return ResponseEntity.ok(SimpleAjaxResponse.of());
    }

    @PostMapping("service-identity/createQueryTicket")
    public ResponseEntity<?> createQueryTicket(QueryTicketRequestData queryTicketRequestData) {
        if (StringUtils.isEmpty(queryTicketRequestData.getServiceNo()) || StringUtils.isEmpty(queryTicketRequestData.getServiceType())) {
            return ResponseEntity.badRequest().body("Service No. / Service Type is empty.");
        }

        // check pending ticket of same service number
        List<SdTicketMasData> dataList = ticketFacade.getPendingTicketList(queryTicketRequestData.getServiceNo());
        if (CollectionUtils.isNotEmpty(dataList)) {
            return ResponseEntity.ok(ResponseTicketData.of(false, dataList));
        }

        // create ticket
        return ResponseEntity.ok(ResponseTicketData.of(true, ticketFacade.createQueryTicket(queryTicketRequestData)));
    }

    @GetMapping("")
    public ModelAndView showQueryTicket(int ticketMasId) {
        return ticketFacade.getTicket(ticketMasId).map(sdTicketMasData -> {
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
    public String searchTicket(Model model) {
        List<CodeDescData> ticketStatusList = ticketFacade.getTicketStatusList();
        if(CollectionUtils.isNotEmpty(ticketStatusList)){
            model.addAttribute("ticketStatusList", ticketStatusList);
        }

        List<CodeDescData> ticketTypeList = ticketFacade.getTicketTypeList();
        if(CollectionUtils.isNotEmpty(ticketTypeList)){
            model.addAttribute("ticketTypeList", ticketTypeList);
        }

        List<SdServiceTypeData> serviceTypeList = serviceTypeFacade.getServiceTypeList();
        if (CollectionUtils.isNotEmpty(serviceTypeList)) {
            model.addAttribute("serviceTypeList", serviceTypeList);
        }

        return "ticket/searchTicket";
    }

    @GetMapping("/searchTicket")
    public ResponseEntity<?> searchTicket(@RequestParam(defaultValue = "0") int draw,
                                          @RequestParam(defaultValue = "0") int start,
                                          @RequestParam(defaultValue = "10") int length,
                                          @RequestParam Map<String, String> searchFormData) {
        int page = start / length;
        Pageable pageable = PageRequest.of(page, length);

        PageData<SdTicketMasData> pageData = ticketFacade.searchTicketList(pageable, searchFormData);
        return ResponseEntityHelper.buildDataTablesResponse(draw, pageData);
    }

    @GetMapping("/my-ticket")
    public String myTicket() {
        return "ticket/myTicket";
    }

    @GetMapping("/myTicket")
    public ResponseEntity<?> getMyTicket(@RequestParam(defaultValue = "0") int draw,
                                         @RequestParam(defaultValue = "0") int start,
                                         @RequestParam(defaultValue = "10") int length) {
        int page = start / length;
        Pageable pageable = PageRequest.of(page, length);

        PageData<SdTicketMasData> pageData = ticketFacade.getMyTicket(pageable);
        return ResponseEntityHelper.buildDataTablesResponse(draw, pageData);
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
    public ResponseEntity<?> submit(SdTicketMasData ticketMasData) {
        try {
            ticketFacade.isAllow(String.valueOf(ticketMasData.getTicketMasId()), StringUtils.EMPTY);
            ticketFacade.createJob4Wfm(ticketMasData.getTicketMasId());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
        return ResponseEntity.ok(SimpleAjaxResponse.of());
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

    @GetMapping("/ajax-get-ticket")
    public ResponseEntity<?> getTicketInfo(@RequestParam Integer ticketMasId) {
        SdTicketData ticketData = ticketFacade.getTicketInfo(ticketMasId);
        return ResponseEntity.ok(ticketData);
    }

    @PostMapping("close")
    public ResponseEntity<?> ticketClose(int ticketMasId, String reasonType, String reasonContent,String contactNumber,String contactName) {
        String errorMsg = ticketFacade.closeTicket(ticketMasId, reasonType, reasonContent,contactName,contactNumber);
        if (StringUtils.isEmpty(errorMsg)) {
            return ResponseEntity.ok(SimpleAjaxResponse.of());
        } else {
            return ResponseEntity.badRequest().body(errorMsg);
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
        List<WfmJobData> jobInfo = wfmApiFacade.getJobInfo(ticketMasId);
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

    @GetMapping("/offer-info")
    public String getOfferInfo(final Model model,
                               @RequestParam String bsn,
                               @ModelAttribute("noraDnGroupData") NoraDnGroupData noraDnGroupData) {
        noraDnGroupData = norarsApiFacade.getRelatedOfferInfoListByBsn(bsn);
        if (noraDnGroupData != null) {
            model.addAttribute("noraDnGroupData", noraDnGroupData);
        }
        return "ticket/offerInfo";
    }

    @GetMapping("/offer-detail")
    public String getOfferInfo(final Model model,
                               @RequestParam String bsn,
                               @ModelAttribute("noraBroadbandInfoData") NoraBroadbandInfoData noraBroadbandInfoData) {
        noraBroadbandInfoData = norarsApiFacade.getOfferDetailListByBsn(bsn);
        if (noraBroadbandInfoData != null) {
            model.addAttribute("noraBroadbandInfoData", noraBroadbandInfoData);
        }
        return "ticket/offerDetail";
    }

    @GetMapping("/getNGN3OneDayAdminAccount/{bsn}")
    public ResponseEntity<?> getNGN3OneDayAdminAccount(@PathVariable String bsn) {
        NoraAccountData oneDayAdminAccount = norarsApiFacade.getNGN3OneDayAdminAccount(bsn);
        if (oneDayAdminAccount == null) {
            return ResponseEntity.badRequest().body("NGN3 one day admin account not found.");
        }
        return ResponseEntity.ok(oneDayAdminAccount);
    }

    @GetMapping("/getAppointmentInfo")
    public ResponseEntity<?> getAppointmentInfo(@RequestParam Integer ticketMasId) {
        AppointmentData appointmentData = ticketFacade.getAppointmentData(ticketMasId);
        if (appointmentData != null) {
            return ResponseEntity.ok(appointmentData);
        }

        return ResponseEntity.badRequest().body("WFM Error: Cannot get appointment info for ticketMasId:" + ticketMasId);
    }

    @GetMapping("getNgn3AccountList/{bsn}")
    @ResponseBody
    public List<String> getNgn3AccountList(@PathVariable String bsn) {
        return Optional.ofNullable(norarsApiFacade.getRelatedOfferInfoListByBsn(bsn)).map(NoraDnGroupData::getAdminPortalId).map(s -> List.of(s.split(","))).orElse(List.of());
    }

    @GetMapping("resetNgn3Pwd/{account}")
    public ResponseEntity<?> resetNgn3Pwd(@PathVariable String account) {
        try {
            return ResponseEntity.ok(cloudApiFacade.resetNgn3Pwd(account));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Reset password fail.");
        }
    }

    @CrossOrigin
    @GetMapping("makeAppointment")
    public ResponseEntity<?> makeAppointment() {
        String html = wfmApiFacade.postAppointmentForm();
        return ResponseEntity.ok(html);
    }
}
