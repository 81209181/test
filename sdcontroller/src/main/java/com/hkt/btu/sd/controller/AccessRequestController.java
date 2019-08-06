package com.hkt.btu.sd.controller;

import com.hkt.btu.common.facade.data.PageData;
import com.hkt.btu.sd.controller.response.SimpleAjaxResponse;
import com.hkt.btu.sd.controller.response.helper.ResponseEntityHelper;
import com.hkt.btu.sd.facade.SdAccessRequestFacade;
import com.hkt.btu.sd.facade.SdAuditTrailFacade;
import com.hkt.btu.sd.facade.SdUserFacade;
import com.hkt.btu.sd.facade.data.*;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;
import java.util.TreeMap;

@SuppressWarnings({"ParameterCanBeLocal", "SameReturnValue"})
@Controller
@RequestMapping(value = "/user/access-request")
public class AccessRequestController {

    @Resource(name = "accessRequestFacade")
    SdAccessRequestFacade sdAccessRequestFacade;
    @Resource(name = "userFacade")
    SdUserFacade sdUserFacade;
    @Resource(name = "auditTrailFacade")
    SdAuditTrailFacade sdAuditTrailFacade;


    @GetMapping("/create")
    public String createAccessRequest (final Model model,
                                       @ModelAttribute("createAccessForm") CreateAccessRequestFormData createAccessForm,
                                       @ModelAttribute("requestLocMap") TreeMap<String, SdAccessRequestLocData> requestLocMap) {
        if( CollectionUtils.isEmpty(createAccessForm.getVisitorDataList()) ) {
            sdAccessRequestFacade.getPrefillAccessFormData(createAccessForm);
            model.addAttribute("createAccessForm", createAccessForm);
        }

        requestLocMap = sdAccessRequestFacade.getAccessRequestLocMap();
        model.addAttribute("requestLocMap", requestLocMap);

        boolean canOnlySubmitSelfVisit = sdAccessRequestFacade.canOnlySubmitSelfVisit();
        model.addAttribute("canOnlySubmitSelfVisit", canOnlySubmitSelfVisit);

        return "user/accessRequest/createAccessRequest";
    }

    @PostMapping("/create")
    public String createAccessRequest (final RedirectAttributes redirectAttributes,
                                       @ModelAttribute("createAccessForm") CreateAccessRequestFormData createAccessRequestFormData) {
        CreateResultData createResultData  = sdAccessRequestFacade.createAccessRequest(createAccessRequestFormData);
        Integer hashedRequestId = createResultData.getNewId();
        String errorMsg = createResultData.getErrorMsg();

        if(hashedRequestId==null){
            redirectAttributes.addFlashAttribute(PageMsgController.ERROR_MSG, errorMsg);
            redirectAttributes.addFlashAttribute("createAccessForm", createAccessRequestFormData);
            return "redirect:create";
        }else {
            String successMsg = "" +
                    "<div class=\"col-md-12 text-center\">\n" +
                    "    <i class=\"fas fa-check-circle fa-7x\"></i> \n" +
                    "    <h3>Success!</h3> \n" +
                    "    <p>\n" +
                    "        Thank you for submitting the access request. <br>\n" +
                    "        Please present the <u>Ticket Number</u> and <u>all Visitors' Staff ID / HKID / Passport</u> for on-site registration.<br>\n" +
                    "    </p>\n" +
                    "</div>";
            redirectAttributes.addFlashAttribute(PageMsgController.INFO_MSG, successMsg);
            return "redirect:view?hashedRequestId="+hashedRequestId;
        }
    }


    @GetMapping("/re-create")
    public String recreateAccessRequest (final RedirectAttributes redirectAttributes,
                                         @RequestParam Integer hashedRequestId,
                                         @ModelAttribute("createAccessForm") CreateAccessRequestFormData createAccessForm ) {
        SdAccessRequestData sdAccessRequestData = sdAccessRequestFacade.getAccessRequestByRequestId(hashedRequestId);
        if(sdAccessRequestData==null){
            redirectAttributes.addFlashAttribute(PageMsgController.INFO_MSG, "Cannot copy from access request " + hashedRequestId + ".");
            return "redirect:/user/access-request/create";
        }

        sdAccessRequestFacade.getPrefillAccessFormData(createAccessForm);

        // fill-in info from old request
        sdAuditTrailFacade.insertViewRequesterAuditTrail(sdAccessRequestData);
        sdAuditTrailFacade.insertViewRequestVisitorAuditTrail(sdAccessRequestData.getVisitorDataList());
        sdAccessRequestFacade.copyAccessFormData(sdAccessRequestData, createAccessForm);

        redirectAttributes.addFlashAttribute("createAccessForm", createAccessForm);
        redirectAttributes.addFlashAttribute(PageMsgController.INFO_MSG, "Copied from access request " + hashedRequestId + ".");
        return "redirect:/user/access-request/create";
    }

