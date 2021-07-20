package com.hkt.btu.sd.controller;

import com.hkt.btu.common.core.exception.InvalidInputException;
import com.hkt.btu.common.facade.data.BtuCodeDescData;
import com.hkt.btu.common.facade.data.BtuPageData;
import com.hkt.btu.common.facade.data.PageData;
import com.hkt.btu.sd.controller.response.SimpleAjaxResponse;
import com.hkt.btu.sd.controller.response.helper.ResponseEntityHelper;
import com.hkt.btu.sd.facade.*;
import com.hkt.btu.sd.facade.data.*;
import com.hkt.btu.sd.facade.data.gmb.GmbErrorData;
import com.hkt.btu.sd.facade.data.norars.NoraAccountData;
import com.hkt.btu.sd.facade.data.norars.NoraBroadbandInfoData;
import com.hkt.btu.sd.facade.data.oss.OssSmartMeterEventData;
import com.hkt.btu.sd.facade.data.wfm.WfmJobData;
import com.hkt.btu.sd.facade.data.wfm.WfmMakeApptData;
import com.hkt.btu.sd.facade.data.wfm.WfmPendingOrderData;
import com.hkt.btu.sd.facade.data.wfm.WfmResponseTokenData;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.MimeTypeUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RequestMapping("/ticket")
@Controller
public class TicketController {
    private static final Logger LOG = LogManager.getLogger(TicketController.class);

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
    @Resource(name = "userFacade")
    SdUserFacade userFacade;
    @Resource(name = "serviceTypeUserRoleFacade")
    SdServiceTypeUserRoleFacade serviceTypeUserRoleFacade;

    @Resource(name = "norarsApiFacade")
    NorarsApiFacade norarsApiFacade;
    @Resource(name = "ossApiFacade")
    OssApiFacade ossApiFacade;
    @Resource(name = "gmbApiFacade")
    GmbApiFacade gmbApiFacade;

    public static final String O_CLOUD_SH = "O_CLOUD_SH";

    @GetMapping("service-identity")
    public String serviceIdentity(Model model) {
        SdUserData userdata = userFacade.getCurrentUser();
        String userId = userdata.getUserId();
        EditResultData result = userRoleFacade.getUserRoleByUserId(userId, false);
        List<String> userRole = result == null ? null : (List<String>) result.getList();

        model.addAttribute("serviceSearchKeyList", serviceTypeUserRoleFacade.getServiceSearchKeyList(userRole));
        return "ticket/serviceIdentity";
    }

    @PostMapping("search-service")
    public ResponseEntity<?> searchService(String searchKey, String searchValue, HttpServletRequest request) {
        // search service
        SdRequestCreateSearchResultsData resultsData = requestCreateFacade.searchProductList(searchKey, searchValue);
        if (resultsData==null){
            return ResponseEntity.badRequest().body("Cannot get search service response.");
        } else if (StringUtils.isNotEmpty(resultsData.getErrorMsg())) {
            return ResponseEntity.badRequest().body(resultsData.getErrorMsg());
        }

        // auto check pending ticket for single result
        List<SdRequestCreateSearchResultData> searchResultDataList = resultsData.getList();
        if( CollectionUtils.size(searchResultDataList)==1 ){
            SdRequestCreateSearchResultData searchResultData = searchResultDataList.get(0);
            List<SdTicketMasData> pendingTicketDataList = ticketFacade.getPendingTicketList(searchResultData.getServiceType(), searchResultData.getServiceNo());
            if (CollectionUtils.isNotEmpty(pendingTicketDataList)) {
                String warningMsg = String.format("The service number already exists in Ticket - <a href='%s/ticket?ticketMasId=%d'>%d</a>",
                        request.getContextPath(), pendingTicketDataList.get(0).getTicketMasId(), pendingTicketDataList.get(0).getTicketMasId());
                resultsData.setWarningMsg(warningMsg);
            }
        }

        return ResponseEntity.ok(resultsData);
    }

