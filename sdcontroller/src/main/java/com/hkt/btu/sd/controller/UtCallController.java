package com.hkt.btu.sd.controller;

import com.hkt.btu.sd.facade.UtApiFacade;
import com.hkt.btu.sd.facade.data.*;
import com.hkt.btu.sd.controller.response.SimpleAjaxResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.apache.commons.lang3.StringUtils;

import javax.annotation.Resource;
import java.util.*;

@Controller
@RequestMapping("/system/ut-call")
public class UtCallController {

    @Resource(name = "utApiFacade")
    UtApiFacade utApiFacade;

    @GetMapping("")
    public String redirectToUTCall() {
        return "system/utCall/utCall";
    }

    @PostMapping("/trigger-new-ut-call")
    public String triggerNewUTCall(final RedirectAttributes redirectAttributes,
                                   @ModelAttribute("triggerNewBSNNum") String triggerNewBSNNum){

        String resultMsg = utApiFacade.newUtCallRequest(triggerNewBSNNum, null);

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

        String resultMsg = utApiFacade.newUtCallResult(utCallId, serviceCode);

        if (resultMsg==null){
            return ResponseEntity.ok(SimpleAjaxResponse.of());
        }
        else {
            return ResponseEntity.badRequest().body("get result failed.");
        }
    }

    @GetMapping("/ut-call-record-list")
    public ResponseEntity<?> getUTCallRequestRecordList() {
        List<SdUtCallPageData> utCallRecordList = utApiFacade.getUTCallRequestRecordList();
        return ResponseEntity.ok(utCallRecordList);
    }

    @PostMapping("/ajax-trigger-ut-call")
    public ResponseEntity<?> ajaxreTriggerUTCall(@RequestParam String bsnNum){
        String resultMsg = utApiFacade.newUtCallRequest(bsnNum, null);

        if (resultMsg==null){
            return ResponseEntity.ok(SimpleAjaxResponse.of());
        }
        else {
            return ResponseEntity.badRequest().body("trigger failed.");
        }
    }
}