    @GetMapping("/search")
    public String searchAccessRequest (final Model model,
                                       @ModelAttribute("requestStatusList") LinkedList<String> requestStatusList,
                                       @ModelAttribute("requestLocMap") TreeMap<String, SdAccessRequestLocData> requestLocMap) {
        // status list
        requestStatusList = sdAccessRequestFacade.getAccessRequestStatusList();
        if(!CollectionUtils.isEmpty(requestStatusList)){
            model.addAttribute("requestStatusList", requestStatusList);
        }

        // location list
        requestLocMap = sdAccessRequestFacade.getAccessRequestLocMap();
        model.addAttribute("requestLocMap", requestLocMap);

        // split internal and external user page
        boolean isInternalUser = sdUserFacade.isInternalUser();
        if( isInternalUser ){
            return "user/accessRequest/searchAccessRequestInternal";
        } else {
            return "user/accessRequest/searchAccessRequestExternal";
        }
    }


    @GetMapping("/ajax-search-request")
    public ResponseEntity<?> ajaxSearchRequest(
            @RequestParam(defaultValue = "0") int draw,
            @RequestParam(defaultValue = "0") int start,
            @RequestParam(defaultValue = "10") int length,
            @RequestParam(required= false) Integer hashedRequestId,
            @RequestParam(required= false) String companyName,
            @RequestParam(required= false) String status,
            @RequestParam(required= false) String visitLocation,
            @RequestParam(required= false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate visitDateFrom,
            @RequestParam(required= false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate visitDateTo) {
        int page = start/length;
        Pageable pageable = PageRequest.of(page, length);

        PageData<SdAccessRequestData> pageData = sdAccessRequestFacade.searchAccessRequest(pageable,
                hashedRequestId, companyName, status, visitLocation, visitDateFrom, visitDateTo );
        return ResponseEntityHelper.buildDataTablesResponse(draw, pageData);
    }

    @GetMapping("/view")
    public String viewAccessRequest (final Model model,
                                     @RequestParam Integer hashedRequestId,
                                     @ModelAttribute("accessRequestData") SdAccessRequestData sdAccessRequestData) {
        sdAccessRequestData = sdAccessRequestFacade.getAccessRequestByRequestId(hashedRequestId);
        if(sdAccessRequestData ==null){
            model.addAttribute(PageMsgController.ERROR_MSG, "Cannot get hashed request with ID " + hashedRequestId + ".");
        }else{
            sdAuditTrailFacade.insertViewRequesterAuditTrail(sdAccessRequestData);
            sdAuditTrailFacade.insertViewRequestVisitorAuditTrail(sdAccessRequestData.getVisitorDataList());
            model.addAttribute("accessRequestData", sdAccessRequestData);
        }

        return "user/accessRequest/viewAccessRequest";
    }

    @GetMapping("/process")
    public String processAccessRequest (final Model model,
                                        final RedirectAttributes redirectAttributes,
                                        @RequestParam Integer hashedRequestId,
                                        @ModelAttribute("targetHashedRequestId") String targetHashedRequestId,
                                        @ModelAttribute("accessRequestData") SdAccessRequestData sdAccessRequestData) {
        // check is internal user
        boolean isInternalUser = sdUserFacade.isInternalUser();
        if(!isInternalUser){
            redirectAttributes.addFlashAttribute(PageMsgController.ERROR_MSG, "Cannot process access request with this account.");
            return "redirect:/user/access-request/pending";
        }

        if(hashedRequestId!=null){
            model.addAttribute("targetHashedRequestId", hashedRequestId);
        }

        return "user/accessRequest/processAccessRequest";
    }

    @GetMapping("/process/ajax-get-basic-info")
    public ResponseEntity<?> getRequestBasicInfo(@RequestParam Integer hashedRequestId) {
        SdAccessRequestData sdAccessRequestData = sdAccessRequestFacade.getAccessRequestBasicInfoByRequestId(hashedRequestId);
        if(sdAccessRequestData==null){
            return ResponseEntity.badRequest().body("Access request not available for processing.");
        }else {
            sdAuditTrailFacade.insertViewRequesterAuditTrail(sdAccessRequestData);
            return ResponseEntity.ok(sdAccessRequestData);
        }
    }

    @GetMapping("/process/ajax-get-visitor-list")
    public ResponseEntity<?> getRequestVisitor(@RequestParam Integer hashedRequestId) {
        List<SdAccessRequestVisitorData> visitorDataList = sdAccessRequestFacade.getAccessRequestVisitorListByRequestId(hashedRequestId);
        if(visitorDataList==null){
            return ResponseEntity.badRequest().body("Visitor list not available for processing.");
        }else {
            sdAuditTrailFacade.insertViewRequestVisitorAuditTrail(visitorDataList);
            return ResponseEntity.ok(visitorDataList);
        }
    }

    @GetMapping("/process/ajax-get-equip-list")
    public ResponseEntity<?> getRequestEquipList(@RequestParam Integer hashedRequestId) {
        List<SdAccessRequestEquipData> equipDataList = sdAccessRequestFacade.getAccessRequestEquipListByRequestId(hashedRequestId);
        if(equipDataList==null){
            return ResponseEntity.badRequest().body("Equipment list not available.");
        }else {
            return ResponseEntity.ok(equipDataList);
        }
    }

    @PostMapping("/process/check-in")
    public ResponseEntity<?> checkIn(@RequestParam Integer visitorAccessId,
                                     @RequestParam String visitorCardNum) {
        String errorMsg = sdAccessRequestFacade.checkinAccessRequestVisitor(visitorAccessId, visitorCardNum);
        if(errorMsg==null){
            return ResponseEntity.ok(SimpleAjaxResponse.of());
        }else {
            return ResponseEntity.ok(SimpleAjaxResponse.of(false, errorMsg));
        }
    }

    @PostMapping("/process/check-out")
    public ResponseEntity<?> checkOut(@RequestParam Integer visitorAccessId) {
        String errorMsg = sdAccessRequestFacade.checkoutAccessRequestVisitor(visitorAccessId);
        if(errorMsg==null){
            return ResponseEntity.ok(SimpleAjaxResponse.of());
        }else {
            return ResponseEntity.ok(SimpleAjaxResponse.of(false, errorMsg));
        }
    }

    @GetMapping("/ajax-get-opt-hist")
    public ResponseEntity<?> getRequestOperationHist(@RequestParam Integer hashedRequestId) {
        List<SdOperationHistData> operationHistDataList = sdAccessRequestFacade.getOperationHistList(hashedRequestId);
        if(operationHistDataList==null){
            return ResponseEntity.badRequest().body("Cannot get operation history of " + hashedRequestId + ".");
        }else {
            return ResponseEntity.ok(operationHistDataList);
        }
    }

    @GetMapping("/pending")
    public String pendingAccessRequest (
            final Model model,
            @ModelAttribute("requestLocMap") TreeMap<String, SdAccessRequestLocData> requestLocMap) {
        // location list
        requestLocMap = sdAccessRequestFacade.getAccessRequestLocMap();
        model.addAttribute("requestLocMap", requestLocMap);

        boolean isInternalUser = sdUserFacade.isInternalUser();
        if( isInternalUser ){
            return "user/accessRequest/pendingAccessRequestInternal";
        } else {
            return "user/accessRequest/pendingAccessRequestExternal";
        }
    }

    @GetMapping("/ajax-list-pending")
    public ResponseEntity<?> ajaxGetPendingAccessRequest (
            @RequestParam(required= false) String targetLocation ) {
        List<SdAccessRequestData> dataList = sdAccessRequestFacade.getPendingAccessRequest(targetLocation);
        if (dataList==null){
            return ResponseEntity.badRequest().body("Cannot get pending access request.");
        } else {
            return ResponseEntity.ok(dataList);
        }
    }

    @GetMapping("/ajax-list-recent-completed")
    public ResponseEntity<?> ajaxGetRecentCompletedAccessRequest (
            @RequestParam(required= false) String targetLocation ) {
        List<SdAccessRequestData> dataList = sdAccessRequestFacade.getRecentCompletedAccessRequest(targetLocation);
        if (dataList==null){
            return ResponseEntity.badRequest().body("Cannot get pending access request.");
        } else {
            return ResponseEntity.ok(dataList);
        }
    }

    @GetMapping("/search-visitor")
    public String searchVisitor (
            final Model model,
            @ModelAttribute("requestLocMap") TreeMap<String, SdAccessRequestLocData> requestLocMap) {
        // location list
        requestLocMap = sdAccessRequestFacade.getAccessRequestLocMap();
        model.addAttribute("requestLocMap", requestLocMap);

        // split internal and external user page
        boolean isInternalUser = sdUserFacade.isInternalUser();
        if( isInternalUser ){
            return "user/accessRequest/searchVisitorInternal";
        }else {
            model.addAttribute(PageMsgController.ERROR_MSG, "Visitor search function is for internal user only.");
            return "public/contactUs";
        }
    }

    @GetMapping("/ajax-search-visitor")
    public ResponseEntity<?> ajaxSearchVisitor(
            @RequestParam(defaultValue = "0") int draw,
            @RequestParam(defaultValue = "0") int start,
            @RequestParam(defaultValue = "10") int length,
            @RequestParam(required= false) String visitorName,
            @RequestParam(required= false) String companyName,
            @RequestParam(required= false) String visitLocation,
            @RequestParam(required= false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate visitDateFrom,
            @RequestParam(required= false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate visitDateTo) {
        int page = start/length;
        Pageable pageable = PageRequest.of(page, length);

        PageData<SdAccessRequestVisitorData> pageData = sdAccessRequestFacade.searchAccessRequestVisitor(pageable,
                visitorName, companyName, visitLocation, visitDateFrom, visitDateTo );
        return ResponseEntityHelper.buildDataTablesResponse(draw, pageData);
    }

}
