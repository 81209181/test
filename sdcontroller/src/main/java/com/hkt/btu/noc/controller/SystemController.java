package com.hkt.btu.noc.controller;

import com.hkt.btu.noc.controller.response.SimpleAjaxResponse;
import com.hkt.btu.noc.facade.NocConfigParamFacade;
import com.hkt.btu.noc.facade.NocJobFacade;
import com.hkt.btu.noc.facade.NocSiteConfigFacade;
import com.hkt.btu.noc.facade.data.NocConfigParamData;
import com.hkt.btu.noc.facade.data.NocCronJobInstData;
import com.hkt.btu.noc.facade.data.NocCronJobProfileData;
import com.hkt.btu.noc.facade.data.NocSiteConfigData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;
import java.util.List;


@SuppressWarnings("SameReturnValue")
@Controller
@RequestMapping(value = "/system")
public class SystemController {

    @Autowired
    NocConfigParamFacade nocConfigParamFacade;
    @Autowired
    NocSiteConfigFacade nocSiteConfigFacade;
    @Autowired
    NocJobFacade nocJobFacade;


    @GetMapping({"", "/", "/index"})
    public String index() {
        return "redirect:/system/config-param/search";
    }

    @GetMapping({"/config-param/search"})
    public String searchConfigParam() {
        return "system/configParam/searchConfigParam";
    }

    @GetMapping({"/config-param/ajax-list"})
    public ResponseEntity<?> listConfigParam() {
        List<NocConfigParamData> dataList = nocConfigParamFacade.getAllConfigParam();

        if(dataList==null) {
            return ResponseEntity.badRequest().body("Config param list not found.");
        } else {
            return ResponseEntity.ok(dataList);
        }
    }

    @GetMapping({"/site-config", "/site-config/", "/site-config/index"})
    public String siteConfig(){
        return "system/siteConfig/siteInstance";
    }
    @GetMapping("/site-config/ajax-get-site-instance")
    public ResponseEntity<?> ajaxGetSiteInstance(){
        NocSiteConfigData nocSiteInstanceData = nocSiteConfigFacade.getSiteInstance();
        if(nocSiteInstanceData==null) {
            return ResponseEntity.badRequest().body("Cannot load site instance.");
        } else {
            return ResponseEntity.ok(nocSiteInstanceData);
        }
    }

    @PostMapping("/site-config/reload")
    public ResponseEntity<?> reloadSiteConfig(){
        nocSiteConfigFacade.reload();
        return ResponseEntity.ok("done");
    }

    @GetMapping("/job/list")
    public String listJob(){
        return "system/job/listJob";
    }

    @GetMapping("/job/ajax-list-job-inst")
    public ResponseEntity<?> ajaxListJobInst(){
        List<NocCronJobInstData> jobDataList = nocJobFacade.getAllJobInstance();

        if(jobDataList==null) {
            return ResponseEntity.badRequest().body("Job inst list not found.");
        } else {
            return ResponseEntity.ok(jobDataList);
        }
    }

    @PostMapping("/job/ajax-resume-job")
    public ResponseEntity<?> ajaxResumeJob(@RequestParam String keyGroup,
                                           @RequestParam String keyName ){
        String errorMsg = nocJobFacade.resumeJob(keyGroup, keyName);

        if(errorMsg==null){
            return ResponseEntity.ok(SimpleAjaxResponse.of());
        }else {
            return ResponseEntity.ok(SimpleAjaxResponse.of(false, errorMsg));
        }
    }

    @PostMapping("/job/ajax-pause-job")
    public ResponseEntity<?> ajaxPauseJob(@RequestParam String keyGroup,
                                          @RequestParam String keyName ){
        String errorMsg = nocJobFacade.pauseJob(keyGroup, keyName);

        if(errorMsg==null){
            return ResponseEntity.ok(SimpleAjaxResponse.of());
        }else {
            return ResponseEntity.ok(SimpleAjaxResponse.of(false, errorMsg));
        }
    }

    @PostMapping("/job/ajax-trigger-job")
    public ResponseEntity<?> ajaxTriggerJob(@RequestParam String keyGroup,
                                            @RequestParam String keyName ){
        String errorMsg = nocJobFacade.triggerJob(keyGroup, keyName);

        if(errorMsg==null){
            return ResponseEntity.ok(SimpleAjaxResponse.of());
        }else {
            return ResponseEntity.ok(SimpleAjaxResponse.of(false, errorMsg));
        }
    }

    @GetMapping("/job/ajax-list-job-profile")
    public ResponseEntity<?> ajaxListJobProfile(){
        List<NocCronJobProfileData> jobDataList = nocJobFacade.getAllJobProfile();

        if(jobDataList==null) {
            return ResponseEntity.badRequest().body("Job profile list not found.");
        } else {
            return ResponseEntity.ok(jobDataList);
        }
    }

    @PostMapping("/job/ajax-activate-job")
    public ResponseEntity<?> ajaxActivateJob(@RequestParam String keyGroup,
                                             @RequestParam String keyName ){
        String errorMsg = nocJobFacade.activateJob(keyGroup, keyName);

        if(errorMsg==null){
            return ResponseEntity.ok(SimpleAjaxResponse.of());
        }else {
            return ResponseEntity.ok(SimpleAjaxResponse.of(false, errorMsg));
        }
    }

    @PostMapping("/job/ajax-deactivate-job")
    public ResponseEntity<?> ajaxDeactivateJob(@RequestParam String keyGroup,
                                               @RequestParam String keyName ){
        String errorMsg = nocJobFacade.deactivateJob(keyGroup, keyName);

        if(errorMsg==null){
            return ResponseEntity.ok(SimpleAjaxResponse.of());
        }else {
            return ResponseEntity.ok(SimpleAjaxResponse.of(false, errorMsg));
        }
    }

    @PostMapping("/job/ajax-sync-job")
    public ResponseEntity<?> ajaxSyncJob(@RequestParam String keyGroup,
                                         @RequestParam String keyName ){
        String errorMsg = nocJobFacade.syncJob(keyGroup, keyName);

        if(errorMsg==null){
            return ResponseEntity.ok(SimpleAjaxResponse.of());
        }else {
            return ResponseEntity.ok(SimpleAjaxResponse.of(false, errorMsg));
        }
    }
}