    @PostMapping("service-identity/checkPendingOrder")
    public ResponseEntity<?> checkPendingOrder(SdQueryTicketRequestData queryTicketRequestData) {
        if (StringUtils.isEmpty(queryTicketRequestData.getServiceNo()) || StringUtils.isEmpty(queryTicketRequestData.getServiceType())) {
            return ResponseEntity.badRequest().body("Service No. / Service Type is empty.");
        }

        // check wfm pending order
        boolean checkPendingOrder = serviceTypeFacade.needCheckPendingOrder(queryTicketRequestData.getServiceType());
        if (checkPendingOrder) {
            WfmPendingOrderData pendingOrderData = wfmApiFacade.getPendingOrderByBsn(queryTicketRequestData.getServiceNo());
            if (pendingOrderData == null) {
                return ResponseEntity.badRequest().body("No pending order response from WFM.");
            } else if (StringUtils.isNotEmpty(pendingOrderData.getErrorMsg())) {
                return ResponseEntity.badRequest().body(pendingOrderData.getErrorMsg());
            } else if (pendingOrderData.getOrderId() != null) {
                return ResponseEntity.ok(SdResponseTicketData.of(false, pendingOrderData.getOrderId()));
            }
        }

        return ResponseEntity.ok(SimpleAjaxResponse.of());
    }

