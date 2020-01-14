package com.hkt.btu.sd.controller;

import com.hkt.btu.sd.facade.UTCallFacade;
import com.hkt.btu.sd.facade.data.*;
import com.hkt.btu.sd.controller.response.SimpleAjaxResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.session.SessionInformation;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.apache.commons.lang3.StringUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/system/ut-call")
public class UTCallController {

    @Resource
    UTCallFacade utCallFacade;

    @GetMapping("")
    public String redirectToUTCall() {
        return "system/utCall/utCall";
    }

    @PostMapping("/trigger-new-ut-call")
    public String triggerNewUTCall(final RedirectAttributes redirectAttributes,
                                   @ModelAttribute("triggerNewBSNNum") String triggerNewBSNNum){
        UTCallRequestTempData requestData = utCallFacade.triggerNewUTCall(triggerNewBSNNum);
        String resultMsg = utCallFacade.insertNewUTCallRequestRecord(triggerNewBSNNum, requestData.getCODE(), requestData.getMSG(),
                                                                requestData.getSERVICECODE(), requestData.getSEQ(), requestData.getSEQTYPE());

        if (resultMsg==null){
            redirectAttributes.addFlashAttribute(PageMsgController.INFO_MSG, "UT Call successfully");
        }
        else {
            redirectAttributes.addFlashAttribute(PageMsgController.ERROR_MSG, resultMsg);
        }
        return "redirect:/system/ut-call";
    }

    @PostMapping("/ajax-ut-call-get-request-result")
    public ResponseEntity<?> utCallGetResult(@RequestParam String utCallId, @RequestParam String serviceCode){
        if (StringUtils.isEmpty(utCallId)||StringUtils.isEmpty(serviceCode)||serviceCode.equals("null")||utCallId.equals("null")){
            return ResponseEntity.badRequest().body("Cannot Get Result with empty UT Call Id or Service Code");
        }

        UTCallProgressData resultData = utCallFacade.checkNewUTCallProgress(serviceCode, "2");
        String resultMsg = utCallFacade.insertNewUTCallResultRecord(utCallId, resultData.getCODE(),
                                                                    resultData.getMSG(), resultData.getACTIONDATA().getUT_SUMMARY());

        if (resultMsg==null){
            String updateMsg = utCallFacade.updateRequestAfterGetResult(utCallId);
            if (updateMsg==null){
                return ResponseEntity.ok(SimpleAjaxResponse.of());
            }
            else {
                return ResponseEntity.badRequest().body("update request failed.");
            }
        }
        else {
            return ResponseEntity.badRequest().body("get result failed.");
        }
    }

    @GetMapping("/ut-call-record-list")
    public ResponseEntity<?> getUTCallRequestRecordList() {
        List<UTCallPageData> utCallRecordList = utCallFacade.getUTCallRequestRecordList();
        return ResponseEntity.ok(utCallRecordList);
    }

    @PostMapping("/ajax-re-trigger-ut-call")
    public ResponseEntity<?> ajaxreTriggerUTCall(@RequestParam String bsnNum){
        UTCallRequestTempData requestData = utCallFacade.triggerNewUTCall(bsnNum);
        String resultMsg = utCallFacade.insertNewUTCallRequestRecord(bsnNum, requestData.getCODE(), requestData.getMSG(),
                                                                requestData.getSERVICECODE(), requestData.getSEQ(), requestData.getSEQTYPE());
        if (resultMsg==null){
            return ResponseEntity.ok(SimpleAjaxResponse.of());
        }
        else {
            return ResponseEntity.badRequest().body("re-trigger failed.");
        }
    }
}