    @PostMapping("service-identity/createQueryTicket")
    public ResponseEntity<?> createQueryTicket(SdQueryTicketRequestData queryTicketRequestData) {
        // check pending ticket of same service number
        List<SdTicketMasData> dataList = ticketFacade.getPendingTicketList(queryTicketRequestData.getServiceType(), queryTicketRequestData.getServiceNo());
        if (CollectionUtils.isNotEmpty(dataList)) {
            return ResponseEntity.ok(SdResponseTicketData.of(false, dataList));
        }

        // create ticket
        try {
            return ResponseEntity.ok(SdResponseTicketData.of(true, ticketFacade.createQueryTicket(queryTicketRequestData)));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("")
    public ModelAndView showQueryTicket(int ticketMasId) {
        return ticketFacade.getTicket(ticketMasId).map(data -> {
            if (data.getOwningRole().equalsIgnoreCase(O_CLOUD_SH)) {
                ModelAndView modelAndView = new ModelAndView("ticket/cloudTicketInfo");
                modelAndView.addObject("ticketInfo", data);
                return modelAndView;
            }
            ModelAndView modelAndView = new ModelAndView("ticket/ticketInfo");
            modelAndView.addObject("ticketInfo", requestCreateFacade.getTicketInfo(data));
            return modelAndView;
        }).orElse(new ModelAndView("redirect:/ticket/search-ticket"));
    }

    @PostMapping("contact/update")
    public ResponseEntity<?> updateContactInfo(@RequestBody List<SdTicketContactData> contactList) {
        try {
            contactList.stream().map(SdTicketContactData::getTicketMasId).findFirst().ifPresent(ticketMasId -> {
                ticketFacade.isAllow(ticketMasId, SdTicketMasData.ACTION_TYPE.COMPLETE);
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
        List<BtuCodeDescData> ticketStatusList = ticketFacade.getTicketStatusList();
        if (CollectionUtils.isNotEmpty(ticketStatusList)) {
            model.addAttribute("ticketStatusList", ticketStatusList);
        }

        List<BtuCodeDescData> ticketTypeList = ticketFacade.getTicketTypeList();
        if (CollectionUtils.isNotEmpty(ticketTypeList)) {
            model.addAttribute("ticketTypeList", ticketTypeList);
        }

        List<SdUserRoleData> userRoleList = userRoleFacade.getCurrentUserUserRole();
        if (CollectionUtils.isNotEmpty(userRoleList)) {
            List<String> userRoleId = userRoleList.stream().map(SdUserRoleData::getRoleId).collect(Collectors.toList());
            List<SdServiceTypeData> serviceTypeList = serviceTypeFacade.getServiceTypeByRoleId(userRoleId);
            if (CollectionUtils.isNotEmpty(serviceTypeList)) {
                model.addAttribute("serviceTypeList", serviceTypeList);
            }
            model.addAttribute("primaryRoleList", userRoleList.stream().filter(sdUserRoleData -> !StringUtils.equals(sdUserRoleData.getRoleId(), "SYS_ADMIN"))
                    .sorted(Comparator.comparing(SdUserRoleData::getRoleDesc)).collect(Collectors.toList()));
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
    public ResponseEntity<?> updateServiceInfo(@RequestBody List<SdRequestTicketServiceData> ticketServiceList) {
        try {
            ticketServiceList.stream().map(SdRequestTicketServiceData::getTicketMasId).findFirst().ifPresent(ticketMasId -> {
                ticketFacade.isAllow(ticketMasId, StringUtils.EMPTY);
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

    @Deprecated // moved to BesController
    @GetMapping("/ajax-get-fault")
    public ResponseEntity<?> getFaultInfo(@RequestParam String subscriberId) {
        return ResponseEntity.ok(ticketFacade.getFaultInfo(subscriberId,null));
    }

    @PostMapping("submit")
    public ResponseEntity<?> submit(int ticketMasId) {
        try {
            ticketFacade.isAllow(ticketMasId, StringUtils.EMPTY);
            ticketFacade.createJob4Wfm(ticketMasId, true);
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
            ticketFacade.isAllow(ticketMasId, SdTicketMasData.ACTION_TYPE.COMPLETE);
            ticketFacade.createTicketRemarks(ticketMasId, remarks);
            return ResponseEntity.ok(SimpleAjaxResponse.of());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/ajax-get-ticket")
    public ResponseEntity<?> getTicketInfo(@RequestParam Integer ticketMasId) {
        SdTicketData ticketData = ticketFacade.getTicketInfo(ticketMasId);
        return ResponseEntity.ok(ticketData);
    }

    @PostMapping("close")
    public ResponseEntity<?> ticketClose(int ticketMasId, String closeCode, String reasonType, String reasonContent, String contactNumber, String contactName) {
        String errorMsg = ticketFacade.closeTicket(ticketMasId, closeCode, reasonType, reasonContent, contactName, contactNumber);
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
                               @ModelAttribute("dnGroupData") SdDnGroupData dnGroupData) {
        dnGroupData = norarsApiFacade.getDnGroupData(bsn);
        if (dnGroupData != null) {
            model.addAttribute("dnGroupData", dnGroupData);
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
        NoraAccountData oneDayAdminAccount;
        try{
            oneDayAdminAccount = norarsApiFacade.getNgn3OneDayAdminAccount(bsn);
            if (oneDayAdminAccount == null) {
                return ResponseEntity.badRequest().body("Cannot get NGN3 one day admin account.");
            }
        }catch (InvalidInputException e){
            LOG.warn(e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        }

        return ResponseEntity.ok(oneDayAdminAccount);
    }

    @GetMapping("/getAppointmentInfo")
    public ResponseEntity<?> getAppointmentInfo(@RequestParam Integer ticketMasId) {
        SdAppointmentData appointmentData = ticketFacade.getAppointmentData(ticketMasId);
        if (appointmentData != null) {
            return ResponseEntity.ok(appointmentData);
        }

        return ResponseEntity.badRequest().body("WFM Error: Cannot get appointment info for ticketMasId:" + ticketMasId);
    }

    @GetMapping("resetNgn3Pwd/{dn}")
    @ResponseBody
    public ResponseEntity<?> resetNgn3Pwd(@PathVariable String dn) {
        String ngn3ComplexPwd;
        try{
            ngn3ComplexPwd = norarsApiFacade.resetNgn3Account(dn);
            if ( StringUtils.isEmpty(ngn3ComplexPwd) ) {
                return ResponseEntity.badRequest().body("Cannot reset NGN3 account password.");
            } else {
                return ResponseEntity.ok(ngn3ComplexPwd);
            }
        }catch (InvalidInputException e){
            LOG.warn(e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("token")
    public ResponseEntity<?> getToken(Integer ticketDetId, String exchangeId) {
        WfmMakeApptData makeApptData = ticketFacade.getMakeApptDataByTicketDetId(ticketDetId);

        if (makeApptData == null) {
            return ResponseEntity.badRequest().body("Cannot get ticket detail by ticketDetId");
        }
        makeApptData.setExchangeId(exchangeId);

        try {
            WfmResponseTokenData data = wfmApiFacade.getToken(makeApptData);
            if (data != null) {
                return ResponseEntity.ok(data);
            } else {
                return ResponseEntity.badRequest().body("Cannot connect WFM for getting token.");
            }
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("transferToken")
    public ResponseEntity<?> getTransferToken(Integer orderId, Integer jobId) {
        try {
            WfmResponseTokenData data = wfmApiFacade.getTransferToken(orderId, jobId);
            if (data != null) {
                return ResponseEntity.ok(data);
            } else {
                return ResponseEntity.badRequest().body("Cannot connect WFM for getting token.");
            }
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("team-summary")
    public String showTeamSummary(Model model) {
        model.addAttribute("teamSummary", ticketFacade.getTeamSummary());
        return "ticket/teamSummary";
    }

    @GetMapping("get-external-service-data/{serviceTypeCode}/{serviceNumber}")
    public ResponseEntity<?> getExternalServiceData(@PathVariable String serviceTypeCode, @PathVariable String serviceNumber) {
        try {
            SdTicketServiceInfoData serviceInfoData = requestCreateFacade.getServiceInfoInApi(serviceTypeCode, serviceNumber);
            return ResponseEntity.ok(serviceInfoData);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("get-upload-files")
    public ResponseEntity<?> getUploadFiles(int ticketMasId) {
        return ResponseEntity.ok(ticketFacade.getUploadFiles(ticketMasId));
    }

    @GetMapping("/service/ajax-event-of-pole-list")
    public ResponseEntity<?> getEventOfPoleList(@RequestParam(defaultValue = "0") int draw,
                                                @RequestParam(defaultValue = "0") int start,
                                                @RequestParam(defaultValue = "10") int length,
                                                @RequestParam Integer poleId,
                                                @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fromTime,
                                                @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime toTime) {
        int page = start / length;
        toTime = toTime == null ? LocalDateTime.now() : toTime;

        BtuPageData<OssSmartMeterEventData> btuPageData = ossApiFacade.queryMeterEvents(page, length, poleId, fromTime, toTime);

        if (btuPageData == null) {
            return ResponseEntityHelper.buildDataTablesResponse(draw, new PageData());
        } else {
            PageData pageData = new PageData(
                btuPageData.getContent(), page,  length,
                btuPageData.getTotalElements(),  null,  btuPageData.isLast(),
                btuPageData.getTotalPages(), null, btuPageData.isFirst(),
                btuPageData.getNumberOfElements());
            return ResponseEntityHelper.buildDataTablesResponse(draw, pageData);
        }
    }

    @GetMapping("/service/closeCode")
    public ResponseEntity<?> getCloseCode(@RequestParam String serviceType) {
        List<SdCloseCodeData> closeCodeData = ticketFacade.getCloseCode(serviceType);
        if (CollectionUtils.isEmpty(closeCodeData)) {
            return ResponseEntity.badRequest().body("close code not found.");
        } else {
            return ResponseEntity.ok(closeCodeData);
        }
    }

    @GetMapping("/service/ajax-gmb-error-list")
    public ResponseEntity<?> getGmbErrorList(@RequestParam(defaultValue = "0") int draw,
                                                @RequestParam(defaultValue = "0") int start,
                                                @RequestParam(defaultValue = "10") int length,
                                                @RequestParam String plateId) {
        int page = start / length;
        Pageable pageable = PageRequest.of(page, length);

        PageData<GmbErrorData> pageData = gmbApiFacade.getErrorList(pageable, plateId);
        return ResponseEntityHelper.buildDataTablesResponse(draw, pageData);
    }

    @GetMapping("getIddInfo")
    public ResponseEntity<?> getIddInfo(@RequestParam String plateId) {
        return ResponseEntity.ok(gmbApiFacade.getIddInfo(plateId));
    }

    @GetMapping("/search-bchsp")
    public String searchBchsp(Model model) {
        List<BtuCodeDescData> ticketStatusList = ticketFacade.getTicketStatusList();
        if (CollectionUtils.isNotEmpty(ticketStatusList)) {
            model.addAttribute("ticketStatusList", ticketStatusList);
        }

        List<BtuCodeDescData> ticketTypeList = ticketFacade.getTicketTypeList();
        if (CollectionUtils.isNotEmpty(ticketTypeList)) {
            model.addAttribute("ticketTypeList", ticketTypeList);
        }

        List<SdServiceTypeData> serviceTypeList = serviceTypeFacade.getServiceTypeList();
        if (CollectionUtils.isNotEmpty(serviceTypeList)) {
            model.addAttribute("serviceTypeList", serviceTypeList);
        }

        List<SdUserRoleData> eligibleUserRoleList = userRoleFacade.getEligibleUserRoleList();
        if (CollectionUtils.isNotEmpty(eligibleUserRoleList)) {
            model.addAttribute("primaryRoleList", eligibleUserRoleList.stream().filter(SdUserRoleData::isPrimaryRole)
                    .sorted(Comparator.comparing(SdUserRoleData::getRoleDesc)).collect(Collectors.toList()));
        }

        List<String> workGroupList = ticketFacade.getWorkGroupList();
        if (CollectionUtils.isNotEmpty(workGroupList)) {
            model.addAttribute("workGroupList", workGroupList);
        }

        return "ticket/searchBchsp";
    }

    @GetMapping("/searchBchsp")
    public ResponseEntity<?> searchBchsp(@RequestParam(defaultValue = "0") int draw,
                                          @RequestParam(defaultValue = "0") int start,
                                          @RequestParam(defaultValue = "10") int length,
                                          @RequestParam Map<String, String> searchFormData) {
        int page = start / length;
        Pageable pageable = PageRequest.of(page, length);

        PageData<SdTicketMasData> pageData = ticketFacade.searchBchspList(pageable, searchFormData);
        return ResponseEntityHelper.buildDataTablesResponse(draw, pageData);
    }

    @PostMapping("/service/utTestLog")
    @ResponseBody
    public void utTestLog(@RequestParam String ticketMasId){
        String userId = userFacade.getCurrentUser().getUserId();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String localDateTime = LocalDateTime.now().format(formatter);
        LOG.info(String.format("UT GUI trigger: {ticketMasId: %s, userId: %s, date: %s}", ticketMasId, userId, localDateTime));
    }

    @GetMapping("getJobId")
    public ResponseEntity<?> getJobId(@RequestParam String ticketMasId) {
        return ResponseEntity.ok(ticketFacade.getJobId(ticketMasId));
    }

    @ResponseBody
    @GetMapping("/exportExcel")
    public ResponseEntity<?> downloadRequest(@RequestParam Map<String, String> searchFormData) throws IOException {
        List<SdTicketExportData> accessRequestDataList = ticketFacade.searchTicketListForExport(searchFormData);
        if (CollectionUtils.isEmpty(accessRequestDataList)) {
            return ResponseEntity.badRequest().body("Ticket data not found.");
        } else if (accessRequestDataList.size() > 65536) {
            return ResponseEntity.badRequest().body("Ticket data exceeds 65536 row(s).");
        }

        HSSFWorkbook workbook = new HSSFWorkbook();
        ticketFacade.fillSheet(workbook.createSheet("Tickets"), accessRequestDataList);
        String fileName = ticketFacade.getFileName();

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        workbook.write(baos);
        baos.close();

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, String.format("attachment;filename=\"%s", fileName));
        return ResponseEntity.ok()
                .headers(headers)
                .contentType(MediaType.parseMediaType(MimeTypeUtils.APPLICATION_OCTET_STREAM_VALUE))
                .<Object>body(baos.toByteArray());
    }
}
